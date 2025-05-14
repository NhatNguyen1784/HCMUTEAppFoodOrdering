package vn.hcmute.appfoodorder.ui.activity.order;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityOrderDetailBinding;
import vn.hcmute.appfoodorder.model.dto.response.OrderDetailDTO;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;
import vn.hcmute.appfoodorder.ui.adapter.OrderDetailAdapter;
import vn.hcmute.appfoodorder.viewmodel.OrderDetailViewModel;
import vn.hcmute.appfoodorder.viewmodel.OrderStatusViewModel;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel viewModel;
    private OrderStatusViewModel statusViewModel;
    private OrderDetailAdapter adapter;
    private ArrayList<Long> listId;
    private Long orderId;
    private Boolean isSuccessful = false;
    // Polling
    private Handler handler = new Handler();
    private Runnable pollingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);
        statusViewModel = new ViewModelProvider(this).get(OrderStatusViewModel.class);

        //Get data from back activity
        orderId = getIntent().getLongExtra("orderId", -1L);
        Toast.makeText(this, orderId.toString(), Toast.LENGTH_SHORT).show();
        if (orderId != -1L) {
            loadOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }

        backOnclick();
    }

    //Image btn back
    private void backOnclick() {
        binding.backImgVOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailActivity.this, OrderStatusActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);//animation
                finish();
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
                    //Lay list orderdetaildto
                    List<OrderDetailDTO> orderDetailList = new ArrayList<>(oDetail.getOrderDetails());
                    //Lay list id orderDetail
                    listId = new ArrayList<>();
                    for (OrderDetailDTO o: orderDetailList) {
                        listId.add(o.getId());
                    }

                    binding.orDetailAddressTv.setText(oDetail.getFullAddress());
                    binding.orDetailInforTv.setText("Người nhận: "+oDetail.getUserName()+"  SĐT: "+ oDetail.getPhoneNumber());
                    binding.orderDetailTotalBillTv.setText(String.format("Tổng đơn hàng: %,.0f đ",oDetail.getTotalPrice()));
                    binding.payOptionTv.setText("Thanh toán: "+oDetail.getPaymentOption());
                    binding.createdDateTv.setText("Thời gian đặt hàng: "+oDetail.getCreatedDate());
                    //Status Pending
                    if(oDetail.getOrderStatus().equals("PENDING")){
                        binding.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(OrderDetailActivity.this)
                                        .setTitle("Xác nhận hủy đơn hàng")
                                        .setMessage("Bạn có chắc muốn hủy đơn hàng này không?")
                                        .setPositiveButton("Hủy đơn", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                statusViewModel.cancelOrderByOrderId(orderId); // orderId truyền vào Fragment
                                                Toast.makeText(OrderDetailActivity.this, "Đã hủy đơn hàng " + orderId, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(OrderDetailActivity.this, OrderStatusActivity.class));
                                                //Xu ly them viec polling do 10 s voi realtime tu pending sang shipping
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Không", null)
                                        .show();
                            }
                        });
                    }
                    //Status shipping
                    else if(oDetail.getOrderStatus().equals("SHIPPING")){
                        binding.detailOrderDetailTv.setText("Đơn của bạn đã được xác nhận. Vui lòng đợi quán nấu và giao tới!");
                        binding.statusOrderDetailTv.setText("Đã xác nhận đơn hàng");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_delivery);
                        //Button
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnNotCancelOrderDetail.setVisibility(View.VISIBLE);
                        binding.btnReorder.setVisibility(View.GONE);
                        binding.btnConfirmOrder.setVisibility(View.GONE);
                    }
                    //Status Delivered
                    else if(oDetail.getOrderStatus().equals("DELIVERED")){
                        binding.detailOrderDetailTv.setText("Đơn đã giao thành công, bạn vui lòng xác nhận và đánh giá nhé!");
                        binding.statusOrderDetailTv.setText("Giao hàng thành công");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_delivered);
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnNotCancelOrderDetail.setVisibility(View.GONE);
                        binding.btnReorder.setVisibility(View.GONE);
                        binding.btnConfirmOrder.setVisibility(View.VISIBLE);

                        binding.btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                statusViewModel.confirmOrderByOrderId(orderId);
                                Toast.makeText(OrderDetailActivity.this, "Đã nhận hàng thành công " + orderId, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OrderDetailActivity.this, OrderStatusActivity.class));
                            }
                        });
                    }
                    //Status successful
                    else if(oDetail.getOrderStatus().equals("SUCCESSFUL")) {
                        binding.detailOrderDetailTv.setText("Nếu cần hổ trợ thêm, bạn vui lòng truy cập và nhắn tin cho quán nhé!");
                        binding.statusOrderDetailTv.setText("Hoàn thành");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_received);
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnConfirmOrder.setVisibility(View.GONE);
                        isSuccessful = true;
                        //chua danh gia
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnNotCancelOrderDetail.setVisibility(View.GONE);
                        binding.btnReorder.setVisibility(View.VISIBLE);
                        binding.btnROD.setVisibility(View.GONE);
                    }
                    //Status cancel
                    else if(oDetail.getOrderStatus().equals("CANCELLED")){
                        binding.detailOrderDetailTv.setText("Nếu cần hổ trợ thêm, bạn vui lòng truy cập và nhắn tin cho quán nhé!");
                        binding.statusOrderDetailTv.setText("Đã hủy");
                        binding.imgStatusOrder.setImageResource(R.drawable.ic_cancelorder);
                        binding.btnCancelOrder.setVisibility(View.GONE);
                        binding.btnROD.setVisibility(View.GONE);
                        binding.btnNotCancelOrderDetail.setVisibility(View.VISIBLE);
                        binding.btnReorder.setVisibility(View.VISIBLE);
                    }
                    adapter = new OrderDetailAdapter(this, orderDetailList, isSuccessful);

                    //set data
                    binding.orderDetailRcv.setAdapter(adapter);
                    binding.orderDetailRcv.setLayoutManager(new LinearLayoutManager(this));
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
                handler.postDelayed(this, 10_000); // Call lai sau 15s
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