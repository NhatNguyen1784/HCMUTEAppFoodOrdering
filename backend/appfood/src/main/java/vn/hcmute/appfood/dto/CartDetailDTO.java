package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDetailDTO {
    private Long foodId;
    private String foodName;
    private int quantity;
    private double unitPrice;
    private double price;
    private List<FoodImageDTO> foodImage;
}
