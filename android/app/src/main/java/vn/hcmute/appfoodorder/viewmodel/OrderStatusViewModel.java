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
    private final MutableLiveData<ApiResponse> confirmOrder = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> shippingOrder = new MutableLiveData<>();

    public OrderStatusViewModel() {
        orderRepository = new OrderRepository();
    }

    public LiveData<ApiResponse> getCancelOrder() {
        return cancelOrder;
    }

    public LiveData<ApiResponse> getConfirmOrder() {
        return confirmOrder;
    }

    public LiveData<List<OrderResponse>> getAllOrders() {
        return allOrders;
    }

    public LiveData<ApiResponse> getShippingOrder() {
        return shippingOrder;
    }

    //Get all order by user email
    public void fetchOrdersByEmail(String token){
        orderRepository.getOrdersByUserEmail(token).observeForever( listApiResponse -> {
            if(listApiResponse.getCode() == 200 && listApiResponse.getResult() != null){
                allOrders.setValue(listApiResponse.getResult());
            }
            else allOrders.setValue(null);
        });
    }

    //Cancel order
    public void cancelOrderByOrderId(String token, Long orderId){
        orderRepository.cancelOrder(token, orderId).observeForever(apiResponse -> {
            if(apiResponse.getCode() == 200){
                cancelOrder.setValue(apiResponse);
            }
            else {
                cancelOrder.setValue(apiResponse);
            }
        });
    }

    //Confirm order
    public void confirmOrderByOrderId(String token, Long orderId){
        orderRepository.confirmOrder(token, orderId).observeForever(apiResponse -> {
            if(apiResponse.getCode() == 200){
                confirmOrder.setValue(apiResponse);
            }
            else {
                confirmOrder.setValue(apiResponse);
            }
        });
    }

    //Shipping order
    public void shippingOrderByOrderId(Long orderId){
        orderRepository.shippingOrder(orderId).observeForever(apiResponse -> {
            if(apiResponse.getCode() == 200){
                shippingOrder.setValue(apiResponse);
            }
            else {
                shippingOrder.setValue(apiResponse);
            }
        });
    }
}
