package vn.hcmute.appfoodorder.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.appfoodorder.data.api.SliderApi;
import vn.hcmute.appfoodorder.data.network.RetrofitClient;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.SliderItem;

public class SliderRepository {
    private SliderApi sliderApi;

    public SliderRepository() {
        sliderApi = RetrofitClient.getRetrofit().create(SliderApi.class);
    }

    public LiveData<ApiResponse<List<SliderItem>>> getAllSliderItem(){
        MutableLiveData<ApiResponse<List<SliderItem>>> sliderItems = new MutableLiveData<>();
        sliderApi.getAllSlider().enqueue(new Callback<ApiResponse<List<SliderItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<SliderItem>>> call, Response<ApiResponse<List<SliderItem>>> response) {
                if(response.isSuccessful() && response.body()!= null){
                    List<SliderItem> list = response.body().getResult();
                    if (list == null || list.isEmpty()) {
                        // Dùng default nếu backend không có slider nào
                        list = getDefaultSliders();
                        sliderItems.setValue(new ApiResponse<>(200,"Load empty, using defaults", list));
                    } else {
                        sliderItems.setValue(new ApiResponse<>(200, response.body().getMessage(), list));
                    }
                }
                else {
                    Log.d("Get Slider", "Load fail");
                    sliderItems.setValue(new ApiResponse<>(200, "Load failed, using defaults", getDefaultSliders()));
                }
            }



            @Override
            public void onFailure(Call<ApiResponse<List<SliderItem>>> call, Throwable throwable) {
                Log.e("SliderRepository", "Failed: " + throwable.getMessage());
                sliderItems.postValue(new ApiResponse<>(500, "Error: "+ throwable.getMessage(), null));
            }
        });
        return sliderItems;
    }
    private List<SliderItem> getDefaultSliders() {
        List<SliderItem> items = new ArrayList<>();
        items.add(new SliderItem("https://res.cloudinary.com/demec8nev/image/upload/v1746148080/aa52c7oynbbxy6fs9uki.jpg", "Ưu đãi 50% cho đơn đầu tiên!"));
        items.add(new SliderItem("https://res.cloudinary.com/dk8e9lwe2/image/upload/v1743464200/fstgl9e75ssfusv57rhl.jpg", "Miễn phí ship từ 0đ"));
        items.add(new SliderItem("https://res.cloudinary.com/dk8e9lwe2/image/upload/v1743464200/fstgl9e75ssfusv57rhl.jpg", "Đặt món ngay, nhận quà liền tay!"));
        return items;
    }
}
