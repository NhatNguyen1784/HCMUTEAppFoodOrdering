package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.model.entity.SliderItem;

public class SliderViewModel extends ViewModel {
    private MutableLiveData<List<SliderItem>> sliderItems;

    public SliderViewModel() {
        sliderItems = new MutableLiveData<>();
        loadSliderItems();
    }

    private void loadSliderItems() {
        // Tạo danh sách slider mẫu (sau này có thể lấy từ API)
        List<SliderItem> items = new ArrayList<>();
        items.add(new SliderItem("https://res.cloudinary.com/dk8e9lwe2/image/upload/v1743464200/fstgl9e75ssfusv57rhl.jpg", "Ưu đãi 50% cho đơn đầu tiên!"));
        items.add(new SliderItem("https://res.cloudinary.com/dk8e9lwe2/image/upload/v1743464200/fstgl9e75ssfusv57rhl.jpg", "Miễn phí ship từ 0đ"));
        items.add(new SliderItem("https://res.cloudinary.com/dk8e9lwe2/image/upload/v1743464200/fstgl9e75ssfusv57rhl.jpg", "Đặt món ngay, nhận quà liền tay!"));

        sliderItems.setValue(items); // Gán danh sách slider vào LiveData
    }

    public LiveData<List<SliderItem>> getSliderItems() {
        return sliderItems;
    }
}
