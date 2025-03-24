package vn.hcmute.appfood.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FoodDTO {
    private String foodName;
    private Double foodPrice;
    private String foodDescription;
    private Long categoryId;
    private List<MultipartFile> foodImage; // anh upload tu client
}
