package vn.hcmute.appfoodorder.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.fragment.tabstatus.HistoryOrdersFragment;
import vn.hcmute.appfoodorder.ui.fragment.tabstatus.PendingOrdersFragment;
import vn.hcmute.appfoodorder.ui.fragment.tabstatus.RatingOrdersFragment;

public class OrderStatusAdapter extends FragmentStateAdapter {
    private final List<OrderResponse> pendingOrders;
    private final List<OrderResponse> historyOrders;
    public OrderStatusAdapter(@NonNull FragmentActivity activity, List<OrderResponse> pendingOrders, List<OrderResponse> historyOrders) {
        super(activity);
        this.pendingOrders = pendingOrders != null ? pendingOrders : new ArrayList<>();
        this.historyOrders = historyOrders != null ? historyOrders : new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return PendingOrdersFragment.newInstance(pendingOrders);
            case 1:
                return HistoryOrdersFragment.newInstance(historyOrders);
            case 2:
                return new RatingOrdersFragment();
            default:
                return PendingOrdersFragment.newInstance(pendingOrders);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
