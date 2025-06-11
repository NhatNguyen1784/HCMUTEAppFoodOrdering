package vn.hcmute.appfoodorder.model.dto.request;

import java.io.Serializable;
import java.util.Set;

public class OrderRequest implements Serializable {
    private String fullAddress;
    private String paymentOption;
    private String orderStatus;
    private String deliveryMethod;
    private Set<OrderDetailRequest> orderDetails;

    public OrderRequest() {
    }

    public OrderRequest(String fullAddress, String paymentOption, String orderStatus, String deliveryMethod, Set<OrderDetailRequest> orderDetails) {
        this.fullAddress = fullAddress;
        this.paymentOption = paymentOption;
        this.orderStatus = orderStatus;
        this.deliveryMethod = deliveryMethod;
        this.orderDetails = orderDetails;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDetails(Set<OrderDetailRequest> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        if (deliveryMethod == null) {
            this.deliveryMethod = "PICKUP";
        } else {
            this.deliveryMethod = deliveryMethod;
        }
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

    public Set<OrderDetailRequest> getOrderDetails() {
        return orderDetails;
    }
}
