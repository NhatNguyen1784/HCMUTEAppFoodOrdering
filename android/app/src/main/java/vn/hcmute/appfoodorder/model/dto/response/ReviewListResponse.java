package vn.hcmute.appfoodorder.model.dto.response;

import java.io.Serializable;
import java.util.List;

public class ReviewListResponse implements Serializable {
    private long totalReviews;
    private double avgRating;
    private List<ReviewResponse> reviews;

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public ReviewListResponse() {
    }
}
