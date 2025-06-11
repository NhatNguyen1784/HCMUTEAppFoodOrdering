package vn.hcmute.appfoodorder.model.entity;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private Long id;
    private String email;
    private Double totalPrice;
    private List<CartItem> cartDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItem> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartItem> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public Cart() {
    }

    public Cart(Long id, String email, Double totalPrice, List<CartItem> cartDetails) {
        this.id = id;
        this.email = email;
        this.totalPrice = totalPrice;
        this.cartDetails = cartDetails;
    }
}
