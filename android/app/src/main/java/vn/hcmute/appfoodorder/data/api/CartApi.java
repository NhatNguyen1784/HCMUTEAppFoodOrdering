package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.request.DeleteCartRequest;
import vn.hcmute.appfoodorder.model.entity.Cart;

public interface CartApi {
    @POST("cart/add")
    Call<ApiResponse<Cart>> addItemToCart(@Header("Authorization") String token, @Body CartRequest request);

    @GET("cart/get")
    Call<ApiResponse<Cart>> getMyCart(@Header("Authorization") String token);

    @PUT("cart/update")
    Call<ApiResponse<Cart>> updateCartItem(@Header("Authorization") String token, @Body CartRequest request);

    @HTTP(method = "DELETE", path = "cart/delete", hasBody = true)
    Call<ApiResponse<Cart>> deleteCartItem(@Header("Authorization") String token, @Body DeleteCartRequest request);
}
