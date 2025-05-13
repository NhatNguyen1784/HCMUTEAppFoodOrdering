package vn.hcmute.appfoodorder.model.dto.response;

public class OrderDetailDTO {
    private Long id;
    private String foodName;
    private Double unitPrice;
    private Integer quantity;
    private String foodImage;

    public OrderDetailDTO(Long id, String foodName, Double unitPrice, Integer quantity, String foodImage) {
        this.id = id;
        this.foodName = foodName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.foodImage = foodImage;
    }

    public OrderDetailDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
