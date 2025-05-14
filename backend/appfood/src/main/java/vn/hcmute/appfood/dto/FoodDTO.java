package vn.hcmute.appfood.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FoodDTO {

    private String foodName;

    private Double foodPrice;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String foodDescription;

    private Long categoryId;

    private List<MultipartFile> foodImage;
}
