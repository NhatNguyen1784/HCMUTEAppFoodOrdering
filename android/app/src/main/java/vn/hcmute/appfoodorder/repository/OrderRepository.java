package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.OrderApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;

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

    public LiveData<ApiResponse<OrderDetail>> getOrderDetailByOrderId(Long orderId){
        MutableLiveData<ApiResponse<OrderDetail>> orderDetail = new MutableLiveData<>();
        api.findOrderDetailByOrderId(orderId).enqueue(new Callback<ApiResponse<OrderDetail>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderDetail>> call, Response<ApiResponse<OrderDetail>> response) {
                if(response.isSuccessful() && response.body() != null){
                    orderDetail.setValue(response.body());
                    Log.d("Order detail", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderDetail>> call, Throwable throwable) {
                Log.d("Order detail", "Netword error "+ throwable.getMessage());
                orderDetail.setValue(new ApiResponse<>(500, "Network error "+ throwable, null));
            }
        });
        return orderDetail;
    }

    public LiveData<ApiResponse<List<OrderResponse>>> getOrdersByUserEmail(String email){
        MutableLiveData<ApiResponse<List<OrderResponse>>> orders = new MutableLiveData<>();
        api.getOrdersByUserEmail(email).enqueue(new Callback<ApiResponse<List<OrderResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<OrderResponse>>> call, Response<ApiResponse<List<OrderResponse>>> response) {
                if(response.isSuccessful() && response.body() != null){
                    orders.setValue(response.body());
                    Log.d("Orders", "Get orders by user's email successful");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<OrderResponse>>> call, Throwable throwable) {
                Log.d("Orders", "Netword error "+ throwable.getMessage());
                orders.setValue(new ApiResponse<>(500, "Network error "+ throwable, null));
            }
        });
        return orders;
    }

    public LiveData<ApiResponse> cancelOrder(Long orderId){
        MutableLiveData<ApiResponse> cancel = new MutableLiveData<>();
        api.cancelOrder(orderId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    cancel.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.d("Orders", "Netword error "+ throwable.getMessage());
                cancel.setValue(new ApiResponse<>(500, "Network error "+ throwable, null));
            }
        });
        return cancel;
    }

    public LiveData<ApiResponse> confirmOrder(Long orderId){
        MutableLiveData<ApiResponse> cancel = new MutableLiveData<>();
        api.confirmOrder(orderId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    cancel.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.d("Orders", "Netword error "+ throwable.getMessage());
                cancel.setValue(new ApiResponse<>(500, "Network error "+ throwable, null));
            }
        });
        return cancel;
    }
}
