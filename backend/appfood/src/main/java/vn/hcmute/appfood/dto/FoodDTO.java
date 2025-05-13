package vn.hcmute.appfood.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FoodDTO {
    @NotBlank(message = "Tên thực phẩm không được để trống")
    @Size(min = 2, max = 100, message = "Tên thực phẩm phải từ 2-100 ký tự")
    private String foodName;

    @NotNull(message = "Giá thực phẩm không được để trống")
    @Positive(message = "Giá thực phẩm phải là số dương")
    @DecimalMax(value = "1000000", message = "Giá thực phẩm không được vượt quá 1,000,000")
    private Double foodPrice;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String foodDescription;

    @NotNull(message = "Danh mục không được để trống")
    @Positive(message = "ID danh mục phải là số dương")
    private Long categoryId;

    @Size(max = 5, message = "Tối đa 5 ảnh được phép tải lên")
    private List<MultipartFile> foodImage;
}
