package vn.hcmute.appfoodorder.data.api;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.model.dto.response.ResponseObject;
import vn.hcmute.appfoodorder.model.dto.response.VNPayResponse;
import vn.hcmute.appfoodorder.model.entity.OrderDetail;

public interface OrderApi {
    @POST("order/create-order")
    Call<ApiResponse<Long>> createOrder(@Header("Authorization") String token, @Body OrderRequest orderRequest);

    @GET("order/{orderId}/details")
    Call<ApiResponse<OrderDetail>> findOrderDetailByOrderId(@Header("Authorization") String token, @Path("orderId") Long orderid);

    @GET("order")
    Call<ApiResponse<List<OrderResponse>>> getOrdersByUserEmail(@Header("Authorization") String token);

    @PUT("order/{orderId}/shipping")
    Call<ApiResponse> shippingOrder(@Path("orderId") Long orderId);

    @PUT("order/{orderId}/cancel")
    Call<ApiResponse> cancelOrder(@Header("Authorization") String token, @Path("orderId") Long orderId);

    @PUT("order/{orderId}/confirm")
    Call<ApiResponse> confirmOrder(@Header("Authorization") String token, @Path("orderId") Long orderid);

    @GET("order/payment/vn-pay")
    Call<ResponseObject<VNPayResponse>> createVNPayPayment(@Query("amount") String amount, @Query("backCode") String bankCode);

}
