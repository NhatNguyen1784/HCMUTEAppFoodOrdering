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
import vn.hcmute.appfoodorder.model.dto.response.FoodWithStarResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodListRepository {
    private static FoodListRepository instance;
    private final FoodApi foodApi;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public FoodListRepository() {
        this.foodApi = RetrofitClient.getRetrofit().create(FoodApi.class);
    }

    public static FoodListRepository getInstance(){
        if(instance == null){
            instance = new FoodListRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<FoodWithStarResponse>>> getFoodByCategory(Long categoryId){

        MutableLiveData<Resource<List<FoodWithStarResponse>>> resultLiveData = new MutableLiveData<>();

        // đánh dấu đang load dữ liệu
        resultLiveData.setValue(Resource.loading(null));

        foodApi.getFoodByCategory(categoryId).enqueue(new Callback<ApiResponse<List<FoodWithStarResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<FoodWithStarResponse>>> call, Response<ApiResponse<List<FoodWithStarResponse>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<List<FoodWithStarResponse>> apiResult = response.body();

                    if (apiResult.getCode() == 200){
                        resultLiveData.postValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.postValue(Resource.error("Error: " + apiResult.getMessage(), apiResult.getResult()));
                    }
                }
                else {
                    // xu li loi tu response
                    String errorMessage =  "Unknown error occurred";
                    if (response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                        resultLiveData.postValue(Resource.error(errorMessage, null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<FoodWithStarResponse>>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });


        return resultLiveData;
    }
}
