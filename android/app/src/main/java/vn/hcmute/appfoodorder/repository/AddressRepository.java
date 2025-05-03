package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.AuthApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Address;

public class AddressRepository {
    private AuthApi api;

    public AddressRepository() {
        api = RetrofitClient.getRetrofit().create(AuthApi.class);
    }

    public LiveData<ApiResponse<List<String>>> getAddressShipping(String email){
        MutableLiveData<ApiResponse<List<String>>> addresses = new MutableLiveData<>();
        api.getAllAddresses(email).enqueue(new Callback<ApiResponse<List<String>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<String>>> call, Response<ApiResponse<List<String>>> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("Sp Addr","Find shipping address successful");
                    addresses.setValue(response.body());
                }
                else{
                    Log.d("Sp Addr","Not found shipping address");
                    addresses.setValue(new ApiResponse<>(400, "Error not found shipping address", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<String>>> call, Throwable throwable) {
                Log.d("Sp addr", "Error network");
                addresses.setValue(new ApiResponse<>(500, "Error "+ throwable.getMessage(), null));
            }
        });
        return addresses;
    }

    public LiveData<ApiResponse<String>> addShippingAddress(Address address){
        MutableLiveData<ApiResponse<String>> addAddress = new MutableLiveData<>();
        api.addShippingAddress(address).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addAddress.setValue(response.body());
                } else {
                    Log.d("Add Sp Addr","Failed to add shipping address. Please try again.");
                    addAddress.setValue(new ApiResponse<>(400, "Error add shipping address", null)); // Cập nhật LiveData với thông báo lỗi
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {
                Log.d("Add Sp addr", "Error network "+throwable.getMessage());
                addAddress.setValue(new ApiResponse<>(500, "Error "+ throwable.getMessage(), null));
            }
        });
        return addAddress;
    }
}
