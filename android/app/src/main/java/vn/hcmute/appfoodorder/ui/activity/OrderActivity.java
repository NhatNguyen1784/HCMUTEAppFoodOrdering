package vn.hcmute.appfoodorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.config.zalopay.Api.CreateOrder;
import vn.hcmute.appfoodorder.databinding.ActivityOrderBinding;
import vn.hcmute.appfoodorder.model.dto.request.OrderDetailRequest;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.entity.CartItem;
import vn.hcmute.appfoodorder.ui.adapter.OrderReviewAdapter;
import vn.hcmute.appfoodorder.ui.fragment.address.AddressBottomSheetFragment;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.AddressViewModel;
import vn.hcmute.appfoodorder.viewmodel.OrderViewModel;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

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
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);

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
            binding.tvDeliveryOrder.setText("Delivery: + " + deliveryFee);
            binding.tvTotalOrder.setText("Total bill: " + totalPrice);
        });
    }

    private void getExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartList = (List<CartItem>) extras.getSerializable("cartList");
            totalPrice = extras.getDouble("total");
            taxFee = extras.getDouble("taxFee");
            subtotal = extras.getDouble("subtotal");

            deliveryFeeTmp = 10000.0;
            deliveryFee = 0.0;
            //Binding data
            binding.tvSubTotalOrder.setText("Subtotal: + "+ subtotal);
            binding.tvDiscountOrder.setText("Discount: - "+0);
            binding.tvDeliveryOrder.setText("Delivery: + "+ deliveryFee);
            binding.tvFeeTaxOrder.setText("Tex fees: + "+taxFee);
            binding.tvTotalOrder.setText("Total bill: "+ totalPrice);
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
                binding.tvAddress.setText("Shipping address");
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
                String selectedPayment = binding.spPaymentMethod.getSelectedItem().toString();
                OrderRequest request = new OrderRequest();
                request.setOrderStatus("PENDING");

                for (CartItem caI: cartList) {
                    OrderDetailRequest o = new OrderDetailRequest(caI.getFoodName(), caI.getUnitPrice(), caI.getQuantity(), caI.getFirstImageUrl());
                    orderDetailRequests.add(o);
                }
                request.setOrderDetails(orderDetailRequests);
                request.setEmail(email);
                request.setDeliveryMethod(deliveryMethod);
                request.setFullAddress(binding.tvAddress.getText().toString());

                if (selectedPayment.equals("COD")) {
                    request.setPaymentOption("COD");
                } else if(selectedPayment.equals("ZALOPAY")){
                    request.setPaymentOption("ZALOPAY");
                }
                orderViewModel.createOrder(request).observe(this, response -> {
                    if (response.getCode() == 200) {
                        if (selectedPayment.equals("COD")) {
                            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if(selectedPayment.equals("ZALOPAY")){
                            CreateOrder orderApi = new CreateOrder();
                            try {
                                JSONObject data = orderApi.createOrder("20000");
                                Toast.makeText(OrderActivity.this, "Order created: " + data.toString(), Toast.LENGTH_LONG).show();
                                String code = data.getString("returncode");
                                Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                                if (code.equals("1")) {
                                    String token = data.getString("zptranstoken");
                                    Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                                    ZaloPaySDK.getInstance().payOrder(OrderActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                        @Override
                                        public void onPaymentSucceeded(String s, String s1, String s2) {
                                            Intent intent = new Intent(OrderActivity.this, PaymentNotification.class);
                                            intent.putExtra("result", "Thanh toán thành công");
                                        }

                                        @Override
                                        public void onPaymentCanceled(String s, String s1) {
                                            Intent intent = new Intent(OrderActivity.this, PaymentNotification.class);
                                            intent.putExtra("result", "Hủy thanh toán");
                                        }

                                        @Override
                                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                            Intent intent = new Intent(OrderActivity.this, PaymentNotification.class);
                                            intent.putExtra("result", "Lỗi thanh toán");
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        Toast.makeText(this, "Đặt hàng thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(this, "You must fill your shipping address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateVNPAYPayment(OrderRequest request) {
    }

    private void handleEvents() {
        binding.imgVBackOrder.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddressShipping(email);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
