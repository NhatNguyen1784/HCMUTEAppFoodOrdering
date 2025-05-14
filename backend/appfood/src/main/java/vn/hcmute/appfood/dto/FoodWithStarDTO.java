package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodWithStarDTO {
    private Long foodId;
    private String foodName;
    private Double foodPrice;
    private Long totalSold;
    private Double avgRating;
    private String foodImageUrls;
}
