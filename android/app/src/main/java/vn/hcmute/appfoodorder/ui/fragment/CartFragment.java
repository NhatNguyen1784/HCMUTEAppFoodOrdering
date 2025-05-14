package vn.hcmute.appfoodorder.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.request.DeleteCartRequest;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.model.entity.Cart;
import vn.hcmute.appfoodorder.model.entity.CartItem;
import vn.hcmute.appfoodorder.ui.activity.order.OrderActivity;
import vn.hcmute.appfoodorder.ui.adapter.CartAdapter;
import vn.hcmute.appfoodorder.viewmodel.CartViewModel;
import vn.hcmute.appfoodorder.viewmodel.ProfileViewModel;

public class CartFragment extends Fragment {
    private RecyclerView rcvCart;
    private TextView tvSubTotal, tvDelivery, tvFeeTax, tvTotal;
    private Button btnOrder;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private List<CartItem> cartItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate layout for fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        anhxa(view);
        setupRecycleView();
        getMyCart();
        setupListener();
        return view;
    }


    private void setupListener() {
        cartAdapter.setCartItemListener(new CartAdapter.CartItemListener() {
            @Override
            public void onIncreaseQuantity(CartItem item) {
                item.setQuantity(item.getQuantity() + 1);
                updateCartItem(item);
            }

            @Override
            public void onDecreaseQuantity(CartItem item) {
                if(item.getQuantity() > 1){
                    item.setQuantity(item.getQuantity() - 1);
                    updateCartItem(item);
                }
            }

            @Override
            public void removeCartItem(CartItem item) {
                deleteCartItem(item);
            }
        });
    }

    private void getCurrentUser(Consumer<UserResponse> callback) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(requireContext());
        profileViewModel.getUserInfor().observe(getViewLifecycleOwner(), userResponse -> {
            if (userResponse != null) {
                callback.accept(userResponse); // gọi lại khi có dữ liệu
            }
        });
    }

    private void deleteCartItem(CartItem item){
        DeleteCartRequest request = new DeleteCartRequest();
        getCurrentUser(user -> {
            String email = user.getEmail();
            request.setEmail(email);
            request.setFoodId(item.getFoodId());

            cartViewModel.deleteCartItem(request);
        });

    }

    private void updateCartItem(CartItem item){
        CartRequest request = new CartRequest();
        getCurrentUser(user -> {
            String email = user.getEmail();
            request.setEmail(email);
            request.setFoodId(item.getFoodId());
            request.setQuantity(item.getQuantity());
            cartViewModel.updateCartItem(request);
        });
    }

    private void getMyCart() {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        getCurrentUser(user -> {
            String email = user.getEmail();
            cartViewModel.getMyCart(email);
        });

        cartViewModel.getCartLiveData().observe(getViewLifecycleOwner(), new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                if (cart != null && cart.getCartDetails() != null && !cart.getCartDetails().isEmpty()) { // Kiểm tra giỏ hàng không rỗng
                    cartItems = cart.getCartDetails();
                    cartAdapter.setData(cartItems);

                    // tính tổng giá (subtotal)
                    double subtotal = cartItems.stream().mapToDouble(CartItem::getPrice).sum();

                    // Mặc định pickup
                    double deliveryFee = 0;
                    double taxFee = subtotal * 0.1; // thuế 10%
                    double remainder = taxFee % 1000;//Làm tròn

                    if (remainder >= 500) {
                        taxFee = Math.ceil(taxFee / 1000) * 1000;
                    } else {
                        taxFee = Math.floor(taxFee / 1000) * 1000;
                    }

                    // Demo tổng số tiền trả trước, không tính phần Intent vì sẽ phim phạm bảo mật
                    double total = subtotal + deliveryFee + taxFee;

                    // hiển thị lên UI
                    tvSubTotal.setText(String.format("%,.0f đ", subtotal));
                    tvDelivery.setText(String.format("%,.0f đ", deliveryFee));
                    tvFeeTax.setText(String.format("%,.0f đ", taxFee));
                    tvTotal.setText(String.format("%,.0f đ", total));

                    double finalTaxFee = taxFee;
                    btnOrder.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), OrderActivity.class);
                        Bundle bundle = new Bundle(); // Xài Bundle thay putExtra vì các kiểu dữ liệu phức tạp
                        bundle.putDouble("subtotal", subtotal);
                        bundle.putDouble("taxFee", finalTaxFee);
                        bundle.putDouble("total", total);
                        // Truyền danh sách cart
                        bundle.putSerializable("cartList", new ArrayList<>(cartItems));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    });

                } else {

                    // Giỏ hàng rỗng - cập nhật lại giao diện
                    cartItems.clear();
                    cartAdapter.setData(cartItems); // Cập nhật adapter để xóa hết item

                    tvSubTotal.setText("0 đ");
                    tvDelivery.setText("0 đ");
                    tvFeeTax.setText("0 đ");
                    tvTotal.setText("0 đ");

                    btnOrder.setOnClickListener(v-> {
                        Toast.makeText(getContext(), "You must add food to cart", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        cartViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getView().getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("Error load cart: ", errorMessage);
            }
        });
    }


    private void anhxa(View view) {
        rcvCart = view.findViewById(R.id.rcvCartItem);
        tvSubTotal = view.findViewById(R.id.tvSubTotalCart);
        tvDelivery = view.findViewById(R.id.tvDeliveryCart);
        tvFeeTax = view.findViewById(R.id.tvFeeTax);
        tvTotal = view.findViewById(R.id.tvTotalCart);
        btnOrder = view.findViewById(R.id.btnOrder);
    }

    private void setupRecycleView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCart.setLayoutManager(layoutManager);
        rcvCart.setItemAnimator(null);
        cartAdapter = new CartAdapter(getContext());
        rcvCart.setAdapter(cartAdapter);
    }
}
