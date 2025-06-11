package vn.hcmute.appfoodorder.model.entity;

import java.io.Serializable;
import java.util.List;

public class Food implements Serializable {
    private Long id;
    private String foodName;
    private String foodDescription;
    private Double foodPrice;
    // danh sách ảnh
    private List<FoodImage> foodImages;

    public String getFirstImageUrl(){
        if(foodImages != null && !foodImages.isEmpty()){
            return foodImages.get(0).getImageUrl();
        }
        return null;
    }

    public Food() {
    }

    public Food(Long id, String foodName, String foodDescription, Double foodPrice, List<FoodImage> foodImages) {
        this.id = id;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodImages = foodImages;
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

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public List<FoodImage> getFoodImages() {
        return foodImages;
    }

    public void setFoodImages(List<FoodImage> foodImages) {
        this.foodImages = foodImages;
    }
}
