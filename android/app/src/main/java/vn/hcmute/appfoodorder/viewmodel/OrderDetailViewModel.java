package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;
import vn.hcmute.appfoodorder.repository.OrderRepository;

public class OrderDetailViewModel extends ViewModel {
    private OrderRepository repository;
    private LiveData<ApiResponse<OrderDetail>> orderDetail;

    public OrderDetailViewModel() {
        repository = new OrderRepository();
    }

    public LiveData<ApiResponse<OrderDetail>> getOrderDetail() {
        return orderDetail;
    }

    public void fetchOrderDetail(Long orderId) {
        orderDetail = repository.getOrderDetailByOrderId(orderId);
    }
}
