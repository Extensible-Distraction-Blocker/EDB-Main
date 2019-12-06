package org.edb.main.network;

import com.google.gson.JsonObject;
import org.edb.main.*;
import org.edb.main.model.*;

import org.edb.main.ServerResponseHandler;

import org.edb.main.model.Object;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import java.util.ArrayList;
import java.util.Date;

import static org.awaitility.Awaitility.await;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;


public class RestAPIRequesterTest {

    private static RestAPIRequester restAPIRequester;

//    public static void main(String[] args) {
//        tempPostPluginDetail();
//
//        //Mockito를 이용한 callback test
////        ServerResponseHandler serverResponseHandler = mock(ServerResponseHandler.class);
//////
//////
//////        //callback boolean successful, String id
//////        doAnswer(new Answer() {
//////            @Override
//////            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//////                java.lang.Object[] args = invocationOnMock.getArguments();
//////                System.out.print("\nargs test33\n");
//////                System.out.print(args);
//////                System.out.print("\ntoken mock test33\n");
//////                System.out.print(User.getUser().getToken());//token
//////
//////                //잠금정책목록
////////                requestUserPlugins();
//////
//////                // 2. 잠금정책등록 선택시 추가가능한 잠금정책목록보여주기
//////                //requestAvailablePlugins();
//////
//////                //3.잠금정책목록에서 특정 잠금 정책 클릭 시 잠금정책설정 불러오기
////////                requestUserDetailPlugin();
////////'{"configuration":{"object":{"object_idx1":"Game.exe","object_idx2":"Chrome"}\n,"time":{"start_time":"09:20","end_time":"15:00"}}}'
//////                // 4. 잠금정책목록에서 특정 잠금 정책 화면에서 잠금정책설정 저장
//////                requestPostPluginDetail();
//////
//////
//////                requestUserDetailPlugin();
//////
//////                return null;
//////            }
//////        }).when(serverResponseHandler).handleLoginResponse(anyBoolean(), anyString(),anyString());
//    }

    @Test
    public void requestLoginTest(){
        /*
            response의 결과들의 이름을 ArrayList에 담는다.
            아무렇게나 지정해도 무관하지만, response 개수와는 일치시켜야함
         */
        ArrayList<String> responseNames=new ArrayList<String>();
        responseNames.add("로그인성공여부");
        responseNames.add("id");
        responseNames.add("token");

        /*
            RestAPIRequester의 반환내용 그대로가 아니라,
            ServerResponseHandler의 메소드를 호출할때의 매개변수를 중간에 취하는 방식.
            ServerResponseHandler 객체를 받아온다.
        */
        ServerResponseHandler serverResponseHandler=RestAPITestAPI.getMockServerResponseHandler();

        /*
            when(serverResponseHandler)까지는 그대로 사용해도 되며,
            그 이후는 response가 도착했을때 호출되는 ServerResponseHandler메소드를 호출.
            매개변수는 anyType()의 형태로 매개변수의 개수와 맞춰준다.
         */
        RestAPITestAPI.getStubber(responseNames).when(serverResponseHandler).handleLoginResponse(anyBoolean(),anyString(),anyString());




        restAPIRequester = new RestAPIRequester();
        restAPIRequester.setServerResponseHandler(serverResponseHandler);
        restAPIRequester.requestLogin("meme2367", "123");

    }

    // 1. 잠금정책목록
    public void requestUserPlugins() {
        restAPIRequester.requestUserPlugins();
    }

    // 2. 잠금정책등록 선택시 추가가능한 잠금정책목록보여주기
    public void requestAvailablePlugins(){ restAPIRequester.requestAvailablePlugins(); }

    // 3. 잠금정책목록에서 특정 잠금 정책 클릭 시 잠금정책설정 불러오기
    public void requestUserDetailPlugin(){restAPIRequester.requestPluginDetails(1);}
    // 4. 잠금정책목록에서 특정 잠금 정책 화면에서 잠 금정책설정 저장
    public void requestPostPluginDetail(){


        Time time = new Time("2019-11-19 12:00","2019-11-29 18:00");

        ArrayList<Object> objectArrayLists = new ArrayList<>();
        objectArrayLists.add(new Object("object1","Chrome"));
        objectArrayLists.add(new Object("object2","game.exe"));
        JsonObject objecttmp = new JsonObject();

        ArrayList<InactivateCondition> inactivateConditionArrayList = new ArrayList<>();
        inactivateConditionArrayList.add(new InactivateCondition("condition1","인강5개보기"));
        inactivateConditionArrayList.add(new InactivateCondition("condition2","6시간동안인터넷금지"));

        JsonObject inactivatetmp = new JsonObject();

        for(int i = 0;i<objectArrayLists.size();i++){
            objecttmp.addProperty(objectArrayLists.get(i).getObject_name(),objectArrayLists.get(i).getObject_value());
        }

        for(int i = 0;i<inactivateConditionArrayList.size();i++){
            inactivatetmp.addProperty(inactivateConditionArrayList.get(i)
                    .getInactivateCondition_name(),inactivateConditionArrayList.get(i).getInactivateCondition_value());
        }


        JsonConverter jsonConverter = new JsonConverter();
        jsonConverter.makeString("object",objecttmp);
        jsonConverter.makeString("inactivateCondition",inactivatetmp);
        jsonConverter.setTime(time);

        restAPIRequester.requestPostUserPlugin(1);

    }

    @Test
    public void tempRequestPluginDetail(){

        RestAPITestAPI.loginFotTest("jooha208","123");



    }

    @Test
    public void tempPostPluginDetail(){
        RestAPITestAPI.loginFotTest("jooha208","123");
        TestEDBPlugin testEDBPlugin = new TestEDBPlugin();

        testEDBPlugin.addTargetProgram(new TargetProgram("chrome","C:\\chrome"));
        testEDBPlugin.addTargetWebsite(new TargetWebsite("www.naver.com"));

        testEDBPlugin.applySingleConfig("TestPluginLogic","tempConfigInt","3");
        testEDBPlugin.applySingleConfig("TestPluginLogic","tempConfigStr", "tempStr");
        testEDBPlugin.applySchedule(new Date(), new Date());

        TempJsonConverter tempJsonConverter= new TempJsonConverter();
        testEDBPlugin.extractConfigs(tempJsonConverter);
        RestAPIRequester restAPIRequester1 = new RestAPIRequester();

        ServerResponseHandler serverResponseHandler = RestAPITestAPI.getMockServerResponseHandler();
        restAPIRequester1.setServerResponseHandler(serverResponseHandler);

        ArrayList<String> responseName = new ArrayList<String>();
        responseName.add("pluginIdx");
        RestAPITestAPI.getStubber(responseName).when(serverResponseHandler).handlePostUserPluginResponse(anyInt());

        JsonObject jsonObejct = tempJsonConverter.getJsonObjectForPost();

        System.out.println(jsonObejct.toString());

//        restAPIRequester1.postUserPluginWithJsonObject(1, tempJsonConverter.getJsonObjectForPost());
    }


    @Test
    public void tempSignup(){
        RestAPITestAPI.signupFotTest("jooha208","123","joonha208@gmail.com","SERVICE_PROVIDER");
    }
    
}
