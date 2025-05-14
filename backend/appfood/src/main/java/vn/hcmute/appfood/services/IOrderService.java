package vn.hcmute.appfood.services;

import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.dto.OrderResponse;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.List;

public interface IOrderService {
    Long createOrder(OrderDTO orderDTO);

    List<OrderResponse> getOrdersByUserEmail(String email);

    long countByUserId(Long userId);

    long countByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    boolean cancelOrder(Long orderId);

    boolean confirmOrder(Long orderId);

    boolean deliveredOrder(Long orderId);

    boolean shippingOrder(Long orderId);
}
