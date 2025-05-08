package vn.hcmute.appfoodorder.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.dto.request.ReviewRequest;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;

public interface ReviewApi {
    @POST("reviews/submit")
    Call<ApiResponse<ReviewResponse>> submitReview(@Body ReviewRequest request);

    @GET("reviews/product")
    Call<ApiResponse<ReviewListResponse>> getReviewProduct(@Query("foodName") String foodName);
}
