package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.repository.OrderRepository;

public class OrderStatusViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private final MutableLiveData<List<OrderResponse>> allOrders = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> cancelOrder = new MutableLiveData<>();

    public OrderStatusViewModel() {
        orderRepository = new OrderRepository();
    }

    public LiveData<ApiResponse> getCancelOrder() {
        return cancelOrder;
    }

    public LiveData<List<OrderResponse>> getAllOrders() {
        return allOrders;
    }

    //Get all order by user email
    public void fetchOrdersByEmail(String email){
        orderRepository.getOrdersByUserEmail(email).observeForever( listApiResponse -> {
            if(listApiResponse.getCode() == 200 && listApiResponse.getResult() != null){
                allOrders.setValue(listApiResponse.getResult());
            }
            else allOrders.setValue(null);
        });
    }

    //Cancel order
    public void cancelOrderByOrderId(Long orderId){
        orderRepository.cancelOrder(orderId).observeForever(apiResponse -> {
            if(apiResponse.getCode() == 200){
                cancelOrder.setValue(apiResponse);
            }
            else {
                cancelOrder.setValue(apiResponse);
            }
        });
    }
}
