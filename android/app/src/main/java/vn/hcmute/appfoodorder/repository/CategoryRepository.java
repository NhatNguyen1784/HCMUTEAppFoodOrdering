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
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;
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


// Tang repo se lay data tu api
    public LiveData<ApiResponse<List<Category>>> getAllCategory(){
        MutableLiveData<ApiResponse<List<Category>>> data = new MutableLiveData<>();

        categoryApi.getAllCategory().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("categoryRepo", "API Response: " + gson.toJson(response.body()));
                    data.setValue(response.body());
                }
                else{
                    data.setValue(new ApiResponse<>(500, "Error cannot get category", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.d("API call failed", throwable.getMessage());
                data.setValue(new ApiResponse<>(500, throwable.getMessage(), null));
            }

        });
        return data;
    }
}
