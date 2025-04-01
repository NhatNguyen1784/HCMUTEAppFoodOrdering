package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.InformationRegisterAccount;
import vn.hcmute.appfoodorder.model.dto.request.EmailRequest;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.repository.AuthRepository;

public class VerifyOtpViewModel extends ViewModel {
    private final AuthRepository authRepository;
    public MutableLiveData<String> otpCode = new MutableLiveData<>("");
    private InformationRegisterAccount inf;
    private final MutableLiveData<ApiResponse<String>> _verifyOtp = new MutableLiveData<>();
    public LiveData<ApiResponse<String>> verifyOtp = _verifyOtp;

    private final MutableLiveData<ApiResponse<String>> _resendOtp = new MutableLiveData<>();
    public LiveData<ApiResponse<String>> resendOtp = _resendOtp;
    public VerifyOtpViewModel() {
        authRepository = new AuthRepository();
    }

    //Set dữ liệu đã truyền từ Register Activity Sang VerifyOtpActivity
    public InformationRegisterAccount getInf() {
        return inf;
    }

    public void setInf(InformationRegisterAccount inf) {
        this.inf = inf;
    }

    //Register
    public void VerifyOtp(){
        if(inf != null){
            String otpCodeValue = otpCode.getValue();
            RegisterRequest request = new RegisterRequest(otpCodeValue, inf);
            if(otpCodeValue!= null){
                authRepository.verifyOtp(request).observeForever(response -> {
                    _verifyOtp.postValue(response);
                });
            } else {
                _verifyOtp.setValue(new ApiResponse<>(400, "OTP hoặc thông tin đăng ký không hợp lệ", null));
            }
        }
        else _verifyOtp.setValue(new ApiResponse<>(404, "Not found information", null));
    }

    //Resend otp
    public void resendOtp(){
        if(inf!=null){
            String email = inf.getEmail();
            String phone = inf.getPhone();
            authRepository.sendOtp(email, phone).observeForever(response -> {
                _resendOtp.postValue(response);
            });
        }
        else {
            _resendOtp.setValue(new ApiResponse<>(404, "Not found information", null));
        }
    }
}
