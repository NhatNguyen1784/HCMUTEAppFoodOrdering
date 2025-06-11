package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;

public interface ReviewApi {
    @Multipart
    @POST("reviews/submit")
    Call<ApiResponse<ReviewResponse>> submitReview(
            @Header("Authorization") String token,
            @Part("review") RequestBody requestJson, // JSON của request
            @Part List<MultipartBody.Part> images); // ảnh kèm theo nếu có

    @GET("reviews/product")
    Call<ApiResponse<ReviewListResponse>> getReviewProduct(@Query("foodId") Long foodId);
}
