package vn.hcmute.appfood.services;

import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.entity.Order;

import java.util.List;

public interface IOrderService {
    Long createOrder(OrderDTO orderDTO);

    List<Order> getAllOrdersByUserId(String email);
}
