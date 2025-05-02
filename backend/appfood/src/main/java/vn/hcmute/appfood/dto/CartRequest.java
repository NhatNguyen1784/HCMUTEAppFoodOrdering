package vn.hcmute.appfood.dto;

import lombok.Data;

@Data
public class CartRequest {
    private String email;
    private Long foodId;
    private int quantity;
}
