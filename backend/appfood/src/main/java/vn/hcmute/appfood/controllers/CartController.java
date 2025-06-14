package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.CartDTO;
import vn.hcmute.appfood.dto.CartRequest;
import vn.hcmute.appfood.dto.response.ApiResponse;
import vn.hcmute.appfood.dto.DeleteCartRequest;
import vn.hcmute.appfood.services.Impl.CartService;

@RestController
@RequestMapping("/api/cart") // http://localhost:8081/api/cart
public class CartController {
    @Autowired
    private CartService cartService;

    // http://localhost:8081/api/cart/get
    @GetMapping("/get")
    public ResponseEntity<?> getCartByUser() {
        try{
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            CartDTO cart = cartService.getCartByEmail(email);
            return ResponseEntity.ok(ApiResponse.success("cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Cart not found: " + e.getMessage(), null));
        }
    }

    // http://localhost:8081/api/cart/add
    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CartRequest request) {
        try{
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            CartDTO cart = cartService.addToCart(email, request.getFoodId(), request.getQuantity());
            return ResponseEntity.ok(ApiResponse.success("Add item to cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while add item to cart: " + e.getMessage(), null));
        }
    }

    // http://localhost:8081/api/cart/update
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            CartDTO cart = cartService.updateCartItem(email, request.getFoodId(), request.getQuantity());
            return ResponseEntity.ok(ApiResponse.success("Update cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while update cart: " + e.getMessage(), null));
        }
    }

    // http://localhost:8081/api/cart/delete
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteItemFromCart(@RequestBody DeleteCartRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            CartDTO cart = cartService.deleteCartItem(email, request.getFoodId());
            return ResponseEntity.ok(ApiResponse.success("Delete cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while delete cart: " + e.getMessage(), null));
        }
    }
}
