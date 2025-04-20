package vn.hcmute.appfoodorder.model.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;

    @SerializedName("urlImage")
    private String urlImage;

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
