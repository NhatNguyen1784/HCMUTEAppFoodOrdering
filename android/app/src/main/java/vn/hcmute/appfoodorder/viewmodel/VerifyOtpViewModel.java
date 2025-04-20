package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.InforRegisAccount;
import vn.hcmute.appfoodorder.model.dto.request.RegisterRequest;
import vn.hcmute.appfoodorder.repository.AuthRepository;

public class VerifyOtpViewModel extends ViewModel {
    private final AuthRepository authRepository;
    public MutableLiveData<String> otpCode = new MutableLiveData<>("");
    private InforRegisAccount inf;
    private final MutableLiveData<ApiResponse<String>> _verifyOtp = new MutableLiveData<>();
    public LiveData<ApiResponse<String>> verifyOtp = _verifyOtp;
    private final MutableLiveData<ApiResponse<String>> _resendOtp = new MutableLiveData<>();
    public LiveData<ApiResponse<String>> resendOtp = _resendOtp;
    public VerifyOtpViewModel() {
        authRepository = new AuthRepository();
    }

    //Set dữ liệu đã truyền từ Register Activity Sang VerifyOtpActivity
    public InforRegisAccount getInf() {
        return inf;
    }
    public void setInf(InforRegisAccount inf) {
        this.inf = inf;
    }
    private boolean isOtpSending = false;
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
            if (isOtpSending) {
                // Đang gửi OTP, không cho gửi lại
                _resendOtp.setValue(new ApiResponse<>(400, "OTP is being sent. Please wait.", null));
                return;
            }
            isOtpSending = true;  // Đánh dấu đang gửi OTP
            String email = inf.getEmail();
            String phone = inf.getPhone();
            authRepository.sendOtp(email, phone).observeForever(response -> {
                isOtpSending = false;
                _resendOtp.postValue(response);
            });
        }
        else {
            _resendOtp.setValue(new ApiResponse<>(404, "Not found information", null));
        }
    }
}
