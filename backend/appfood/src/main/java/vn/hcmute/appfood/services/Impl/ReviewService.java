package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.ReviewDTO;
import vn.hcmute.appfood.entity.*;
import vn.hcmute.appfood.exception.AccessDeniedException;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.FoodRepository;
import vn.hcmute.appfood.repository.OrderDetailRepository;
import vn.hcmute.appfood.repository.ReviewRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.ArrayList;
import java.util.List;

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

    // danh gia mon an
    public ReviewDTO submitReview(ReviewDTO reviewDTO) {
        OrderDetail orderDetail = orderDetailRepository.findById(reviewDTO.getOrderDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("Can not find order detail with ID: " + reviewDTO.getOrderDetailId()));
        User user = userRepository.findByEmail(reviewDTO.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Can not find user with email: " + reviewDTO.getUserEmail()));

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

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setOrderDetail(orderDetail);

        ProductReview savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    // Lay danh sach danh gia theo ten mon an
    public List<ReviewDTO> getAllReviewByFoodName(String foodName){
        Food food = foodRepository.findByFoodName(foodName)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find food with name: " + foodName));
        List<ProductReview> reviews  = reviewRepository.findByOrderDetail_FoodName(food.getFoodName());
        List<ReviewDTO> dtos = new ArrayList<ReviewDTO>();
        for(ProductReview review : reviews){
            dtos.add(convertToDTO(review));
        }
        return dtos;
    }

    // ham convert entity qua DTO
    private ReviewDTO convertToDTO(ProductReview review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setOrderDetailId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setOrderDetailId(review.getOrderDetail().getId());
        return dto;
    }
}
