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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CategoryRepository {
    private static CategoryRepository instance;

    private final CategoryApi categoryApi;

    private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();

    private MutableLiveData<String> messageError = new MutableLiveData<>();

    private MutableLiveData<Boolean> getError = new MutableLiveData<>();

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

    public void getAllCategory(){
        categoryApi.getAllCategory().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                // kiem tra response
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<List<Category>> apiResult = response.body();
                    if( apiResult.getCode() == 200){
                        categoryList.setValue(apiResult.getResult());
                    }
                    else {
                        getError.setValue(true);
                    }
                }
                else { // kiem tra loi trong body
                    if(response.errorBody() != null){
                        ApiErrorResponse errorResponse = gson.
                                fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        messageError.setValue(errorResponse.getMessage());
                    }
                    else {
                        getError.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.d("Error get all category", throwable.getMessage());
                getError.setValue(true);
            }
        });
    }

    public MutableLiveData<List<Category>> getCategoryList(){
        return categoryList;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getGetError() {
        return getError;
    }

}
