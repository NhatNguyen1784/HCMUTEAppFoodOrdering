package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hcmute.appfood.dto.response.ApiResponse;
import vn.hcmute.appfood.dto.ReviewListResponse;
import vn.hcmute.appfood.dto.ReviewRequest;
import vn.hcmute.appfood.dto.ReviewResponse;
import vn.hcmute.appfood.services.Impl.ReviewService;

@RestController
@RequestMapping("/api/reviews") // http://localhost:8081/api/reviews
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // http://localhost:8081/api/reviews/submit
    @PostMapping("/submit")
    public ResponseEntity<?> submitReview(@RequestPart("review") ReviewRequest dto,
                                          @RequestPart(value = "images", required = false) MultipartFile[] images ) {
        dto.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewResponse dtoReview = reviewService.submitReview(dto, images);
        return ResponseEntity.ok(ApiResponse.success("Submit review successfully", dtoReview));
    }

    // http://localhost:8081/api/reviews/product?foodId=
    @GetMapping("/product")
    public ResponseEntity<?> getReviewProduct(@RequestParam("foodId") @Validated Long foodId){
        try{
            ReviewListResponse listResponse = reviewService.getAllReviewByFoodId(foodId);
            return ResponseEntity.ok(ApiResponse.success("Fetched reviews", listResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Submit review failed", e));
        }
    }
}
