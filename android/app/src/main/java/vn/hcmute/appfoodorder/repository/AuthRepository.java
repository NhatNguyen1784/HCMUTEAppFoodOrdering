package vn.hcmute.appfoodorder.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.AuthApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.EmailRequest;
import vn.hcmute.appfoodorder.model.dto.request.LoginRequest;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.model.dto.request.ResetPasswordRequest;
import vn.hcmute.appfoodorder.model.dto.request.UserUpdateDTO;
import vn.hcmute.appfoodorder.model.dto.request.VerifyResetPasswordRequest;
import vn.hcmute.appfoodorder.model.dto.response.ResetPasswordResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.utils.Resource;

public class AuthRepository {
    private final AuthApi authApi;
    private static AuthRepository instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public AuthRepository() {
        authApi = RetrofitClient.getRetrofit().create(AuthApi.class);
    }

    public static AuthRepository getInstance(){
        if (instance == null){
            instance = new AuthRepository();
        }
        return instance;
    }

    //Login
    public LiveData<ApiResponse<UserResponse>> loginAccount (String email, String password){
        MutableLiveData<ApiResponse<UserResponse>> data = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email,password);
        authApi.loginAccount(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    data.setValue(response.body());
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

    // send request reset mat khau
    public LiveData<Resource<ResetPasswordResponse>> requestResetPassword(String email, String newPassword){
        MutableLiveData<Resource<ResetPasswordResponse>> resultLiveData = new MutableLiveData<>();
        ResetPasswordRequest request = new ResetPasswordRequest(email, newPassword);

        authApi.requestResetPassword(request).enqueue(new Callback<ApiResponse<ResetPasswordResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResetPasswordResponse>> call, Response<ApiResponse<ResetPasswordResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<ResetPasswordResponse> apiResult = response.body();
                    if(apiResult.getCode() == 200){
                        resultLiveData.postValue(Resource.success(apiResult.getResult()));
                    }
                    else{
                        resultLiveData.postValue(Resource.error("ERROR code: " + apiResult.getCode(), apiResult.getResult()));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.postValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResetPasswordResponse>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }

    // resend otp reset mat khau
    public LiveData<Resource<ResetPasswordResponse>> resendOtpResetPassword(String email){
        MutableLiveData<Resource<ResetPasswordResponse>> resultLiveData = new MutableLiveData<>();

        authApi.resendOtpResetPassword(email).enqueue(new Callback<ApiResponse<ResetPasswordResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResetPasswordResponse>> call, Response<ApiResponse<ResetPasswordResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<ResetPasswordResponse> apiResult = response.body();
                    if(apiResult.getCode() == 200){
                        resultLiveData.postValue(Resource.success(apiResult.getResult()));
                    }
                    else{
                        resultLiveData.postValue(Resource.error("ERROR code: " + apiResult.getCode(), apiResult.getResult()));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.postValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResetPasswordResponse>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }

    // verify OTP & cap nhat mat khau
    public LiveData<Resource<Object>> verifyOtpResetPassword(VerifyResetPasswordRequest request){
        MutableLiveData<Resource<Object>> resultLiveData = new MutableLiveData<>();

        authApi.verifyOtpResetPassword(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Object> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.postValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.postValue(Resource.error("ERROR code: " + apiResult.getCode(), apiResult.getResult()));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.postValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }

    // Update Profile
    public LiveData<Resource<Object>> updateProfile(String token, UserUpdateDTO dto, File imgFile){
        MutableLiveData<Resource<Object>> resultLiveData = new MutableLiveData<>();

        // Convert JSON reviewRequest thành RequestBody
        String requestJson = gson.toJson(dto);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestJson);
        // Xử lý hình ảnh (nếu có)
        MultipartBody.Part imagePart = null;
        if (imgFile != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
            imagePart = MultipartBody.Part.createFormData("image", imgFile.getName(), fileBody);
        }

        authApi.updateProfile(token, requestBody, imagePart).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Object> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.postValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.postValue(Resource.error("ERROR code: " + apiResult.getCode(), apiResult.getResult()));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.postValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;

    }

}
