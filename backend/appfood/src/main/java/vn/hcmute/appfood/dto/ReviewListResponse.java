package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListResponse {
    private Long totalReviews;
    private Double avgRating;
    private List<ReviewResponse> reviews;
}
