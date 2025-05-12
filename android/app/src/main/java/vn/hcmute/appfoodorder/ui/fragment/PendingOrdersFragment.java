package vn.hcmute.appfoodorder.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.viewmodel.PendingOrderViewModel;

public class PendingOrdersFragment extends Fragment {

    public static PendingOrdersFragment newInstance() {
        return new PendingOrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_order, container, false);


        return view;
    }


}