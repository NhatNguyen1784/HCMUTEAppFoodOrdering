package vn.hcmute.appfoodorder.model.entity;

import java.util.Set;

import vn.hcmute.appfoodorder.model.dto.response.OrderDetailDTO;

public class OrderDetail {
    private Set<OrderDetailDTO> orderDetails;
    private String userName;
    private String phoneNumber;
    private String fullAddress;
    private String paymentOption;
    private String orderStatus;
    private String deliveryMethod;
    private Double totalPrice;
    private String createdDate;

    public OrderDetail() {
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public Set<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }
}
