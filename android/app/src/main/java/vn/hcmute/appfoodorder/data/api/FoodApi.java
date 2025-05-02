package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Food;

public interface FoodApi {
    @GET("foods/category/{categoryId}")
    Call<ApiResponse<List<Food>>> getFoodByCategory(@Path("categoryId") Long categoryId);

    @GET("foods/{foodId}")
    Call<ApiResponse<Food>> getFoodById(@Path("foodId") Long foodId);

    @GET("foods/search")
    Call<ApiResponse<List<Food>>> searchFoodByName(@Query("keyword") String keyword);
}
