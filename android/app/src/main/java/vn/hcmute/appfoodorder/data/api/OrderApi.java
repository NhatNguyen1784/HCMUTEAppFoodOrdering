package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;

public interface OrderApi {
    @POST("order/create-order")
    Call<ApiResponse<Long>> createOrder(@Body OrderRequest orderRequest);

    @GET("order/{orderId}/details")
    Call<ApiResponse<OrderDetail>> findOrderDetailByOrderId(@Path("orderId") Long orderid);
}
