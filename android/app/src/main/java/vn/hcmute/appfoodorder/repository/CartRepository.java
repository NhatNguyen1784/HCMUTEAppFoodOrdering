package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.CartApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.request.DeleteCartRequest;
import vn.hcmute.appfoodorder.model.entity.Cart;
import vn.hcmute.appfoodorder.utils.Resource;

public class CartRepository {
    private static CartRepository instance;
    private final CartApi cartApi;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CartRepository() {
        this.cartApi = RetrofitClient.getRetrofit().create(CartApi.class);
    }

    public static CartRepository getInstance(){
        if (instance == null){
            instance = new CartRepository();
        }
        return instance;
    }

    public LiveData<Resource<Cart>> addItemToCart(CartRequest request){
        MutableLiveData<Resource<Cart>> resultLiveData = new MutableLiveData<>();

        cartApi.addItemToCart(request).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Cart> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.body() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable throwable) {
                Log.e("AddToCart", "Error add item to cart", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });
        return resultLiveData;
    }

    public LiveData<Resource<Cart>> updateCartItem(CartRequest request){
        MutableLiveData<Resource<Cart>> resultLiveData = new MutableLiveData<>();

        cartApi.updateCartItem(request).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Cart> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.body() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable throwable) {
                Log.e("CartRepository", "Error update item cart", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });
        return resultLiveData;
    }

    public LiveData<Resource<Cart>> deleteCartItem(DeleteCartRequest request){
        MutableLiveData<Resource<Cart>> resultLiveData = new MutableLiveData<>();

        cartApi.deleteCartItem(request).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Cart> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.body() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable throwable) {
                Log.e("CartRepository", "Error delete item cart", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });
        return resultLiveData;
    }

    public LiveData<Resource<Cart>> getMyCart(String request){
        MutableLiveData<Resource<Cart>> resultLiveData = new MutableLiveData<>();

        cartApi.getMyCart(request).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Cart> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if(response.body() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable throwable) {
                Log.e("CartRepository", "Error get my cart", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });
        return resultLiveData;
    }

}
