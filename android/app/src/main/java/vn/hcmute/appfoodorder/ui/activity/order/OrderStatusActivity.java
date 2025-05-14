package vn.hcmute.appfoodorder.ui.activity.order;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.databinding.ActivityOrderStatusBinding;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.adapter.OrderStatusAdapter;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.OrderStatusViewModel;

public class OrderStatusActivity extends AppCompatActivity {

    List<OrderResponse> pendingList;
    List<OrderResponse> historyList;
    private OrderStatusViewModel viewModel;
    private ActivityOrderStatusBinding binding;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        viewModel = new ViewModelProvider(this).get(OrderStatusViewModel.class);

        //Set list
        listDataByStatus();
    }

    private void listDataByStatus() {
        String email = sessionManager.getUserInfor().getEmail();
        viewModel.fetchOrdersByEmail(email);
        viewModel.getAllOrders().observe(this, orders -> {
            if (orders != null) {
                Log.d("OrderStatusActivity", "Fetched orders: " + orders.size());
                pendingList = new ArrayList<>();
                historyList = new ArrayList<>();
                for (OrderResponse order : orders) {
                    switch (order.getOrderStatus()) {
                        case "PENDING":
                        case "SHIPPING":
                            pendingList.add(order);
                            break;
                        case "DELIVERED":
                        case "CANCELLED":
                            historyList.add(order);
                            break;
                    }
                }
                Log.d("OrderStatusActivity", "Pending orders size: " + pendingList.size());

                //Gan adapter
                OrderStatusAdapter adapter = new OrderStatusAdapter(this, pendingList, historyList);
                binding.vPager2OrderStatus.setAdapter(adapter);

                new TabLayoutMediator(binding.tbLayOrderStatus, binding.vPager2OrderStatus, (tab, position) -> {
                    switch (position){
                        case 0: tab.setText("Đang đến"); break;
                        case 1: tab.setText("Lịch sử"); break;
                        case 2: tab.setText("Đánh giá"); break;
                    }
                }).attach();
            } else {
                pendingList = new ArrayList<>();
                historyList = new ArrayList<>();
                Toast.makeText(this, "Orders null", Toast.LENGTH_SHORT).show();
            }
        });
    }


}