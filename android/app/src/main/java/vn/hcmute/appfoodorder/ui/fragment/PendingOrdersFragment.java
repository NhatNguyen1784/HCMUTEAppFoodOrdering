package vn.hcmute.appfoodorder.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.databinding.FragmentPendingOrderBinding;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.adapter.TabStatusOrderAdapter;
import vn.hcmute.appfoodorder.viewmodel.OrderStatusViewModel;

public class PendingOrdersFragment extends Fragment {
    private static final String ARG_ORDERS = "orders";
    private List<OrderResponse> orders;
    private FragmentPendingOrderBinding binding;
    private TabStatusOrderAdapter adapter;
    private OrderStatusViewModel viewModel;

    public static PendingOrdersFragment newInstance(List<OrderResponse> orders) {
        PendingOrdersFragment fragment = new PendingOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDERS, (Serializable) orders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingOrderBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(OrderStatusViewModel.class);//Init view model
        if (getArguments() != null) {
            orders = (List<OrderResponse>) getArguments().getSerializable(ARG_ORDERS);
            if (orders != null && !orders.isEmpty()) {
                binding.recyclerViewPendingOrders.setVisibility(View.VISIBLE);
                binding.noOrdersLayout.setVisibility(View.GONE);
                adapter = new TabStatusOrderAdapter(getContext(), 0, orders, viewModel);
                binding.recyclerViewPendingOrders.setAdapter(adapter);
                binding.recyclerViewPendingOrders.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                binding.recyclerViewPendingOrders.setVisibility(View.GONE);
                binding.noOrdersLayout.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "No pending orders", Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.recyclerViewPendingOrders.setVisibility(View.GONE);
            binding.noOrdersLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No orders data available", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
