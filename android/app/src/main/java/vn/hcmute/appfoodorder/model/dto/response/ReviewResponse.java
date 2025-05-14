package vn.hcmute.appfoodorder.model.dto.response;

import java.io.Serializable;
import java.util.List;

public class ReviewResponse implements Serializable {
    private int rating;
    private String comment;
    private String createdAt; // Vì LocalDateTime sẽ được map thành String khi nhận ở Android
    private String userEmail;
    private List<String> imageUrls;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ReviewResponse() {
    }
}
