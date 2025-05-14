package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        try{
            ReviewResponse dtoReview = reviewService.submitReview(dto, images);
            return ResponseEntity.ok(ApiResponse.success("Submit review successfully", dtoReview));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error("Submit review failed", e));
        }
    }

    // http://localhost:8081/api/reviews/product?foodName=
    @GetMapping("/product")
    public ResponseEntity<?> getReviewProduct(@RequestParam("foodName") @Validated String foodName){
        try{
            ReviewListResponse listResponse = reviewService.getAllReviewByFoodName(foodName);
            return ResponseEntity.ok(ApiResponse.success("Fetched reviews", listResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Submit review failed", e));
        }
    }
}
