package vn.hcmute.appfoodorder.ui.fragment.tabstatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;

public class RatingOrdersFragment extends Fragment {

    public static RatingOrdersFragment newIntance(List<OrderResponse> orders){
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rating_orders, container, false);
    }
}