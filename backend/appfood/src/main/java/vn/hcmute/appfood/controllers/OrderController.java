package vn.hcmute.appfood.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.*;
import vn.hcmute.appfood.dto.response.ApiResponse;
import vn.hcmute.appfood.dto.response.ResponseObject;
import vn.hcmute.appfood.services.Impl.OrderDetailService;
import vn.hcmute.appfood.services.Impl.OrderService;
import vn.hcmute.appfood.services.Impl.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private PaymentService paymentService;

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
            return ResponseEntity.badRequest().body(ApiResponse.error("Orders retrieved fail", null));
        }
    }

    //Shipping order
    //http://localhost:8081/api/order/1/shipping
    @PutMapping("/{orderId}/shipping")
    public ResponseEntity<?> shippingOrder(@PathVariable Long orderId) {
        boolean check = orderService.shippingOrder(orderId);
        if(check) {
            return ResponseEntity.ok(ApiResponse.success("Order shipping successfully"));
        }
        else{
            return ResponseEntity.badRequest().body(ApiResponse.error("Order shipping failed. The order may not exist or is already cancelled.", null));        }
    }

    //Delivered order
    //http://localhost:8081/api/order/1/delivered
    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<?> deliveredOrder(@PathVariable Long orderId) {
        boolean check = orderService.deliveredOrder(orderId);
        if(check) {
            return ResponseEntity.ok(ApiResponse.success("Order delivered successfully"));
        }
        else{
            return ResponseEntity.badRequest().body(ApiResponse.error("Order delivered failed. The order may not exist or is already cancelled.", null));        }
    }

    //Confirm order
    //http://localhost:8081/api/order/1/confirm
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable Long orderId) {
        boolean check = orderService.confirmOrder(orderId);
        if(check) {
            return ResponseEntity.ok(ApiResponse.success("Order confirm successfully"));
        }
        else{
            return ResponseEntity.badRequest().body(ApiResponse.error("Order confirm failed. The order may not exist or is already cancelled.", null));        }
    }

    //User cancel order
    //http://localhost:8081/api/order/1/cancel
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        boolean check = orderService.cancelOrder(orderId);
        if(check) {
            return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully"));
        }
        else{
            return ResponseEntity.badRequest().body(ApiResponse.error("Order cancellation failed. The order may not exist or is already cancelled.", null));        }
    }

    @GetMapping("/payment/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    @GetMapping("/payment/vn-pay-callback")
    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            PaymentDTO.VNPayResponse res = PaymentDTO.VNPayResponse.builder()
                    .code("00")
                    .message("Success")
                    .paymentUrl("")
                    .build();
            return new ResponseObject<>(HttpStatus.OK, "Success", res);
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }
}
