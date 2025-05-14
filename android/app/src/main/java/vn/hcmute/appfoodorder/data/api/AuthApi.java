package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.EmailRequest;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.model.dto.request.ResetPasswordRequest;
import vn.hcmute.appfoodorder.model.dto.request.VerifyResetPasswordRequest;
import vn.hcmute.appfoodorder.model.dto.response.ResetPasswordResponse;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.model.entity.Address;
import vn.hcmute.appfoodorder.utils.Resource;

public interface AuthApi {
    @POST("auth/login")
    Call<ApiResponse<UserResponse>> loginAccount(@Body LoginRequest loginRequest);

    @POST("auth/sendOtp")
    Call<ApiResponse<String>> sendOtpRegister(@Body EmailRequest emailRequest);

    @POST("auth/verifyOtp")
    Call<ApiResponse<String>> verifyOtp(@Body RegisterRequest request);

    @GET("auth/user/shipping-address")
    Call<ApiResponse<List<String>>> getAllAddresses(@Query("email") String email);

    @POST("auth/user/add-address")
    Call<ApiResponse<String>> addShippingAddress(@Body Address address);

    // gui yeu cau email, new password
    @POST("auth/reset-password/request")
    Call<ApiResponse<ResetPasswordResponse>> requestResetPassword(@Body ResetPasswordRequest request);

    // verify otp reset password
    @POST("auth/reset-password/verify")
    Call<ApiResponse<Object>> verifyOtpResetPassword(@Body VerifyResetPasswordRequest request);

    // resend otp reset password
    @POST("auth/reset-password/resend-otp")
    Call<ApiResponse<ResetPasswordResponse>> resendOtpResetPassword(@Query("email") String email);

    // update profile
    @Multipart
    @PUT("auth/update")
    Call<ApiResponse<Object>> updateProfile(
            @Part("userUpdate") RequestBody requestJson, // JSON của request
            @Part() MultipartBody.Part image); // anh kem theo neu co
}
