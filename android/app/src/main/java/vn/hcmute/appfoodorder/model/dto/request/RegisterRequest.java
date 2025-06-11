package vn.hcmute.appfoodorder.model.dto.request;

import vn.hcmute.appfoodorder.model.dto.InforRegisAccount;

public class RegisterRequest {
    private String otpCode;
    private InforRegisAccount infUser;

    public RegisterRequest() {
    }

    public RegisterRequest(String otpCode, InforRegisAccount inf) {
        this.otpCode = otpCode;
        this.infUser = inf;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public InforRegisAccount getInfUser() {
        return infUser;
    }

    public void setInfUser(InforRegisAccount infUser) {
        this.infUser = infUser;
    }
}
