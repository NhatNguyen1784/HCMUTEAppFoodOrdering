package vn.hcmute.appfoodorder.ui.fragment.tabstatus;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.FragmentHistoryOrdersBinding;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.adapter.TabStatusOrderAdapter;
import vn.hcmute.appfoodorder.viewmodel.OrderStatusViewModel;

public class HistoryOrdersFragment extends Fragment {
    private static final String ARG_HISTORY = "orders";
    private List<OrderResponse> orders;

    private TabStatusOrderAdapter adapter;
    private OrderStatusViewModel viewModel;

    private FragmentHistoryOrdersBinding binding;

    public static HistoryOrdersFragment newInstance(List<OrderResponse> orders){
        HistoryOrdersFragment fragment = new HistoryOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HISTORY, (Serializable) orders);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentHistoryOrdersBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            orders = (List<OrderResponse>) getArguments().getSerializable(ARG_HISTORY);
            if (orders != null && !orders.isEmpty()) {
                binding.recyclerViewHistoryOrders.setVisibility(View.VISIBLE);
                binding.noOrdersLayout.setVisibility(View.GONE);
                adapter = new TabStatusOrderAdapter(getContext(), 1, orders, viewModel);
                binding.recyclerViewHistoryOrders.setAdapter(adapter);
                binding.recyclerViewHistoryOrders.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                binding.recyclerViewHistoryOrders.setVisibility(View.GONE);
                binding.noOrdersLayout.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(), "No pending orders", Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.recyclerViewHistoryOrders.setVisibility(View.GONE);
            binding.noOrdersLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No orders data available in history", Toast.LENGTH_SHORT).show();
        }


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}