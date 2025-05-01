package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.FoodApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodDetailRepository {
    private static FoodDetailRepository instance;
    private final FoodApi foodApi;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FoodDetailRepository() {
        this.foodApi = RetrofitClient.getRetrofit().create(FoodApi.class);
    }

    public static FoodDetailRepository getInstance(){
        if (instance == null){
            instance = new FoodDetailRepository();
        }
        return instance;
    }

    public LiveData<Resource<Food>> getFoodById(Long foodId){
        MutableLiveData<Resource<Food>> resultLiveData = new MutableLiveData<>();

        foodApi.getFoodById(foodId).enqueue(new Callback<ApiResponse<Food>>() {
            @Override
            public void onResponse(Call<ApiResponse<Food>> call, Response<ApiResponse<Food>> response) {
                // kiem tra response
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<Food> apiResult = response.body();
                    if (apiResult.getCode() == 200){
                        // tra ve du lieu thanh cong
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else {
                    String errorMessage = "Unknown error occurred";
                    if (response.body() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Food>> call, Throwable throwable) {
                Log.e("FoodDetailRepository", "Error fetching food by Food Id", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }
}
