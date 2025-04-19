package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.repository.AuthRepository;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<ApiResponse<UserResponse>> _loginResponse = new MutableLiveData<>();
    public LiveData<ApiResponse<UserResponse>> loginResponse = _loginResponse;

    private final AuthRepository authRepository;

    public LoginViewModel() {
        authRepository = new AuthRepository();
    }

    //Login
    public void login(){
        String emailValue = email.getValue();
        String passwordlValue = password.getValue();
        if(emailValue!=null && passwordlValue!= null){
            authRepository.loginAccount(emailValue, passwordlValue).observeForever(apiResponse -> {
                _loginResponse.postValue(apiResponse);
            });
        }
    }



}
