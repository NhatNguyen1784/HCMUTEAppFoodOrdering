package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.OrderDTO;
import vn.hcmute.appfood.services.Impl.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

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
}
