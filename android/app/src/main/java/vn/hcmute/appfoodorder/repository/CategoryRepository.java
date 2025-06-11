package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.CategoryApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiErrorResponse;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;
import vn.hcmute.appfoodorder.utils.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CategoryRepository {
    private static CategoryRepository instance;

    private final CategoryApi categoryApi;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CategoryRepository(){
        categoryApi = RetrofitClient.getRetrofit().create(CategoryApi.class);
    }

    public static CategoryRepository getInstance(){
        if(instance == null){
            instance = new CategoryRepository();
        }
        return instance;
    }

    public LiveData<Resource<List<Category>>> getAllCategory(){

        MutableLiveData<Resource<List<Category>>> resultLiveData = new MutableLiveData<>();

        categoryApi.getAllCategory().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                // kiem tra response
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<List<Category>> apiResult = response.body();
                    if( apiResult.getCode() == 200){
                        resultLiveData.setValue(Resource.success(apiResult.getResult()));
                    }
                    else {
                        resultLiveData.setValue(Resource.error("Error code: " + apiResult.getCode(), null));
                    }
                }
                else { // kiem tra loi trong body
                    String errorMessage = "Unknown error occurred";
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.
                                fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        errorMessage = errorResponse.getMessage();
                    }
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.e("ListCategoryRepository", "Error fetching category", throwable);
                resultLiveData.setValue(Resource.error("Network error: " + throwable.getMessage(), null));
            }
        });

        return resultLiveData;
    }


}
