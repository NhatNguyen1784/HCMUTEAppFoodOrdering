package vn.hcmute.appfoodorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityOrderDetailBinding;
import vn.hcmute.appfoodorder.model.dto.response.OrderDetailDTO;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;
import vn.hcmute.appfoodorder.ui.adapter.OrderDetailAdapter;
import vn.hcmute.appfoodorder.ui.adapter.OrderReviewAdapter;
import vn.hcmute.appfoodorder.viewmodel.OrderDetailViewModel;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel viewModel;
    private OrderDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new OrderDetailViewModel();

        Long orderId = getIntent().getLongExtra("order_id", -1L);

        if (orderId != -1L) {
            loadOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadOrderDetail(Long orderId) {
        viewModel.fetchOrderDetail(orderId);
        viewModel.getOrderDetail().observe(this, response -> {
            if(response.getCode() == 200 && response.getResult()!=null){
                OrderDetail oDetail = response.getResult();
                if(oDetail.getOrderDetails() != null && oDetail.getTotalPrice()!=null){
                    List<OrderDetailDTO> orderDetailList = new ArrayList<>(oDetail.getOrderDetails());
                    adapter = new OrderDetailAdapter(this, orderDetailList);
                    //set data
                    binding.orderDetailRcv.setAdapter(adapter);
                    binding.orderDetailRcv.setLayoutManager(new LinearLayoutManager(this));
                    binding.orderDetailRcv.setAdapter(adapter);
                    binding.orDetailAddressTv.setText(oDetail.getFullAddress());
                    binding.orDetailInforTv.setText(oDetail.getUserName()+" "+ oDetail.getPhoneNumber());
                    binding.orderDetailTotalBillTv.setText(String.format("Tổng đơn hàng: %,.0f đ",oDetail.getTotalPrice()));
                    binding.payOptionTv.setText("Thanh toán: "+oDetail.getPaymentOption());
                    binding.createdDateTv.setText("Thời gian đặt hàng: "+oDetail.getCreatedDate());
                    
                    if(oDetail.getOrderStatus().equals("SHIPPING")){
                        binding.detailOrderDetailTv.setText("Đơn của bạn đã được xác nhận. Vui lòng đợi quán nấu và giao tới!");
                        binding.statusOrderDetailTv.setText("Đã xác nhận đơn hàng");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_delivery);
                    }
                    else if(oDetail.getOrderStatus().equals("DELIVERED")){
                        binding.detailOrderDetailTv.setText("Nếu cần hổ trợ thêm, bạn vui lòng truy cập và nhắn tin cho quán nhé!");
                        binding.statusOrderDetailTv.setText("Hoàn thành");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_received);
                    }

                }
            }
            else{
                Toast.makeText(this, "Hiện tải chưa tải được thông tin, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, OrderStatusActivity.class));
                finish();
            }
        });

    }
}