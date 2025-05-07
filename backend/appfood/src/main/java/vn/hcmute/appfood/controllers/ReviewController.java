package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.ReviewDTO;
import vn.hcmute.appfood.services.Impl.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews") // http://localhost:8081/api/reviews
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // http://localhost:8081/api/reviews/submit
    @PostMapping("/submit")
    public ResponseEntity<?> submitReview(@RequestBody ReviewDTO dto){
        try{
            ReviewDTO dtoReview = reviewService.submitReview(dto);
            return ResponseEntity.ok(ApiResponse.success("Submit review successfully", dtoReview));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(ApiResponse.error("Submit review failed", e));
        }
    }

    // http://localhost:8081/api/reviews/product?foodName=
    @GetMapping("/product")
    public ResponseEntity<?> getReviewProduct(@RequestParam("foodName") @Validated String foodName){
        try{
            List<ReviewDTO> reviewDTOS = reviewService.getAllReviewByFoodName(foodName);
            return ResponseEntity.ok(ApiResponse.success("Fetched reviews", reviewDTOS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Submit review failed", e));
        }
    }
}
