package vn.hcmute.appfoodorder.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.AuthApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.EmailRequest;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;

public class AuthRepository {
    private final AuthApi authApi;

    public AuthRepository() {
        authApi = RetrofitClient.getRetrofit().create(AuthApi.class);
    }

    //Login
    public LiveData<ApiResponse<UserResponse>> loginAccount (String email, String password){
        MutableLiveData<ApiResponse<UserResponse>> data = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email,password);
        authApi.loginAccount(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    data.postValue(response.body());
                }
                else
                    data.postValue(new ApiResponse<>(500,"Login fail",null));
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {
                data.postValue(new ApiResponse<>(500, throwable.getMessage(), null));
            }
        });
        return data;
    }

    //Register (Send code)
    public LiveData<ApiResponse<String>> sendOtp(String email, String phone){
        MutableLiveData<ApiResponse<String>> data = new MutableLiveData<>();
        EmailRequest request = new EmailRequest(email, phone);
        authApi.sendOtpRegister(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getCode()==200){
                        data.postValue(response.body());
                    }
                    else data.postValue(new ApiResponse<>(500, "Send Otp fail", null));
                }
                else {
                    String errorMsg = "Không xác định";
                    if (response.body() != null) {
                        errorMsg = response.body().getMessage();
                    } else {
                        // Bạn có thể log response.errorBody() để debug thêm nếu muốn
                        errorMsg = "Lỗi không có nội dung phản hồi từ server hoặc chưa bật redis server";
                    }
                    data.postValue(new ApiResponse<>(500, "Fail: " + errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                data.postValue(new ApiResponse<>(500, throwable.getMessage(), null));
            }
        });
        return data;
    }

    //Verify OTP register
    public LiveData<ApiResponse<String>> verifyOtp (RegisterRequest request){
        MutableLiveData<ApiResponse<String>> data = new MutableLiveData<>();
        authApi.verifyOtp(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    data.postValue(response.body());
                }
                else data.setValue(new ApiResponse<>(500, response.message(), null));
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                data.setValue(new ApiResponse<>(400, "Error on failure: " + throwable.getMessage(), null));
            }
        });
        return data;
    }
}
