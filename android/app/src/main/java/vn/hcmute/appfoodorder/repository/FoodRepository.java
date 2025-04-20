package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.FoodApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodRepository {
    private static FoodRepository instance;
    private final FoodApi foodApi;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FoodRepository() {
        this.foodApi = RetrofitClient.getRetrofit().create(FoodApi.class);
    }

    public static FoodRepository getInstance(){
        if(instance == null){
            instance = new FoodRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<Food>>> getFoodByCategory(Long categoryId){

        MutableLiveData<Resource<List<Food>>> resultLiveData = new MutableLiveData<>();

        // đánh dấu đang load dữ liệu
        resultLiveData.setValue(Resource.loading(null));

        foodApi.getFoodByCategory(categoryId).enqueue(new Callback<ApiResponse<List<Food>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Food>>> call, Response<ApiResponse<List<Food>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<List<Food>> apiResult = response.body();
                    if(apiResult.getCode() == 200){
                        // tra ve du lieu thanh cong
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    } else {
                        // tra ve loi tu API
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
            public void onFailure(Call<ApiResponse<List<Food>>> call, Throwable throwable) {
                Log.e("ListFoodRepository", "Error fetching food by category", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }
}
