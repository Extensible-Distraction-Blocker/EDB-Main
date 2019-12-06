package org.edb.main.network;

import com.google.gson.JsonObject;

import org.edb.main.*;
import org.edb.main.network.get.getAvailableExternalServiceResponse;
import org.edb.main.network.get.getExternalServiceDetailListResponse;
import org.edb.main.network.get.getExternalServiceListResponse;

import org.edb.main.network.get.*;

import org.edb.main.network.post.postLoginResponse;
import org.edb.main.network.post.postPluginDetailResponse;
import org.edb.main.network.post.postSignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestAPIRequester  implements ServerRequester {

    private ServerResponseHandler serverResponseHandler;
    private EDBPluginManager pluginManager;

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void setServerResponseHandler(ServerResponseHandler serverResponseHandler) {
        this.serverResponseHandler = serverResponseHandler;
    }

    private String getToken() {
        String token;
        try {
            token = User.getUser().getToken();
        } catch (RuntimeException runtimeException) {
            token = "dummy";
        }
        return token;
    }

    @Override
    public void requestAvailableExternalServices() {
        String token = getToken();

        Call<getAvailableExternalServiceResponse> getAvailableExternalServiceResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getAvailableExternalServiceListAPI();

        getAvailableExternalServiceResponseCall.enqueue(new Callback<getAvailableExternalServiceResponse>() {

            @Override
            public void onResponse(Call<getAvailableExternalServiceResponse> call, Response<getAvailableExternalServiceResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        int status = response.body().getStatus();
                        if (status == 200) {
                            System.out.print("\navilable service\n");

                            serverResponseHandler.handleAvailableExternalServiceResponse(response.body().getData());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getAvailableExternalServiceResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });

    }

    @Override
    public void requestUserExternalServices() {
        String token = getToken();


        Call<getExternalServiceListResponse> getExternalServiceListResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getExternalServiceListAPI(token);

        getExternalServiceListResponseCall.enqueue(new Callback<getExternalServiceListResponse>() {

            @Override
            public void onResponse(Call<getExternalServiceListResponse> call, Response<getExternalServiceListResponse> response) {
                if (response.isSuccessful()) {
                    serverResponseHandler.handleUserExternalServiceResponse(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<getExternalServiceListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

        });


    }

    @Override
    public void requestExternalServiceDetails(int externalIdx) {

        String token = getToken();

        //선택한 외부서비스의 목표달성테이블 데이터받기
        //요청값 externalIdx,token //응답값  external_service_detail_idx(목표달성idx),name (목표달성이름),if_achieve(달성여부 1 = 목표 달성 , 0 = 목표달성x)
        //@GET("external/detail/{externalIdx}")

        Call<getExternalServiceDetailListResponse> getExternalServiceDetailListResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getExternalServiceDetailListAPI(externalIdx, token);

        getExternalServiceDetailListResponseCall.enqueue(new Callback<getExternalServiceDetailListResponse>() {

            @Override
            public void onResponse(Call<getExternalServiceDetailListResponse> call, Response<getExternalServiceDetailListResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        //데이터 받기 성공시 테이블과 체크박스 보여주기
                        serverResponseHandler.handleExternalServiceDetailsResponse(externalIdx, response.body().getData());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getExternalServiceDetailListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

        });
    }

    @Override
    public void requestLogin(String id, String pw) {
        System.out.print("db connect login test\n");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("passwd", pw);

        Call<postLoginResponse> postLoginResponseCall =
                RestApiConnector.getUserNetworkService().postLoginAPI(jsonObject);

        postLoginResponseCall.enqueue(new Callback<postLoginResponse>() {

            @Override
            public void onResponse(Call<postLoginResponse> call, Response<postLoginResponse> response) {


                boolean sucessful=response.isSuccessful();
                String tempToken="null";
                if (sucessful) {
                    System.out.print("db connect success\n");
                    tempToken=response.body().getToken();
                }
                serverResponseHandler.handleLoginResponse(sucessful,id,tempToken);
            }

            @Override
            public void onFailure(Call<postLoginResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });

    }


    //load userPluginsList //token
    @Override
    public void requestUserPlugins() {

        System.out.print("user pluginlist test\n");

        String token = getToken();

        Call<getPluginListResponse> getPluginListResponseCall =
                RestApiConnector.getPluginService().getPluginListAPI(token);

        getPluginListResponseCall.enqueue(new Callback<getPluginListResponse>() {

            @Override
            public void onResponse(Call<getPluginListResponse> call, Response<getPluginListResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        int status = response.body().getStatus();
                        if (status == 200) {
                            System.out.print("\nRestAPIRequester.java의 requestUserPlugins()\n\n");
                            System.out.print(response.body().getData());
                            serverResponseHandler.handleUserPluginResponse(response.body().getData());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getPluginListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });
    }

    // 2. 잠금정책등록 선택시 추가가능한 잠금정책목록보여주기
    @Override
    public void requestAvailablePlugins() {

        String token = getToken();

        Call<getAvailablePluginResponse> getAvailablePluginResponseCall =
                RestApiConnector.getPluginService().getAvailablePluginListAPI();

        getAvailablePluginResponseCall.enqueue(new Callback<getAvailablePluginResponse>() {

            @Override
            public void onResponse(Call<getAvailablePluginResponse> call, Response<getAvailablePluginResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        int status = response.body().getStatus();
                        if (status == 200) {

                            System.out.print("\nRestAPIRequester.java의 requestAvailablePlugins()\n\n");
                            System.out.print(response.body().getData().get(1).getName());
                            serverResponseHandler.handleAvailablePluginResponse(response.body().getData());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getAvailablePluginResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });

    }

    //특정잠금정책 설정 조회
    @Override
    public void requestPluginDetails(int pluginIdx) {
        String token = getToken();

        Call<getPluginDetailListResponse> getPluginDetailListResponseCall =
                RestApiConnector.getPluginService().getPluginDetailListAPI(pluginIdx, token);

        getPluginDetailListResponseCall.enqueue(new Callback<getPluginDetailListResponse>() {

            @Override
            public void onResponse(Call<getPluginDetailListResponse> call, Response<getPluginDetailListResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        System.out.print("\nRestAPIRequester.java의 requestPluginDetails()\n\n");
                        System.out.print(response.body().getData().get(0).getConfiguration());

                        serverResponseHandler.handlePluginDetailsResponse(pluginIdx, response.body().getData().get(0),new TempJsonConverter());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getPluginDetailListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

        });
    }

    public void requestPostUserPlugin(int pluginIdx){
//        실질적으로 post시 이 메서드를 활용함
        JsonObject jsonObject = getJsonObjectFromPluginManager(pluginIdx);

        postUserPluginWithJsonObject(pluginIdx, jsonObject);
    }

    protected void postUserPluginWithJsonObject(int pluginIdx, JsonObject jsonObject) {
//        테스트 목적 메소드 분리 정의
        String token= getToken();

        Call<postPluginDetailResponse> postPluginDetailResponseCall =
                RestApiConnector.getPluginService()
                        .postPluginDetailAPI(pluginIdx,token,jsonObject);

        postPluginDetailResponseCall.enqueue(new Callback<postPluginDetailResponse>() {

            @Override
            public void onResponse(Call<postPluginDetailResponse> call, Response<postPluginDetailResponse> response) {

                boolean sucessful = response.isSuccessful();
                if (sucessful) {
                    System.out.print("db connect token success\n");
                    System.out.print(response.body().getMessage());

                }
                serverResponseHandler.handlePostUserPluginResponse(pluginIdx);
            }

            @Override
            public void onFailure(Call<postPluginDetailResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });
    }

    private JsonObject getJsonObjectFromPluginManager(int pluginIdx) {
        TempJsonConverter jsonConverter = new TempJsonConverter();
        pluginManager.collectConfigs(pluginIdx, jsonConverter);
        JsonObject jsonObject = jsonConverter.getJsonObjectForPost();

        System.out.println(jsonObject.toString());
        return jsonObject;
    }
    @Override
    public void requestSingup(String id, String pw,String email,String grade) {

        System.out.print("db connect signup test\n");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("passwd", pw);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("grade",grade);


        Call<postSignupResponse> postSignupResponseCall =
                RestApiConnector.getUserNetworkService().postSignupAPI(jsonObject);

        postSignupResponseCall.enqueue(new Callback<postSignupResponse>() {

            @Override
            public void onResponse(Call<postSignupResponse> call, Response<postSignupResponse> response) {

                boolean success = response.body().getStatus() == 200? true : false;


                serverResponseHandler.handleSignupResponse(success);
            }

            @Override
            public void onFailure(Call<postSignupResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }
        });

    }
}
