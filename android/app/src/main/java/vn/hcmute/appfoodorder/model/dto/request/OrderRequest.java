package vn.hcmute.appfoodorder.model.dto.request;

import java.util.Set;

public class OrderRequest {
    private String email;
    private String fullAddress;
    private String paymentOption;
    private String orderStatus;
    private String deliveryMethod;
    private Set<OrderDetailRequest> orderDetails;

    public OrderRequest() {
    }

    public OrderRequest(String email, String fullAddress, String paymentOption, String orderStatus, String deliveryMethod, Set<OrderDetailRequest> orderDetails) {
        this.email = email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        if (deliveryMethod == null) {
            this.deliveryMethod = "PICKUP";
        } else {
            this.deliveryMethod = deliveryMethod;
        }
    }
}
