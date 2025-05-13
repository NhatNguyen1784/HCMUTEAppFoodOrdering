package vn.hcmute.appfoodorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import vn.hcmute.appfoodorder.viewmodel.OrderDetailViewModel;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel viewModel;
    private OrderDetailAdapter adapter;
    private ArrayList<Long> listId;
    private Long orderId;
    // Polling
    private Handler handler = new Handler();
    private Runnable pollingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new OrderDetailViewModel();

        orderId = getIntent().getLongExtra("order_id", -1L);

        if (orderId != -1L) {
            loadOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }

        reviewOrder();
    }

    private void reviewOrder() {
        binding.btnReviewOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(OrderDetailActivity.this, OrderStatusActivity.class); //Thay thanh Review Activity
                intent.putExtra("o_detail_id_list", listId);
                startActivity(intent);

                 */
                Toast.makeText(OrderDetailActivity.this, listId.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrderDetail(Long orderId) {
        viewModel.fetchOrderDetail(orderId);
        setupObserver(orderId);
    }

    private void setupObserver(Long orderId) {
        viewModel.getOrderDetail().observe(this, response -> {
            if(response.getCode() == 200 && response.getResult()!=null){
                OrderDetail oDetail = response.getResult();
                if(oDetail.getOrderDetails() != null && oDetail.getTotalPrice()!=null){
                    List<OrderDetailDTO> orderDetailList = new ArrayList<>(oDetail.getOrderDetails());
                    adapter = new OrderDetailAdapter(this, orderDetailList);
                    listId = new ArrayList<>();
                    for (OrderDetailDTO o: orderDetailList) {
                        listId.add(o.getId());
                    }
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
                        //Button
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnSupportOrderDetail.setVisibility(View.VISIBLE);
                        binding.btnReorder.setVisibility(View.GONE);
                        binding.btnReviewOrderDetail.setVisibility(View.GONE);
                    }
                    else if(oDetail.getOrderStatus().equals("DELIVERED")){
                        binding.detailOrderDetailTv.setText("Nếu cần hổ trợ thêm, bạn vui lòng truy cập và nhắn tin cho quán nhé!");
                        binding.statusOrderDetailTv.setText("Hoàn thành");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_received);
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        /*
                        * Neu da danh gia hien
                        * binding.btnROD.setVisibility(View.VISIBLE);
                        * binding.btnReviewOrderDetail.setVisibility(View.GONE);
                         * */
                        //chua danh gia
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnSupportOrderDetail.setVisibility(View.GONE);
                        binding.btnReorder.setVisibility(View.VISIBLE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnReviewOrderDetail.setVisibility(View.VISIBLE);
                    }
                    else if(oDetail.getOrderStatus().equals("CANCELLED")){
                        binding.detailOrderDetailTv.setText("Nếu cần hổ trợ thêm, bạn vui lòng truy cập và nhắn tin cho quán nhé!");
                        binding.statusOrderDetailTv.setText("Đã hủy");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_cancelorder);
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnSupportOrderDetail.setVisibility(View.VISIBLE);
                        binding.btnReorder.setVisibility(View.VISIBLE);
                        binding.btnReviewOrderDetail.setVisibility(View.GONE);
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

    public void startPolling(){
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                loadOrderDetail(orderId);//Call API
                handler.postDelayed(this, 30_000); // Call lai sau 30s
            }
        };
        handler.post(pollingRunnable);
    }

    private void stopPolling() {
        if (pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (orderId != -1L) {
            startPolling();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPolling();
    }
}