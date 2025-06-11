package vn.hcmute.appfood.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDTO {
    private String categoryName;
    private MultipartFile image;
}
