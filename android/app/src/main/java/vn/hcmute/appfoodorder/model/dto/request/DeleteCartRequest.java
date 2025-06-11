package vn.hcmute.appfoodorder.model.dto.request;

public class DeleteCartRequest {
    private Long foodId;

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }
}
