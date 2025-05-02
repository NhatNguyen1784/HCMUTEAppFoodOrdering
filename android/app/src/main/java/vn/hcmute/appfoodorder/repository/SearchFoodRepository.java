package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.FoodApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Food;

public class SearchFoodRepository {
    private final FoodApi foodApi;

    public SearchFoodRepository() {
        foodApi = RetrofitClient.getRetrofit().create(FoodApi.class);
    }

    //Search food by name
    public LiveData<ApiResponse<List<Food>>> searchFoodByName(String keyword){
        MutableLiveData<ApiResponse<List<Food>>> foods = new MutableLiveData<>();
        foodApi.searchFoodByName(keyword).enqueue(new Callback<ApiResponse<List<Food>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Food>>> call, Response<ApiResponse<List<Food>>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Food> listFood = response.body().getResult();
                    if(!listFood.isEmpty() && listFood != null){
                        foods.setValue(response.body());
                    }
                    else{
                        foods.setValue(new ApiResponse<>(response.code(), "Empty food", null));
                    }
                }
                else{
                    foods.setValue(new ApiResponse<>(response.code(), "Error load food", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Food>>> call, Throwable throwable) {
                foods.setValue(new ApiResponse<>(500,"Network error (Search food): " + throwable.getMessage(), null));
                Log.e("Search product", "Network error", throwable);
            }
        });
        return foods;
    }
}
