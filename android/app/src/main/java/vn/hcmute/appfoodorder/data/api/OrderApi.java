package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderApi {
    @GET("order/create-payment")
    Call<String> createPayment(@Query("amount") Long amount);
}
