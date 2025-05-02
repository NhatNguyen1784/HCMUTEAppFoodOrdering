package vn.hcmute.appfoodorder.model.dto.request;

public class DeleteCartRequest {
    private String email;
    private Long foodId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }
}
