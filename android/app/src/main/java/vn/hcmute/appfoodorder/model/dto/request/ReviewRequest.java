package vn.hcmute.appfoodorder.model.dto.request;

import java.io.Serializable;

public class ReviewRequest implements Serializable {
    private Long orderDetailId;
    private int rating;
    private String comment;
    private String userEmail;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ReviewRequest() {
    }
}
