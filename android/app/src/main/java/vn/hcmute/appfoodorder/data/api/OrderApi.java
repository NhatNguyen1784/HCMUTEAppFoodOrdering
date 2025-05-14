package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;

public interface OrderApi {
    @POST("order/create-order")
    Call<ApiResponse<Long>> createOrder(@Body OrderRequest orderRequest);

    @GET("order/{orderId}/details")
    Call<ApiResponse<OrderDetail>> findOrderDetailByOrderId(@Path("orderId") Long orderid);

    @GET("order")
    Call<ApiResponse<List<OrderResponse>>> getOrdersByUserEmail(@Query("email") String email);

    @PUT("order/{orderId}/cancel")
    Call<ApiResponse> cancelOrder(@Path("orderId") Long orderId);

    @PUT("order/{orderId}/confirm")
    Call<ApiResponse> confirmOrder(@Path("orderId") Long orderid);
}
