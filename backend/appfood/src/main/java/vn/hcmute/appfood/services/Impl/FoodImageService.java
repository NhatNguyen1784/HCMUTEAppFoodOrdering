package vn.hcmute.appfood.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hcmute.appfood.dto.FoodDTO;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.entity.FoodImage;
import vn.hcmute.appfood.repository.FoodImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodImageService {

    @Autowired
    private FoodImageRepository foodImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<FoodImage> getImagesByFood(Food food) {
        return foodImageRepository.findByFoodId(food.getId());
    }

    public void uploadImagesFromDTO(Food food, FoodDTO foodDTO) {
        // kiem tra anh
        if(foodDTO.getFoodImage() == null || foodDTO.getFoodImage().isEmpty()) {
            return;
        }

        for (MultipartFile file : foodDTO.getFoodImage()) {
            String imgUrl = cloudinaryService.uploadImage(file);
            FoodImage foodImage = new FoodImage();
            foodImage.setImageUrl(imgUrl);
            foodImage.setFood(food);
            foodImageRepository.save(foodImage);
        }

    }

    // xoa tat ca cac anh cua food
    public void deleteAllImagesByFood(Food food) {
        List<FoodImage> foodImages = foodImageRepository.findByFoodId(food.getId());
        for (FoodImage foodImage : foodImages) {

            // xoa tren cloud
            cloudinaryService.deleteImage(foodImage.getImageUrl());

            // xoa trong db
            foodImageRepository.delete(foodImage);
        }
    }

    // xoa bang id cua anh trong db
    public void deleteImageById(Long id){
        FoodImage image = foodImageRepository.findById(id).orElse(null);
        if(image != null){
            cloudinaryService.deleteImage(image.getImageUrl()); // xoa tren cloud
            foodImageRepository.delete(image); // xoa trong db
        }
        else {
            throw new RuntimeException("Khong tim thay anh voi ID: " + id);
        }
    }
}
