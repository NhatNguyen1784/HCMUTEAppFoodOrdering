package vn.hcmute.appfoodorder.model.dto.request;

import vn.hcmute.appfoodorder.model.dto.InformationRegisterAccount;

public class RegisterRequest {
    private String otpCode;
    private InformationRegisterAccount infUser;

    public RegisterRequest() {
    }

    public RegisterRequest(String otpCode, InformationRegisterAccount inf) {
        this.otpCode = otpCode;
        this.infUser = inf;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public InformationRegisterAccount getInfUser() {
        return infUser;
    }

    public void setInfUser(InformationRegisterAccount infUser) {
        this.infUser = infUser;
    }
}
