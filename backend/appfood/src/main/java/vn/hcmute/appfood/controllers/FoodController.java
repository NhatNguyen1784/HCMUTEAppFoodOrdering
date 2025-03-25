package vn.hcmute.appfood.controllers;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.FoodDTO;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.services.Impl.FoodImageService;
import vn.hcmute.appfood.services.Impl.FoodService;

import java.util.Optional;

@RestController
@RequestMapping("/api/foods/")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodImageService foodImageService;

    @PostMapping("add")
    public ResponseEntity<?> addFood(@ModelAttribute FoodDTO foodDTO){
        Optional<Food> food = foodService.findByName(foodDTO.getFoodName());
        if(food.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("food is already exist");
        }
        else {
            return new ResponseEntity<>(new ApiResponse(200, "add food thanh cong", foodService.saveFood(foodDTO)), HttpStatus.OK);
        }
    }
    
}
