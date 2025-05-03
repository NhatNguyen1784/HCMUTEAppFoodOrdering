package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.CartDTO;
import vn.hcmute.appfood.dto.CartDetailDTO;
import vn.hcmute.appfood.dto.FoodImageDTO;
import vn.hcmute.appfood.entity.Cart;
import vn.hcmute.appfood.entity.CartDetail;
import vn.hcmute.appfood.entity.Food;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.repository.CartRepository;
import vn.hcmute.appfood.repository.UserRepository;

import javax.lang.model.element.NestingKind;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private CartRepository cartRepository;

    public CartDTO addToCart(String email, Long foodId, int quantity){
        // kiem tra thong tin user
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // kiem tra thong tin cua food
        Food food = foodService.findById(foodId).orElseThrow(() -> new RuntimeException("Food not found"));

        // lay gio hang cua user, neu chua co thi tao moi
        Cart cart = getOrCreateCartByUser(user);

        // neu gio hang da co roi thi kiem tra item da co chua?
        Optional<CartDetail> existItem = cart.getCartDetails()
                .stream().filter(cartDetail -> cartDetail.getFood().getId().equals(foodId)).findFirst();

        // neu item da ton tai thi cap nhat so luong va gia theo so luong, khong thi tao moi
        if(existItem.isPresent()){
            CartDetail detail = existItem.get();
            detail.setQuantity(detail.getQuantity() + quantity);
            detail.setPrice(detail.getQuantity() * food.getFoodPrice());
        }
        else{
            CartDetail detail = new CartDetail();
            detail.setFood(food);
            detail.setQuantity(quantity);
            detail.setUnitPrice(food.getFoodPrice());
            detail.setPrice(quantity * food.getFoodPrice());
            detail.setCart(cart);
            cart.getCartDetails().add(detail); // them item vao gio hang
        }

        // tinh tong gia tri cho gio hang
        updateCartTotal(cart);

        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }

    public CartDTO updateCartItem(String email, Long foodId, int newQuantity){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // tim item trong list cart detail
        CartDetail cartDetail = cart.getCartDetails().stream()
                .filter(detail -> detail.getFood().getId().equals(foodId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        if(newQuantity <= 0) {
            // neu so luong = 0 thi xoa luon
            cart.getCartDetails().remove(cartDetail);
        }
        else{
            // cap nhat so luong va gia theo so luong
            cartDetail.setQuantity(newQuantity);
            cartDetail.setPrice(cartDetail.getUnitPrice() * newQuantity);
        }
        // cap nhat lai tong gia tri gio hang
        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }

    public CartDTO deleteCartItem(String email, Long foodId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

        // tim item trong list cart detail
        CartDetail cartDetail = cart.getCartDetails().stream()
                .filter(detail -> detail.getFood().getId().equals(foodId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cart.getCartDetails().remove(cartDetail);

        // cap nhat lai tong gia tri gio hang
        updateCartTotal(cart);

        Cart savedCart = cartRepository.save(cart);
        return convertToDTO(savedCart);
    }

    public CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setEmail(cart.getUser().getEmail());
        dto.setTotalPrice(cart.getTotalPrice());

        Set<CartDetailDTO> detailDTOs = cart.getCartDetails().stream().map(detail -> {
            CartDetailDTO d = new CartDetailDTO();
            d.setFoodId(detail.getFood().getId());
            d.setFoodName(detail.getFood().getFoodName());
            d.setQuantity(detail.getQuantity());
            d.setUnitPrice(detail.getUnitPrice());
            d.setPrice(detail.getPrice());
            // Mapping danh sách hình ảnh từ Food -> FoodImageDTO
            List<FoodImageDTO> imageDTOs = detail.getFood().getFoodImages().stream()
                    .map(img -> new FoodImageDTO(img.getId(), img.getImageUrl()))
                    .collect(Collectors.toList());
            d.setFoodImage(imageDTOs);
            return d;
        }).collect(Collectors.toSet());

        dto.setCartDetails(detailDTOs);
        return dto;
    }

    public Cart getOrCreateCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartDetails(new HashSet<>());
            newCart.setTotalPrice(0.0);
            return newCart;
        });
    }

    public CartDTO getCartByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart =  cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        return convertToDTO(cart);
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getCartDetails().stream().mapToDouble(CartDetail::getPrice).sum();
        cart.setTotalPrice(total);
    }
}
