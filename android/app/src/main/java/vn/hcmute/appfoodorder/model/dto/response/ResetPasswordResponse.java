package vn.hcmute.appfoodorder.model.dto.response;

import java.io.Serializable;

public class ResetPasswordResponse implements Serializable {
    private String email;
    private int expiresIn; // thoi gian het han OTP

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
