package vn.hcmute.appfoodorder.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.ReviewApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.ReviewRequest;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;
import vn.hcmute.appfoodorder.utils.Resource;

public class ReviewRepository {
    private static ReviewRepository instance;

    private final ReviewApi reviewApi;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReviewRepository() {
        this.reviewApi = RetrofitClient.getRetrofit().create(ReviewApi.class);
    }

    public static ReviewRepository getInstance() {
        if(instance == null){
            instance = new ReviewRepository();
        }
        return instance;
    }

    public LiveData<Resource<ReviewResponse>> submitReview(ReviewRequest request){
        MutableLiveData<Resource<ReviewResponse>> resultLiveData = new MutableLiveData<>();

        reviewApi.submitReview(request).enqueue(new Callback<ApiResponse<ReviewResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReviewResponse>> call, Response<ApiResponse<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<ReviewResponse> apiResult = response.body();
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
            public void onFailure(Call<ApiResponse<ReviewResponse>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }

    public LiveData<Resource<ReviewListResponse>> getReviewByFoodName(String foodName){
        MutableLiveData<Resource<ReviewListResponse>> resultLiveData = new MutableLiveData<>();

        reviewApi.getReviewProduct(foodName).enqueue(new Callback<ApiResponse<ReviewListResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReviewListResponse>> call, Response<ApiResponse<ReviewListResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<ReviewListResponse> apiResult = response.body();
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
            public void onFailure(Call<ApiResponse<ReviewListResponse>> call, Throwable throwable) {
                resultLiveData.postValue(Resource.error("Network error: " + throwable.getMessage(), null));

            }
        });

        return resultLiveData;
    }
}
