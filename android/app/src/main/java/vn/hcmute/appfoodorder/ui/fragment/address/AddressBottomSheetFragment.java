package vn.hcmute.appfoodorder.ui.fragment.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.activity.order.OrderActivity;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.AddressViewModel;

public class AddressBottomSheetFragment extends BottomSheetDialogFragment {
    private EditText edtFullAddress;
    private AddressViewModel addressViewModel;
    private Button btnConfirm;
    private SessionManager sessionManager;
    public AddressBottomSheetFragment() {}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_bottom_sheet, container, false);
        mappingAndInit(view);
        addShippingAddr();
        return view;
    }

    private void addShippingAddr() {
        btnConfirm.setOnClickListener(v -> {
            String token = sessionManager.getAuthHeader();
            String address = edtFullAddress.getText().toString().trim();

            if (!address.isEmpty() && !token.isEmpty()) {
                // Gọi ViewModel và observe kết quả
                addressViewModel.addShippingAddress(token, address)
                        .observe(getViewLifecycleOwner(), apiResponse -> {
                            if (apiResponse != null) {
                                if (apiResponse.getCode() == 200) {
                                    Toast.makeText(getContext(), "Address added successfully", Toast.LENGTH_SHORT).show();
                                    dismiss(); // Đóng bottom sheet nếu thành công
                                    if(getActivity() instanceof OrderActivity){
                                        ((OrderActivity) getActivity()).setupAddressUI();
                                    }
                                } else {
                                    Toast.makeText(getContext(), apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void mappingAndInit(View view) {
        edtFullAddress = view.findViewById(R.id.edt_full_address);
        btnConfirm = view.findViewById(R.id.btn_confirm_address);

        //Address viewmodel
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        //Lấy thông tin người dùng
        sessionManager = new SessionManager(getContext());
    }
}
