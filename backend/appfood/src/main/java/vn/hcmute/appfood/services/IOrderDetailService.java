package vn.hcmute.appfood.services;

import vn.hcmute.appfood.dto.OrderDetailResponseDTO;

public interface IOrderDetailService {
    OrderDetailResponseDTO findByOrderId(Long orderId);
}
