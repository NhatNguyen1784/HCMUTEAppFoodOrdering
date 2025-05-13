package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.repository.OrderRepository;

public class OrderStatusViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private final MutableLiveData<List<OrderResponse>> allOrders = new MutableLiveData<>();

    public OrderStatusViewModel() {
        orderRepository = new OrderRepository();
    }

    public LiveData<List<OrderResponse>> getAllOrders() {
        return allOrders;
    }

    public void fetchOrdersByEmail(String email){
        orderRepository.getOrdersByUserEmail(email).observeForever( listApiResponse -> {
            if(listApiResponse.getCode() == 200 && listApiResponse.getResult() != null){
                allOrders.setValue(listApiResponse.getResult());
            }
            else allOrders.setValue(null);
        });
    }
}
