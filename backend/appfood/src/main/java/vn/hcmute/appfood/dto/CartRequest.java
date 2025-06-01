package vn.hcmute.appfood.dto;

import lombok.Data;

@Data
public class CartRequest {
    private Long foodId;
    private int quantity;
}
