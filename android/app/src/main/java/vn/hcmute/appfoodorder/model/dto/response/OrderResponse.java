package vn.hcmute.appfoodorder.model.dto.response;

import java.io.Serializable;

public class OrderResponse implements Serializable {
    private Long orderId;
    private int totalQuantity;
    private String createdDate;
    private Double totalPrice;
    private String orderStatus;

    public OrderResponse(Long orderId, int totalQuantity, String createdDate, Double totalPrice, String orderStatus) {
        this.orderId = orderId;
        this.totalQuantity = totalQuantity;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public OrderResponse() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
