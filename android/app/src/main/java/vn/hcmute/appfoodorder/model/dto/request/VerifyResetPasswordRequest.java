package vn.hcmute.appfoodorder.model.dto.request;

import java.io.Serializable;

public class VerifyResetPasswordRequest implements Serializable {
    private String email;
    private String otpCode;

    public VerifyResetPasswordRequest() {
    }

    public VerifyResetPasswordRequest(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
