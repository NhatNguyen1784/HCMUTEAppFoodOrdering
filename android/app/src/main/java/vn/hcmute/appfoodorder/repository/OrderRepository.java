package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.OrderApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;

public class OrderRepository {
    public OrderApi api;

    public OrderRepository() {
        api = RetrofitClient.getRetrofit().create(OrderApi.class);
    }

    public LiveData<ApiResponse<Long>> createOrder(OrderRequest request){
        MutableLiveData<ApiResponse<Long>> order = new MutableLiveData<>();
        api.createOrder(request).enqueue(new Callback<ApiResponse<Long>>() {
            @Override
            public void onResponse(Call<ApiResponse<Long>> call, Response<ApiResponse<Long>> response) {
                if(response.isSuccessful() && response.body() != null){
                    order.setValue(response.body());
                    Log.d("Order", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Long>> call, Throwable throwable) {
                Log.d("Order", "Netword error "+ throwable.getMessage());
                order.setValue(new ApiResponse<>(500, "Network error "+ throwable, null));
            }
        });
        return order;
    }
}
