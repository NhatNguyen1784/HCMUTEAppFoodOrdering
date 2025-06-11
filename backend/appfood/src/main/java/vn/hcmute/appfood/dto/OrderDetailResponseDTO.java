package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {
    private Set<OrderDetailDTO> orderDetails;
    private String userName;
    private String phoneNumber;
    private String fullAddress;
    private String paymentOption;
    private String orderStatus;
    private String deliveryMethod;
    private Double totalPrice;
    private String createdDate;
}
