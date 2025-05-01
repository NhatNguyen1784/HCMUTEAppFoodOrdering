package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.request.DeleteCartRequest;
import vn.hcmute.appfoodorder.model.entity.Cart;

public interface CartApi {
    @POST("cart/add")
    Call<ApiResponse<Cart>> addItemToCart(@Body CartRequest request);

    @GET("cart/get")
    Call<ApiResponse<Cart>> getMyCart(@Query("email") String email);

    @PUT("cart/update")
    Call<ApiResponse<Cart>> updateCartItem(@Body CartRequest request);

    @DELETE("cart/delete")
    Call<ApiResponse<Cart>> deleteCartItem(@Body DeleteCartRequest request);
}
