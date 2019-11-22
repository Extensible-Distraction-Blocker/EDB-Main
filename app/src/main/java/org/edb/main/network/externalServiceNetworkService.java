package org.edb.main.network;

import org.edb.main.network.get.getAvailableExternalServiceResponse;
import org.edb.main.network.get.getExternalServiceDetailListResponse;
import org.edb.main.network.get.getExternalServiceListResponse;
import org.edb.main.network.post.postExternalServiceResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface externalServiceNetworkService {

    @GET("external/")
    Call<getExternalServiceListResponse> getExternalServiceListAPI(@Header("token") String token);

    @GET("external/available")
    Call<getAvailableExternalServiceResponse> getAvailableExternalServiceListAPI();

    @PUT("external/{externalIdx}/{externalDetailIdx}")
    Call<postExternalServiceResponse> postExternalServiceListAPI(@Path("externalIdx") int externalIdx, @Path("externalDetailIdx") int externalDetailIdx ,@Header("token") String token);

    @GET("external/detail/{externalIdx}")
    Call<getExternalServiceDetailListResponse> getExternalServiceDetailListAPI(@Path("externalIdx") int externalIdx, @Header("token") String token);



}
