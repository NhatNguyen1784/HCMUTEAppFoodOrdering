package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.OrderDetailDTO;
import vn.hcmute.appfood.dto.OrderDetailResponseDTO;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.entity.OrderDetail;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.OrderDetailRepository;
import vn.hcmute.appfood.repository.OrderRepository;
import vn.hcmute.appfood.services.IOrderDetailService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderDetailService implements IOrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDetailResponseDTO findByOrderId(Long orderId) {
        try {
            OrderDetailResponseDTO orders = new OrderDetailResponseDTO();

            Optional<Order> order = orderRepository.findById(orderId);
            if(order.isEmpty()){
                return orders;
            }
            User user = order.get().getUser();
            Set<OrderDetail> orderDetail = orderDetailRepository.findByOrderId(orderId);
            if(orderDetail.isEmpty()){
                throw new ResourceNotFoundException("Order not found with id: " + orderId);
            }
            Set<OrderDetailDTO> orderDetailDTO = orderDetail.stream().map(o ->
            {
                OrderDetailDTO dto = new OrderDetailDTO();
                dto.setId(o.getId());
                dto.setFoodImage(o.getFoodImage());
                dto.setFoodName(o.getFoodName());
                dto.setUnitPrice(o.getUnitPrice());
                dto.setQuantity(o.getQuantity());
                dto.setIsReview(o.getIsReview());
                return dto;
            }).collect(Collectors.toSet());

            orders.setOrderDetails(orderDetailDTO);
            orders.setOrderStatus(order.get().getOrderStatus().toString());
            orders.setPaymentOption(order.get().getPaymentOption().toString());
            orders.setFullAddress(order.get().getFullAddress());
            orders.setDeliveryMethod(order.get().getDeliveryMethod().toString());
            orders.setUserName(user.getFullName());
            orders.setPhoneNumber(user.getPhone());
            orders.setTotalPrice(order.get().getTotalPrice());

            LocalDateTime createdDate = order.get().getCreatedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
            String formattedDate = createdDate.format(formatter);
            orders.setCreatedDate(formattedDate);
            return orders;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
