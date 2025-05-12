package vn.hcmute.appfoodorder.ui.activity;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.adapter.OrderStatusAdapter;

public class OrderStatusActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private OrderStatusAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_status);
        mappingAndInit();

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0: tab.setText("Đang đến"); break;
                case 1: tab.setText("Lịch sử"); break;
                case 2: tab.setText("Đánh giá"); break;
            }
        }).attach();
    }

    private void mappingAndInit() {
        //mapping
        viewPager2 = findViewById(R.id.vPager2OrderStatus);
        tabLayout = findViewById(R.id.tbLayOrderStatus);
        //init
        adapter = new OrderStatusAdapter(this);
        viewPager2.setAdapter(adapter);
    }
}