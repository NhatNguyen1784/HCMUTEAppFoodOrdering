package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.FoodDTO;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.services.Impl.FoodService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    @Autowired
    private FoodService foodService;

    // lay danh sach tat ca food
    @GetMapping
    public ResponseEntity<?> getAllFoods() {
        return new ResponseEntity<>(new ApiResponse(200, "List All Foods", foodService.findAll()), HttpStatus.OK);
    }

    //lay food theo food_id
    @GetMapping("/{foodId}")
    public ResponseEntity<?> getFoodById(@PathVariable Long foodId) {
        return new ResponseEntity<>(new ApiResponse(200, "Food item", foodService.findById(foodId)), HttpStatus.OK);
    }

    // lay danh sach food theo category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getFoodByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(new ApiResponse(200, "List Foods", foodService.findByCategoryId(categoryId)), HttpStatus.OK);
    }

    // add new food
    @PostMapping("/add")
    public ResponseEntity<?> addFood(@ModelAttribute FoodDTO foodDTO){
        Optional<Food> food = foodService.findByName(foodDTO.getFoodName());
        if(food.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Food is already exist");
        }
        else {
            return new ResponseEntity<>(new ApiResponse(200, "add food successfull", foodService.saveFood(foodDTO)), HttpStatus.OK);
        }
    }

    // update food
    @PutMapping("/update/{foodId}")
    public ResponseEntity<?> updateFood(@Validated @ModelAttribute FoodDTO foodDTO, @Validated @PathVariable Long foodId){
        Optional<Food> food = foodService.findById(foodId);
        if(food.isPresent()){
            return new ResponseEntity<>(new ApiResponse(200, "Update food successfull!", foodService.updateFood(foodId, foodDTO)), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("foodId not found");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFoodsByName(@RequestParam String keyword) {
        try{
            List<Food> foods = foodService.searchFoodsByName(keyword);
            return ResponseEntity.ok(ApiResponse.success("Search food by name", foods));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
