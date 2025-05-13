package vn.hcmute.appfood.services;

import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.List;

public interface IOrderService {
    Long createOrder(OrderDTO orderDTO);

    List<Order> getAllOrdersByUserId(String email);

    long countByUserId(Long userId);

    long countByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
