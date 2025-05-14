package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.entity.*;
import vn.hcmute.appfood.dto.OrderResponse;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.entity.OrderDetail;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.CartRepository;
import vn.hcmute.appfood.repository.FoodRepository;
import vn.hcmute.appfood.repository.OrderRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.services.IOrderService;
import vn.hcmute.appfood.utils.DeliveryMethod;
import vn.hcmute.appfood.utils.OrderStatus;
import vn.hcmute.appfood.utils.PaymentOption;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CartRepository cartRepository;

    //Create order
    @Override
    public Long createOrder(OrderDTO orderDTO) {
        User user = userRepository.findByEmail(orderDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with User: " + user.getEmail()));
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
                Food food = foodRepository.findById(dto.getFoodId()).get();
                detail.setFood(food);
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
            cartRepository.delete(cart);
            return savedOrder.getId();
        }
    }

    //List order by user
    @Override
    public List<OrderResponse> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        if(orders.isEmpty()){
            return new ArrayList<>();
        }
        List<OrderResponse> orderResponse = orders.stream().map(list -> {
            OrderResponse response = new OrderResponse();
            response.setOrderId(list.getId());
            response.setTotalPrice(list.getTotalPrice());
            response.setTotalQuantity(list.getTotalQuantity());
            response.setOrderStatus(list.getOrderStatus().toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            response.setCreatedDate(list.getCreatedDate().format(formatter));
            return response;
        }).collect(Collectors.toList());
        return orderResponse;
    }

    //Count order by user id
    @Override
    public long countByUserId(Long userId) {
        Long count = orderRepository.countByUserId(userId);
        return count != null ? count : 0;// Nếu count == null thì trả về 0
    }

    @Override
    public long countByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus) {
        Long count = orderRepository.countByUserIdAndOrderStatus(userId, orderStatus);
        return count != null ? count : 0;  // Nếu count == null thì trả về 0
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return false; // Đơn hàng không tồn tại
        }
        Order order = orderOptional.get();
        if(order.getOrderStatus().equals(OrderStatus.CANCELLED) || !order.getOrderStatus().equals(OrderStatus.PENDING)){
            return false;//Da o trang thai Cancel roi
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return true;
    }

    @Override
    public boolean confirmOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return false; // Đơn hàng không tồn tại
        }
        Order order = orderOptional.get();
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERED)){
            return false;
        }
        order.setOrderStatus(OrderStatus.SUCCESSFUL);
        orderRepository.save(order);
        return true;
    }

    @Override
    public boolean deliveredOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return false; // Đơn hàng không tồn tại
        }
        Order order = orderOptional.get();
        if(!order.getOrderStatus().equals(OrderStatus.SHIPPING)){
            return false;
        }
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return true;
    }

    @Override
    public boolean shippingOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return false; // Đơn hàng không tồn tại
        }
        Order order = orderOptional.get();
        if(!order.getOrderStatus().equals(OrderStatus.PENDING)){
            return false;
        }
        order.setOrderStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);
        return true;
    }
}
