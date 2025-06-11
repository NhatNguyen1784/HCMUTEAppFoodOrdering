package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {
    private Long id;
    private String email;
    private Double totalPrice;
    private Set<CartDetailDTO> cartDetails;
}
