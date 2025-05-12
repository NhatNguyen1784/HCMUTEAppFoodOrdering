package vn.hcmute.appfoodorder.ui.activity;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityOrderStatusBinding;
import vn.hcmute.appfoodorder.ui.adapter.OrderStatusAdapter;

public class OrderStatusActivity extends AppCompatActivity {

    private ActivityOrderStatusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.vPager2OrderStatus.setAdapter(new OrderStatusAdapter(this));

        new TabLayoutMediator(binding.tbLayOrderStatus, binding.vPager2OrderStatus, (tab, position) -> {
            switch (position){
                case 0: tab.setText("Đang đến"); break;
                case 1: tab.setText("Lịch sử"); break;
                case 2: tab.setText("Đánh giá"); break;
            }
        }).attach();
    }

}