package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.CartDTO;
import vn.hcmute.appfood.dto.CartRequest;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.DeleteCartRequest;
import vn.hcmute.appfood.entity.Cart;
import vn.hcmute.appfood.services.Impl.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<?> getCartByUser(@RequestParam("email") String email) {
        try{
            CartDTO cart = cartService.getCartByEmail(email);
            return ResponseEntity.ok(ApiResponse.success("cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Cart not found: " + e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody CartRequest request) {
        try{
            CartDTO cart = cartService.addToCart(request.getEmail(), request.getFoodId(), request.getQuantity());
            return ResponseEntity.ok(ApiResponse.success("Add item to cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while add item to cart: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartRequest request) {
        try {
            CartDTO cart = cartService.updateCartItem(request.getEmail(), request.getFoodId(), request.getQuantity());
            return ResponseEntity.ok(ApiResponse.success("Update cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while update cart: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteItemFromCart(@RequestBody DeleteCartRequest request) {
        try {
            CartDTO cart = cartService.deleteCartItem(request.getEmail(), request.getFoodId());
            return ResponseEntity.ok(ApiResponse.success("Delete cart successfull", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error while delete cart: " + e.getMessage(), null));
        }
    }
}
