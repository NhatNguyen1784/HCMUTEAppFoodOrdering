package vn.hcmute.appfoodorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.data.api.OrderApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.entity.CartItem;
import vn.hcmute.appfoodorder.ui.adapter.OrderReviewAdapter;
import vn.hcmute.appfoodorder.ui.fragment.address.AddressBottomSheetFragment;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.AddressViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private TextView tvAddress, tvEditAddress;
    private Button btnAddAddress, btnOrder;
    private ImageView btnBack;
    private LinearLayout layoutAddressContainer;
    private AddressViewModel addressViewModel;
    private SessionManager session;
    private String email;
    private OrderReviewAdapter orderRvAdapter;
    private Spinner spPaymentMethod;
    private RecyclerView orderReviewItemRV;
    private List<CartItem> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order);
        getExtraFromCartFragment();
        mappingAndInit();
        updateAddressUI();
        setupRVOrderReview();
        createPaymentMethod();
    }

    private void createPaymentMethod() {
        // Tạo một danh sách các phương thức thanh toán
        List<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("VNPay");
        paymentMethods.add("Momo");
        paymentMethods.add("PayPal");

        // Tạo một ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter cho Spinner
        spPaymentMethod.setAdapter(adapter);

        // Set VNPay làm giá trị mặc định cho Spinner
        int position = paymentMethods.indexOf("VNPay");
        spPaymentMethod.setSelection(position);
    }


    private void setupRVOrderReview() {
        //Adapter
        orderRvAdapter = new OrderReviewAdapter(this, cartList);
        orderReviewItemRV.setAdapter(orderRvAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        orderReviewItemRV.setLayoutManager(layoutManager);
    }

    private void getExtraFromCartFragment() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double subtotal = extras.getDouble("subtotal", 0);
            double taxFee = extras.getDouble("taxFee", 0);
            double total = extras.getDouble("total", 0);
            cartList = (List<CartItem>) extras.getSerializable("cartList");
        }
    }

    private void mappingAndInit() {
        tvAddress = findViewById(R.id.tvAddress);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        tvEditAddress = findViewById(R.id.tvEditAddress);
        btnBack = findViewById(R.id.imgVBackOrder);
        layoutAddressContainer = findViewById(R.id.layoutAddressContainer);
        orderReviewItemRV = findViewById(R.id.rcvOrderReview);
        btnOrder = findViewById(R.id.btn_Order);
        spPaymentMethod = findViewById(R.id.spPaymentMethod);

        //Address ViewModel
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        //Lấy email
        session = new SessionManager(this);
        email = session.getUserInfor().getEmail();

    }

    public void updateAddressUI() {
        // Quan sát LiveData để nhận dữ liệu và cập nhật UI khi dữ liệu thay đổi
        addressViewModel.getAddressLiveData().observe(this, response -> {
            if (response != null && response.getResult() != null) {
                List<String> addresses = response.getResult();
                if (!addresses.isEmpty()) {
                    tvAddress.setText(addresses.get(0));
                    btnAddAddress.setVisibility(View.GONE);
                    tvEditAddress.setVisibility(View.VISIBLE);
                } else {
                    tvAddress.setText("🏠 Shipping address");
                    btnAddAddress.setVisibility(View.VISIBLE);
                    tvEditAddress.setVisibility(View.GONE);
                }
            } else {
                tvAddress.setText("🏠 Shipping address");
                btnAddAddress.setVisibility(View.VISIBLE);
                tvEditAddress.setVisibility(View.GONE);
            }
        });

        // Gọi phương thức trong ViewModel để lấy địa chỉ giao hàng từ API
        addressViewModel.getAddressShipping(email);

        btnAddAddress.setOnClickListener(v -> openAddressBottomSheetFragment());
        tvEditAddress.setOnClickListener(v -> openAddressBottomSheetFragment());
    }

    // Mở AddressBottomSheetFragment để sửa địa chỉ
    public void openAddressBottomSheetFragment(){
        AddressBottomSheetFragment addressBottomSheet = new AddressBottomSheetFragment();
        addressBottomSheet.show(getSupportFragmentManager(), addressBottomSheet.getTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressViewModel.getAddressShipping(email);
    }

}