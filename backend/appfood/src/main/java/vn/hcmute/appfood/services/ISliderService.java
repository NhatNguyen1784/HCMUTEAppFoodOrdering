package vn.hcmute.appfood.services;

import vn.hcmute.appfood.dto.SliderDTO;
import vn.hcmute.appfood.entity.SliderItem;

import java.util.List;

public interface ISliderService {
    List<SliderItem> getAllSliders();

    SliderItem addSlider(SliderDTO dto);

    void deleteSlider(Long id);
}
