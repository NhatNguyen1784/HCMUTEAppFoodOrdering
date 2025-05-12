package vn.hcmute.appfoodorder.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityOrderBinding;
import vn.hcmute.appfoodorder.model.dto.request.OrderDetailRequest;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.entity.CartItem;
import vn.hcmute.appfoodorder.ui.adapter.OrderReviewAdapter;
import vn.hcmute.appfoodorder.ui.fragment.address.AddressBottomSheetFragment;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.AddressViewModel;
import vn.hcmute.appfoodorder.viewmodel.OrderViewModel;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private AddressViewModel addressViewModel;
    private OrderViewModel orderViewModel;
    private SessionManager sessionManager;
    private String email, deliveryMethod;
    private List<CartItem> cartList;
    private Double totalPrice, taxFee, deliveryFee, deliveryFeeTmp, subtotal;
    private Set<OrderDetailRequest> orderDetailRequests = new HashSet<>();
    private OrderReviewAdapter orderRvAdapter;
    private Boolean haveAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getExtrasFromIntent();
        setupSessionAndViewModels();
        setupAddressUI();
        setupRecyclerView();
        setupPaymentMethodSpinner();
        handleOrderCreation();
        deliveryMethodSelection();

        handleEvents();
    }

    private void deliveryMethodSelection() {
        deliveryMethod = "PICKUP";
        deliveryFee = 0.0;
        binding.rgShipping.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbStandard) {
                deliveryFee = deliveryFeeTmp;
                deliveryMethod = "STANDARD";
            } else if (checkedId == R.id.rbExpress) {
                deliveryFee = deliveryFeeTmp * 2;
                deliveryMethod = "FAST";
            }
            else if (checkedId == R.id.rbPickup) {
                deliveryFee = 0.0;
                deliveryMethod = "PICKUP";
            }

            totalPrice = subtotal + deliveryFee + taxFee;// Demo tổng số tiền

            // Cập nhật UI
            binding.tvDeliveryOrder.setText("Vận chuyển: + " + deliveryFee);
            binding.tvTotalOrder.setText("Tổng hóa đơn: " + totalPrice);
        });
    }

    private void getExtrasFromIntent() {
        deliveryFeeTmp = 10000.0;
        deliveryFee = 0.0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartList = (List<CartItem>) extras.getSerializable("cartList");
            totalPrice = extras.getDouble("total");
            taxFee = extras.getDouble("taxFee");
            subtotal = extras.getDouble("subtotal");

            //Binding data
            binding.tvSubTotalOrder.setText(subtotal == 0 ? "Tổng phụ: + 0 đ" : String.format("Tổng phụ: + %,.0f đ", subtotal));
            binding.tvDeliveryOrder.setText(deliveryFee == 0 ? "Vận chuyển: + 0 đ" : String.format("Vận chuyển: + %,.0f đ", deliveryFee));
            binding.tvFeeTaxOrder.setText(taxFee == 0 ? "Thuế: + 0 đ" : String.format("Thuế: + %,.0f đ", taxFee));
            binding.tvTotalOrder.setText(totalPrice == 0 ? "Tổng hóa đơn: 0 đ" : String.format("Tổng hóa đơn: %,.0f đ", totalPrice));

        }
    }

    private void setupSessionAndViewModels() {
        sessionManager = new SessionManager(this);
        email = sessionManager.getUserInfor().getEmail();
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    public void setupAddressUI() {
        addressViewModel.getAddressLiveData().observe(this, response -> {
            if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
                String address = response.getResult().get(0);
                haveAddress = true;
                binding.tvAddress.setText(address);
                binding.btnAddAddress.setVisibility(View.GONE);
                binding.tvEditAddress.setVisibility(View.VISIBLE);
            } else {
                binding.tvAddress.setText("Địa chỉ giao hàng");
                haveAddress = false;
                binding.btnAddAddress.setVisibility(View.VISIBLE);
                binding.tvEditAddress.setVisibility(View.GONE);
            }
        });

        addressViewModel.getAddressShipping(email);

        binding.btnAddAddress.setOnClickListener(v -> openAddressBottomSheetFragment());
        binding.tvEditAddress.setOnClickListener(v -> openAddressBottomSheetFragment());
    }

    private void openAddressBottomSheetFragment() {
        AddressBottomSheetFragment fragment = new AddressBottomSheetFragment();
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    private void setupRecyclerView() {
        orderRvAdapter = new OrderReviewAdapter(this, cartList);
        binding.rcvOrderReview.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvOrderReview.setAdapter(orderRvAdapter);
    }

    private void setupPaymentMethodSpinner() {
        List<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("COD");
        paymentMethods.add("ZALOPAY");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spPaymentMethod.setAdapter(adapter);

        int position = paymentMethods.indexOf("COD");
        binding.spPaymentMethod.setSelection(position);
    }

    private void handleOrderCreation() {
        binding.btnOrder.setOnClickListener(view -> {
            if(haveAddress == true){
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận đặt hàng")
                        .setMessage("Bạn có chắc muốn đặt hàng không?")
                        .setPositiveButton("Đồng ý",(dialog, which) -> {
                        createOrder();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
            else {
                Toast.makeText(this, "You must fill your shipping address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder() {
        String selectedPayment = binding.spPaymentMethod.getSelectedItem().toString();
        OrderRequest request = new OrderRequest();
        if (selectedPayment.equals("COD")) {
            request.setPaymentOption("COD");
            request.setOrderStatus("PENDING");
        } else {
            request.setPaymentOption("ZALOPAY");
            Toast.makeText(this, "Tạm thời chỉ hỗ trợ COD. Vui lòng chọn lại!", Toast.LENGTH_SHORT).show();
        }
        for (CartItem caI: cartList) {
            OrderDetailRequest o = new OrderDetailRequest(caI.getFoodName(), caI.getUnitPrice(), caI.getQuantity(), caI.getFirstImageUrl());
            orderDetailRequests.add(o);
        }
        request.setOrderDetails(orderDetailRequests);
        request.setEmail(email);
        request.setDeliveryMethod(deliveryMethod);
        request.setFullAddress(binding.tvAddress.getText().toString());

        orderViewModel.createOrder(request).observe(this, response -> {
            if (response.getCode() == 200) {
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, OrderStatusActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Đặt hàng thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleEvents() {
        binding.imgVBackOrder.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddressShipping(email);
    }
}
