package vn.hcmute.appfoodorder.model;

import java.io.Serializable;

public class UserLocalData implements Serializable {
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String urlImage;

    public UserLocalData(String email, String fullName, String phone, String address, String urlImage) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.urlImage = urlImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
