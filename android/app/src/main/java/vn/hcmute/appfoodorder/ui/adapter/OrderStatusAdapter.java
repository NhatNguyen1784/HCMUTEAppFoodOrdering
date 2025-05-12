package vn.hcmute.appfoodorder.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.hcmute.appfoodorder.ui.fragment.HistoryOrdersFragment;
import vn.hcmute.appfoodorder.ui.fragment.PendingOrdersFragment;
import vn.hcmute.appfoodorder.ui.fragment.RatingOrdersFragment;

public class OrderStatusAdapter extends FragmentStateAdapter {
    public OrderStatusAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public OrderStatusAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public OrderStatusAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PendingOrdersFragment();
            case 1:
                return new HistoryOrdersFragment();
            case 2:
                return new RatingOrdersFragment();
            default:
                return new PendingOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
