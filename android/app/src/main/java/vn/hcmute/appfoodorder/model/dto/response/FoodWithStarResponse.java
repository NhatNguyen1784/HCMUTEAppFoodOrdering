package vn.hcmute.appfoodorder.model.dto.response;

import java.io.Serializable;
public class FoodWithStarResponse implements Serializable {
    private Long foodId;
    private String foodName;
    private Double foodPrice;
    private Long totalSold;
    private Double avgRating;
    private String foodImageUrls;

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

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Long totalSold) {
        this.totalSold = totalSold;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String getFoodImageUrls() {
        return foodImageUrls;
    }

    public void setFoodImageUrls(String foodImageUrls) {
        this.foodImageUrls = foodImageUrls;
    }

    public FoodWithStarResponse() {
    }
}
