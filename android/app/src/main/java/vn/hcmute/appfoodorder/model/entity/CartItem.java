package vn.hcmute.appfoodorder.model.entity;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CartItem implements Serializable {
    private Long foodId;
    private String foodName;
    private int quantity;
    private double unitPrice;
    private double price;
    private List<FoodImage> foodImage;

    public String getFirstImageUrl(){
        if(foodImage != null && !foodImage.isEmpty()){
            return foodImage.get(0).getImageUrl();
        }
        return null;
    }

    public List<FoodImage> getFoodImages() {
        return foodImage;
    }

    public void setFoodImages(List<FoodImage> foodImages) {
        this.foodImage = foodImages;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CartItem() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, foodName, quantity, unitPrice);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity &&
                foodId.equals(cartItem.foodId) &&
                foodName.equals(cartItem.foodName) &&
                unitPrice == cartItem.unitPrice;
    }

    public CartItem(Long foodId, String foodName, int quantity, double unitPrice, double price, List<FoodImage> foodImages) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.price = price;
        this.foodImage = foodImages;
    }
}
