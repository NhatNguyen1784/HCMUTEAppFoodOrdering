package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.entity.OrderDetail;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.OrderRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.services.IOrderService;
import vn.hcmute.appfood.utils.DeliveryMethod;
import vn.hcmute.appfood.utils.OrderStatus;
import vn.hcmute.appfood.utils.PaymentOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    //Create order
    @Override
    public Long createOrder(OrderDTO orderDTO) {
        User user = userRepository.findByEmail(orderDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        long countPenOrder = countByUserIdAndOrderStatus(user.getId(), OrderStatus.PENDING);
        long countShipOrder = countByUserIdAndOrderStatus(user.getId(), OrderStatus.SHIPPING);
        long totalOrderWaiting = countShipOrder + countPenOrder;
        if(totalOrderWaiting >= 2){
            return null;
        }
        else{
            Order order = new Order();
            try {
                order.setOrderStatus(OrderStatus.valueOf(orderDTO.getOrderStatus().toUpperCase()));
                order.setPaymentOption(PaymentOption.valueOf(orderDTO.getPaymentOption().toUpperCase()));
                order.setDeliveryMethod(DeliveryMethod.valueOf(orderDTO.getDeliveryMethod().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid order status, payment option, or delivery method");
            }
            order.setFullAddress(orderDTO.getFullAddress());

            // Chuyển đổi OrderDTO thành OrderDetail
            Set<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream().map(dto -> {
                OrderDetail detail = new OrderDetail();
                detail.setFoodName(dto.getFoodName());
                detail.setUnitPrice(dto.getUnitPrice());
                detail.setQuantity(dto.getQuantity());
                detail.setPrice(dto.getUnitPrice() * dto.getQuantity());
                detail.setFoodImage(dto.getFoodImage());
                detail.setOrder(order);
                return detail;
            }).collect(Collectors.toSet());
            order.setOrderDetailSet(orderDetails);
            order.setUser(user);

            // Tính phí giao hàng
            double deliveryFee = switch (order.getDeliveryMethod()) {
                case FAST -> 20000.0;
                case STANDARD -> 10000.0;
                case PICKUP -> 0.0;
                default -> 0.0;
            };

            // Tính tổng giá trị đơn hàng (cộng với phí giao hàng)
            double itemsTotal = orderDetails.stream()
                    .mapToDouble(OrderDetail::getPrice)
                    .sum();
            double taxFee = itemsTotal * 0.1; //Tax
            order.setTotalPrice(itemsTotal + taxFee + deliveryFee);

            // Tính tổng số lượng đơn hàng
            int totalQuantity = orderDetails.stream()
                    .mapToInt(OrderDetail::getQuantity)
                    .sum();
            order.setTotalQuantity(totalQuantity);

            // Lưu đơn hàng và trả về id của đơn hàng đó
            Order savedOrder = orderRepository.save(order);
            return savedOrder.getId();
        }
    }


    //List order by user
    @Override
    public List<Order> getAllOrdersByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orders;
    }

    //Count order by user id
    long countByUserId(Long userId) {
        Long count = orderRepository.countByUserId(userId);
        return count != null ? count : 0;// Nếu count == null thì trả về 0
    }

    long countByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus) {
        Long count = orderRepository.countByUserIdAndOrderStatus(userId, orderStatus);
        return count != null ? count : 0;  // Nếu count == null thì trả về 0
    }
}
