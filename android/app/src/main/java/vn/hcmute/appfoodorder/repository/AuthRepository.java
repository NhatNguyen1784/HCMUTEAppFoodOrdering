package vn.hcmute.appfoodorder.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.AuthApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
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
}
