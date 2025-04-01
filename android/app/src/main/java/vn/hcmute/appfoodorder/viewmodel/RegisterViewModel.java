package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.InformationRegisterAccount;
import vn.hcmute.appfoodorder.repository.AuthRepository;
public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> confirmPass = new MutableLiveData<>("");
    public MutableLiveData<String> fullName = new MutableLiveData<>("");
    public MutableLiveData<String> phone = new MutableLiveData<>("");
    public MutableLiveData<String> address = new MutableLiveData<>("");
    private final AuthRepository authRepository;
    private final MutableLiveData<ApiResponse<String>> _registerResponse = new MutableLiveData<>();
    public LiveData<ApiResponse<String>> registerResponse = _registerResponse;
    private MutableLiveData<InformationRegisterAccount> information = new MutableLiveData<>();

    //Xư lý lỗi
    public MutableLiveData<String> emailError = new MutableLiveData<>("");
    public MutableLiveData<String> phoneError = new MutableLiveData<>("");
    public MutableLiveData<String> passwordError = new MutableLiveData<>("");
    public MutableLiveData<String> confirmPassError = new MutableLiveData<>("");
    public MutableLiveData<String> fullNameError = new MutableLiveData<>("");
    public MutableLiveData<String> addressError = new MutableLiveData<>("");

    public RegisterViewModel() {
        authRepository = new AuthRepository();
    }

    // Regex kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Regex kiểm tra số điện thoại (chỉ số, 10-11 chữ số)
    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{10,11}$");
    }

    //On click register button
    public void onRegisterButtonClick(){
        boolean isValid = true;

        // Kiểm tra email
        if (email.getValue() == null || email.getValue().isEmpty()) {
            emailError.setValue("Email không được để trống");
            isValid = false;
        } else if (!isValidEmail(email.getValue())) {
            emailError.setValue("Email không hợp lệ");
            isValid = false;
        } else {
            emailError.setValue("");
        }

        // Kiểm tra password
        if (password.getValue() == null || password.getValue().isEmpty()) {
            passwordError.setValue("Mật khẩu không được để trống");
            isValid = false;
        } else {
            passwordError.setValue("");
        }

        // Kiểm tra confirm password
        if (confirmPass.getValue() == null || !confirmPass.getValue().equals(password.getValue())) {
            confirmPassError.setValue("Mật khẩu xác nhận không khớp");
            isValid = false;
        } else {
            confirmPassError.setValue("");
        }

        // Kiểm tra full name
        if (fullName.getValue() == null || fullName.getValue().isEmpty()) {
            fullNameError.setValue("Họ và tên không được để trống");
            isValid = false;
        } else {
            fullNameError.setValue("");
        }

        // Kiểm tra phone
        if (phone.getValue() == null || phone.getValue().isEmpty()) {
            phoneError.setValue("Số điện thoại không được để trống");
            isValid = false;
        } else if (!isValidPhone(phone.getValue())) {
            phoneError.setValue("Số điện thoại không hợp lệ");
            isValid = false;
        } else {
            phoneError.setValue("");
        }

        // Kiểm tra address
        if (address.getValue() == null || address.getValue().isEmpty()) {
            addressError.setValue("Địa chỉ không được để trống");
            isValid = false;
        } else {
            addressError.setValue("");
        }

        // Nếu tất cả hợp lệ -> Chuyển qua VerifyOtpActivity
        if (isValid) {
            String emailValue = email.getValue();
            String passwordValue = password.getValue();
            String confirmPassValue = confirmPass.getValue();
            String fullNameValue = fullName.getValue();
            String phoneValue = phone.getValue();
            String addressValue = address.getValue();
            if(emailValue != null && passwordValue != null && confirmPassValue!= null && fullNameValue != null
                    && phoneValue!=null && addressValue != null){
                if (!confirmPassValue.equals(passwordValue)) {
                    _registerResponse.setValue(new ApiResponse<>(400, "Passwords do not match", null));
                    return;
                }
                InformationRegisterAccount info = new InformationRegisterAccount(
                        emailValue, passwordValue, fullNameValue, phoneValue, addressValue
                );
                setInformation(info);
                //Gui otp
                authRepository.sendOtp(emailValue, phoneValue).observeForever(response -> {
                    _registerResponse.postValue(response);
                });
            }
            else _registerResponse.setValue(new ApiResponse<>(400, "All fields are required", null));
        }
    }

    public InformationRegisterAccount getInformation(){
        return information.getValue();
    }

    public void setInformation(InformationRegisterAccount info) {
        information.setValue(info);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
