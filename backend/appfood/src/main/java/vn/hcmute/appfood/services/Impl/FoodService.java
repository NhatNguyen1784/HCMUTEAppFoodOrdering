package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.FoodDTO;
import vn.hcmute.appfood.entity.Category;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.repository.CategoryRepository;
import vn.hcmute.appfood.repository.FoodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodImageService foodImageService;

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

    // tim theo category
    public List<Food> findByCategoryId(Long categoryId) {
        return foodRepository.findByCategoryId(categoryId);
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
