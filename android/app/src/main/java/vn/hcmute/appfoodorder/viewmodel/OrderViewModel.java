package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.repository.OrderRepository;

public class OrderViewModel extends ViewModel {
    private OrderRepository orderRepository;

    public OrderViewModel() {
        orderRepository = new OrderRepository();
    }
    public LiveData<ApiResponse<Long>> createOrder(OrderRequest request) {
        return orderRepository.createOrder(request);
    }
}
