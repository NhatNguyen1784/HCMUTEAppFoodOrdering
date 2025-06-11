package vn.hcmute.appfoodorder.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.SliderItem;

public interface SliderApi {
    @GET("slider")
    Call<ApiResponse<List<SliderItem>>> getAllSlider();
}
