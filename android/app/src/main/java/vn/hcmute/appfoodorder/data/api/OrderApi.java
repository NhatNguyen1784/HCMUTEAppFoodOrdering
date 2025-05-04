package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.OrderRequest;

public interface OrderApi {
    @POST("order/create-order")
    Call<ApiResponse<Long>> createOrder(@Body OrderRequest orderRequest);
}
