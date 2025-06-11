package vn.hcmute.appfoodorder.ui.activity.order;

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
import vn.hcmute.appfoodorder.ui.activity.payment.PaymentVNPayActivity;
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
        String token = new SessionManager(OrderActivity.this).getAuthHeader();
        addressViewModel.getAddressShipping(token);

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
        paymentMethods.add("VNPAY");

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
                        submitOrder();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
            else {
                Toast.makeText(this, "Bạn phải điền địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitOrder() {
        String selectedPayment = binding.spPaymentMethod.getSelectedItem().toString();
        OrderRequest request = buildOrderRequest();
        String token = new SessionManager(OrderActivity.this).getAuthHeader();
        if (selectedPayment.equals("COD")) {
            request.setPaymentOption("COD");
        }
        else if(selectedPayment.equals("VNPAY")){
            request.setPaymentOption("VNPAY");
        }
        orderViewModel.createOrder(token, request).observe(this, response -> {
            if (response.getCode() == 200) {
                if(response.getResult() != null){
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    Long orderId = response.getResult();
                    if (request.getPaymentOption().equals("VNPAY")) {
                        long roundedTotal = ((long) Math.ceil(totalPrice / 1000)) * 1000;
                        orderViewModel.createVNPayPayment(String.valueOf(roundedTotal), "NCB")
                                .observe(this, vnPayResponse -> {
                                    if (vnPayResponse != null && vnPayResponse.getData().getPaymentUrl() != null) {
                                        Intent intent = new Intent(this, PaymentVNPayActivity.class);
                                        intent.putExtra("orderId", orderId);
                                        intent.putExtra("paymentUrl", vnPayResponse.getData().getPaymentUrl());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Lỗi khi tạo thanh toán VNPAY", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    else {
                        Intent intent = new Intent(this, OrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(this, "Bạn đang có 1 đơn hàng chờ xử lý, không thể đặt thêm đơn khác!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OrderStatusActivity.class));
                }
            } else {
                Toast.makeText(this, "Đặt hàng thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private OrderRequest buildOrderRequest() {
        OrderRequest request = new OrderRequest();
        for (CartItem caI: cartList) {
            orderDetailRequests.add(new OrderDetailRequest(
                    caI.getFoodId(),
                    caI.getFoodName(),
                    caI.getUnitPrice(),
                    caI.getQuantity(),
                    caI.getFirstImageUrl()
            ));
        }
        request.setOrderDetails(orderDetailRequests);
        request.setDeliveryMethod(deliveryMethod);
        request.setFullAddress(binding.tvAddress.getText().toString());
        request.setOrderStatus("PENDING");
        return request;
    }


    private void handleEvents() {
        binding.imgVBackOrder.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddressShipping(new SessionManager(OrderActivity.this).getAuthHeader());
    }
}
