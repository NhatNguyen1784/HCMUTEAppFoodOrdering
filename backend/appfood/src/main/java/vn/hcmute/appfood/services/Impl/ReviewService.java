package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hcmute.appfood.dto.ReviewListResponse;
import vn.hcmute.appfood.dto.ReviewRequest;
import vn.hcmute.appfood.dto.ReviewResponse;
import vn.hcmute.appfood.entity.*;
import vn.hcmute.appfood.exception.AccessDeniedException;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.FoodRepository;
import vn.hcmute.appfood.repository.OrderDetailRepository;
import vn.hcmute.appfood.repository.ReviewRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CloudinaryService uploadService;

    // danh gia mon an
    public ReviewResponse submitReview(ReviewRequest reviewRequest, MultipartFile[] images) {
        OrderDetail orderDetail = orderDetailRepository.findById(reviewRequest.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("Can not find order detail with ID: " + reviewRequest.getOrderDetailId()));
        User user = userRepository.findByEmail(reviewRequest.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Can not find user with email: " + reviewRequest.getUserEmail()));

        // kiem tra item co bi update khong
        if(validateFoodModified(orderDetail)){ // neu da update roi thi khong duoc danh gia
            throw new AccessDeniedException("Cannot review product. Food information has been updated.");
        }
        // kiem tra trang thai da nhan hang chua
        Order order = orderDetail.getOrder();
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERED)){
            // neu chua nhan duoc thi khong duoc review
            throw new AccessDeniedException("Cannot review product. Order not delivered yet.");
        }

        // kiem tra item mua da duoc review chua
        if(reviewRepository.existsByOrderDetailAndUser(orderDetail, user)){

            // neu da review roi thi khong duoc review
            throw  new AccessDeniedException("This item has already been reviewed.");
        }

        ProductReview review = new ProductReview();

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setOrderDetail(orderDetail);
        review.setUser(user);

        // xu ly anh upload kem theo (neu co)
        if(images != null && images.length > 0){
            for(MultipartFile image : images){
                String imageUrl = uploadService.uploadImage(image);
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setUrl(imageUrl);
                reviewImage.setReview(review);
                review.getImages().add(reviewImage);
            }

        }

        ProductReview savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    private boolean validateFoodModified(OrderDetail orderDetail) {
        Food food = orderDetail.getFood();
        if(food.getFoodName().equals(orderDetail.getFoodName())){
           return true;
        }
        return false;
    }

    // Lay danh sach danh gia theo ten mon an
    public ReviewListResponse getAllReviewByFoodName(String foodName){
        Food food = foodRepository.findByFoodName(foodName)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find food with name: " + foodName));

        // lay tat ca review cua nguoi dung ve mon an
        List<ProductReview> reviews  = reviewRepository.findByOrderDetail_FoodName(food.getFoodName());

        // chuyen entity thanh DTO de tra ve
        List<ReviewResponse> responses = reviews.stream().map(this::convertToDTO).collect(Collectors.toList());

        // tinh tong so luong danh gia
        long totalReviews = responses.size();

        // tinh rating trung binh
        double avgRating = reviews.stream()
                .mapToInt(ProductReview::getRating)
                .average()
                .orElse(0.0);

        // lam tron den 1 chu so thap phan
        avgRating = Math.round(avgRating * 10.0) / 10.0;

        ReviewListResponse listResponse = new ReviewListResponse();
        listResponse.setTotalReviews(totalReviews);
        listResponse.setAvgRating(avgRating);
        listResponse.setReviews(responses);

        return listResponse;
    }


    // ham convert entity qua DTO
    private ReviewResponse convertToDTO(ProductReview review) {
        ReviewResponse dto = new ReviewResponse();
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setImageUrls(
                review.getImages().stream()
                        .map(ReviewImage::getUrl)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
