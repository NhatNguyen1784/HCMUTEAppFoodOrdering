package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String email;
    private String fullAddress;
    private String paymentOption;
    private String orderStatus;
    private String deliveryMethod;
    private Set<OrderDetailDTO> orderDetails;
}
