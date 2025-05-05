package vn.hcmute.appfoodorder.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.request.VerifyResetPasswordRequest;
import vn.hcmute.appfoodorder.model.dto.response.ResetPasswordResponse;
import vn.hcmute.appfoodorder.repository.AuthRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class ResetPassViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Resource<ResetPasswordResponse>> resetPasswordResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<ResetPasswordResponse>> resendOtpResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Object>> verifyOtpResult = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isOtpSending = new MutableLiveData<>(false);
    public ResetPassViewModel() {
        this.authRepository = AuthRepository.getInstance();
    }

    // gui yeu cau reset mat khau
    public void requestResetPassword(String email, String newPassword){
        if (Boolean.TRUE.equals(isOtpSending.getValue()))
            return;

        isOtpSending.setValue(true);

        authRepository.requestResetPassword(email, newPassword).observeForever(new Observer<Resource<ResetPasswordResponse>>() {
            @Override
            public void onChanged(Resource<ResetPasswordResponse> result) {
                resetPasswordResult.setValue(result);
                isOtpSending.setValue(false);
            }
        });
    }
    // gui lai Otp
    public void resendOtpResetPassword(String email){
        if (Boolean.TRUE.equals(isOtpSending.getValue()))
            return;

        isOtpSending.setValue(true);

        authRepository.resendOtpResetPassword(email).observeForever(new Observer<Resource<ResetPasswordResponse>>() {
            @Override
            public void onChanged(Resource<ResetPasswordResponse> result) {
                resendOtpResult.setValue(result);
                isOtpSending.setValue(false);
            }
        });
    }

    // xac thuc otp va cap nhat mat khau
    public void verifyOtpResetPassword(String email, String otpCode){
        VerifyResetPasswordRequest request = new VerifyResetPasswordRequest(email, otpCode);

        authRepository.verifyOtpResetPassword(request).observeForever(new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> result) {
                verifyOtpResult.setValue(result);
            }
        });
    }
    public LiveData<Boolean> getIsOtpSending() {
        return isOtpSending;
    }

    public LiveData<Resource<ResetPasswordResponse>> getResetPasswordResult() {
        return resetPasswordResult;
    }

    public LiveData<Resource<ResetPasswordResponse>> getResendOtpResult() {
        return resendOtpResult;
    }

    public LiveData<Resource<Object>> getVerifyOtpResult() {
        return verifyOtpResult;
    }
}
