package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.repository.AddressRepository;

public class AddressViewModel extends ViewModel {
    private AddressRepository addressRepository;
    private MutableLiveData<ApiResponse<List<String>>> addressLiveData;
    private MutableLiveData<ApiResponse<String>> addAddressResponse;

    public AddressViewModel() {
        addressRepository = new AddressRepository();
        addressLiveData = new MutableLiveData<>();
        addAddressResponse = new MutableLiveData<>();
    }

    // Phương thức để lấy địa chỉ giao hàng từ repository và cập nhật LiveData
    public void getAddressShipping(String token) {
        LiveData<ApiResponse<List<String>>> addresses = addressRepository.getAddressShipping(token);

        // Cập nhật LiveData cho ViewModel
        addresses.observeForever(response -> {
            addressLiveData.setValue(response);
        });
    }

    // Phương thức để lấy LiveData trong ViewModel
    public LiveData<ApiResponse<List<String>>> getAddressLiveData() {
        return addressLiveData;
    }

    public LiveData<ApiResponse<String>> addShippingAddress(String token, String fullAddress) {
        addAddressResponse = (MutableLiveData<ApiResponse<String>>) addressRepository.addShippingAddress(token, fullAddress);
        return addAddressResponse;
    }
}
