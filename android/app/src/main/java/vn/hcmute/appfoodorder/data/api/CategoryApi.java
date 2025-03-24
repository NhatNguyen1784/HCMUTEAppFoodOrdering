package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;

public interface CategoryApi {
    @GET("category")
    Call<ApiResponse<List<Category>>> getAllCategory();
}
