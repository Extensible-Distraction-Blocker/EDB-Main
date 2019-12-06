package org.edb.main.network;

import com.google.gson.JsonObject;
import org.edb.main.network.post.postSignupResponse;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import org.edb.main.network.get.getUserInfoResponse;
import org.edb.main.network.post.postLoginResponse;

public interface userNetworkService {
    //회원정보조회
    //반환되는 타입은 call 객체타입의 형태로 기술
    @GET("auth/{userIdx}")
    Call<getUserInfoResponse> getUserInfoAPI(@Path("userIdx") int userIdx);

    //회원가입
    @POST("auth/signup")
    Call<postSignupResponse> postSignupAPI(@Body() JsonObject body);


    //로그인
    @POST("auth/signin")
    Call<postLoginResponse> postLoginAPI(@Body() JsonObject body);


}
