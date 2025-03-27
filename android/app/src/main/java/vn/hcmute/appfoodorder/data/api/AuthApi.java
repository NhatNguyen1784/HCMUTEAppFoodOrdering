package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;

public interface AuthApi {
    @POST("login")
    Call<ApiResponse<UserResponse>> loginAccount(@Body LoginRequest loginRequest);
}
