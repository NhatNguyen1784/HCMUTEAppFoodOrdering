package vn.hcmute.appfoodorder.model.entity;

public class Address {
    private String email;
    private String fullAddress;

    public Address() {
    }

    public Address(String email, String fullAddress) {
        this.email = email;
        this.fullAddress = fullAddress;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
