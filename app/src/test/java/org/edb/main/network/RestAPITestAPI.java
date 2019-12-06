package org.edb.main.network;

import org.edb.main.ServerResponseHandler;
import org.edb.main.User;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.ArrayList;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/*
        RestAPIRequester의 반환내용 그대로가 아니라,
        ServerResponseHandler의 메소드를 호출할때의 매개변수를 중간에 취하는 방식
*/
public class RestAPITestAPI {

    /*
    사용방식은 RestAPIRequesterTest의 requestLoginTest 참고.
     */
    public static ServerResponseHandler getMockServerResponseHandler(){
        ServerResponseHandler serverResponseHandler=mock(ServerResponseHandler.class);

        return serverResponseHandler;
    }

    public static Stubber getStubber(ArrayList<String> responseNames){
        return doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                int size=responseNames.size();
                Object[] args = invocationOnMock.getArguments();
                for (int i = 0; i <size ; i++) {
                    System.out.println(responseNames.get(i) + args[i].toString());
                }
                return null;
            }
        });
    }

    public static void loginFotTest(String id, String pw){
        ServerResponseHandler serverResponseHandler=RestAPITestAPI.getMockServerResponseHandler();

        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                User.login((String)args[1], (String)args[2]);
                return null;
            }
        }).when(serverResponseHandler).handleLoginResponse(anyBoolean(),anyString(),anyString());

        RestAPIRequester restAPIRequester=new RestAPIRequester();
        restAPIRequester.setServerResponseHandler(serverResponseHandler);
        restAPIRequester.requestLogin(id,pw);
    }


    public static void signupFotTest(String id, String pw,String email,String grade){
        ServerResponseHandler serverResponseHandler=mock(ServerResponseHandler.class);

        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                System.out.print("args test\n");
                System.out.print(args);
                return null;
            }
        }).when(serverResponseHandler).handleSignupResponse(anyBoolean());

        RestAPIRequester restAPIRequester=new RestAPIRequester();
        restAPIRequester.setServerResponseHandler(serverResponseHandler);
        restAPIRequester.requestSingup(id,pw,email,grade);
    }
}
