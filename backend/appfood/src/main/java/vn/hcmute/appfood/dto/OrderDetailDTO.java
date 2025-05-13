package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private Long id;
    private String foodName;
    private Double unitPrice;
    private Integer quantity;
    private String foodImage;
    private Long foodId;
}
