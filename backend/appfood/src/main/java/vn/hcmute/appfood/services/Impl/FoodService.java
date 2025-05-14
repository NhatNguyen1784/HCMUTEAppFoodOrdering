package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.FoodDTO;
import vn.hcmute.appfood.dto.FoodWithStarDTO;
import vn.hcmute.appfood.entity.Category;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.entity.FoodImage;
import vn.hcmute.appfood.entity.ProductReview;
import vn.hcmute.appfood.repository.CategoryRepository;
import vn.hcmute.appfood.repository.FoodRepository;
import vn.hcmute.appfood.repository.OrderDetailRepository;
import vn.hcmute.appfood.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FoodImageService foodImageService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // lay ra tat ca mon an
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    // lay theo foodId
    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    // lay theo ten
    public Optional<Food> findByName(String name) {
        return foodRepository.findByFoodName(name);
    }

    // lay so luong ban duoc dua vao food ID trong OrderDetail
    public Long getTotalSoldByFoodId(Long foodId) {
        if (foodId == null) {
            throw new IllegalArgumentException("Food ID không được để trống");
        }
        // Đếm số lượng đánh giá từ các đơn hàng có trạng thái SUCCESSFUL
        Long totalSold = orderDetailRepository.countReviewsByFoodId(foodId);

        // Nếu không có kết quả (món ăn chưa được bán), trả về 0
        return totalSold != null ? totalSold : 0L;
    }

    // tim theo category
    public List<FoodWithStarDTO> findByCategoryId(Long categoryId) {
        List<Food> foods = foodRepository.findByCategoryId(categoryId);
        List<FoodWithStarDTO> foodWithStarDTOs = new ArrayList<FoodWithStarDTO>();

        for (Food food : foods) {
            // tinh tong so luong ban duoc cua tung food
            Long totalSold = getTotalSoldByFoodId(food.getId());

            // tinh avgRating cho tung food
            List<ProductReview> reviews = reviewRepository.findByOrderDetail_Food(food.getId());
            double avgRating = reviewService.calculateAverageRating(reviews);

            // Chỉ lấy hình ảnh đầu tiên của food (nếu có)
            String firstImageUrl = null;
            if (food.getFoodImages() != null && !food.getFoodImages().isEmpty()) {
                firstImageUrl = food.getFoodImages().get(0).getImageUrl();
            }

            // gan data cho DTO de tra ve
            FoodWithStarDTO dto = new FoodWithStarDTO();
            dto.setFoodId(food.getId());
            dto.setFoodName(food.getFoodName());
            dto.setTotalSold(totalSold);
            dto.setAvgRating(avgRating);
            dto.setFoodPrice(food.getFoodPrice());
            dto.setFoodImageUrls(firstImageUrl);

            foodWithStarDTOs.add(dto); // Thêm DTO vào danh sách kết quả
        }
        return foodWithStarDTOs;
    }

    // create food
    public Food saveFood(FoodDTO foodDTO) {
        Food food = new Food();
        food.setFoodName(foodDTO.getFoodName());
        food.setFoodDescription(foodDTO.getFoodDescription());
        food.setFoodPrice(foodDTO.getFoodPrice());
        food.setCategory(new Category(foodDTO.getCategoryId()));

        // Neu khong co anh thi se luu
        Food savedFood = foodRepository.save(food);

        // neu co anh thi upload len cloud
        if(foodDTO.getFoodImage() != null && !foodDTO.getFoodImage().isEmpty()) {
            foodImageService.uploadImagesFromDTO(food, foodDTO);
        }

        return savedFood;
    }

    // update food co the upload anh them
    public Food updateFood(Long id, FoodDTO foodDTO) {
        Food food = foodRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Food ID khong ton tai: " + id));
        food.setFoodName(foodDTO.getFoodName());
        food.setFoodDescription(foodDTO.getFoodDescription());
        food.setFoodPrice(foodDTO.getFoodPrice());
        food.setCategory(new Category(foodDTO.getCategoryId()));

        // neu co anh thi upload len cloud
        if(foodDTO.getFoodImage() != null && !foodDTO.getFoodImage().isEmpty()) {
            foodImageService.uploadImagesFromDTO(food, foodDTO);
        }

        return foodRepository.save(food);
    }

    // delete food
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id).orElse(null);
        if(food != null) {
            // xoa anh cua food
            foodImageService.deleteAllImagesByFood(food);

            // xoa food trong db
            foodRepository.delete(food);
        }
    }

    //Search by name
    public List<Food> searchFoodsByName(String keyword) {
        return foodRepository.findByFoodNameContainingIgnoreCase(keyword);
    }


}
