package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.EmailRequest;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;

public interface AuthApi {
    @POST("login")
    Call<ApiResponse<UserResponse>> loginAccount(@Body LoginRequest loginRequest);

    @POST("sendOtp")
    Call<ApiResponse<String>> sendOtpRegister(@Body EmailRequest emailRequest);

    @POST("verifyOtp")
    Call<ApiResponse<String>> verifyOtp(@Body RegisterRequest request);
}
