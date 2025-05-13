package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.dto.OrderDetailResponseDTO;
import vn.hcmute.appfood.dto.OrderResponse;
import vn.hcmute.appfood.services.Impl.OrderDetailService;
import vn.hcmute.appfood.services.Impl.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    //Create order (1 user max 2 order Pending + Shipping)
    //http://localhost:8081/api/order/create-order
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try{
            Long orderId = orderService.createOrder(orderDTO);
            if(orderId != null) {
                return ResponseEntity.ok(ApiResponse.success("Order created successfully", orderId));
            }
            else
                return ResponseEntity.ok(ApiResponse.success("Order creation failed. You already have a pending order and cannot place another one."));
        }
        catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Order created fail", null));
        }
    }

    //Get order detail by order id
    //http://localhost:8081/api/order/1/details
    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try{
            OrderDetailResponseDTO o = orderDetailService.findByOrderId(orderId);
            if(o.getTotalPrice() != null && o.getOrderDetails() != null) {
                return ResponseEntity.ok(ApiResponse.success("Order details retrieved successfully", o));
            }
            else{
                return ResponseEntity.ok(ApiResponse.error("Order details retrieved fail", null));
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Get order details failed", null));
        }
    }

    //Get all order by email
    //http://localhost:8081/api/order?email=nd2004lk13@gmail.com
    @GetMapping
    public ResponseEntity<?> getOrdersByUserEmail(@RequestParam String email) {
        List<OrderResponse> order = orderService.getOrdersByUserEmail(email);
        if(order != null) {
            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", order));
        }
        else{
            return ResponseEntity.ok(ApiResponse.error("Orders retrieved fail", null));
        }
    }
}
