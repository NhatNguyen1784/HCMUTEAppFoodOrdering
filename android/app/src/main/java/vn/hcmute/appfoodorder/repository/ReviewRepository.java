package vn.hcmute.appfoodorder.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public LiveData<Resource<ReviewResponse>> submitReview(ReviewRequest request, List<File> imageFiles){
        MutableLiveData<Resource<ReviewResponse>> resultLiveData = new MutableLiveData<>();

        // Convert JSON reviewRequest thành RequestBody
        String reviewJson = gson.toJson(request);
        RequestBody reviewBody = RequestBody.create(MediaType.parse("application/json"), reviewJson);

        // Conver từng ảnh thành MutipartBody.Part
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for(int i = 0; i<imageFiles.size(); i++){
            File file = imageFiles.get(i);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), fileBody);
            imageParts.add(part);
        }

        reviewApi.submitReview(reviewBody, imageParts).enqueue(new Callback<ApiResponse<ReviewResponse>>() {
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

    public LiveData<Resource<ReviewListResponse>> getReviewByFoodName(Long foodId){
        MutableLiveData<Resource<ReviewListResponse>> resultLiveData = new MutableLiveData<>();

        reviewApi.getReviewProduct(foodId).enqueue(new Callback<ApiResponse<ReviewListResponse>>() {
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
