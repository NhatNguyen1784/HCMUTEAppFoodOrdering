package vn.hcmute.appfoodorder.model.dto.request;

public class OrderDetailRequest {
    private String foodName;
    private Double unitPrice;
    private Integer quantity;
    private String foodImage;
    public OrderDetailRequest(String foodName, Double unitPrice, Integer quantity, String foodImage) {
        this.foodName = foodName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.foodImage = foodImage;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
