package vn.hcmute.appfoodorder.model.dto;

import java.io.Serializable;

public class InforRegisAccount implements Serializable {
    private String email;
    private String password;
    private String phone;
    private String fullName;
    private String address;
    private String urlImage;

    public InforRegisAccount(String email, String password, String phone, String fullName, String address) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.fullName = fullName;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
