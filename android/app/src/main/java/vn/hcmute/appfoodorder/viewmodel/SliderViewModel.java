package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.SliderItem;
import vn.hcmute.appfoodorder.repository.SliderRepository;

public class SliderViewModel extends ViewModel {
    private SliderRepository repository;
    private LiveData<ApiResponse<List<SliderItem>>> sliders;

    public SliderViewModel() {
        repository = new SliderRepository();
        sliders = repository.getAllSliderItem();
    }

    public LiveData<ApiResponse<List<SliderItem>>> getSliders() {
        return sliders;
    }

    public void fetchSliders() {
    }
}
