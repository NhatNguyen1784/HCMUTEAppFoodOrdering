package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.SliderDTO;
import vn.hcmute.appfood.entity.SliderItem;
import vn.hcmute.appfood.repository.SliderItemRepository;
import vn.hcmute.appfood.services.ISliderService;

import java.util.List;

@Service
public class SliderService implements ISliderService {
    @Autowired
    private SliderItemRepository sliderRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<SliderItem> getAllSliders() {
        return sliderRepository.findAll();
    }

    @Override
    public SliderItem addSlider(SliderDTO dto) {
        SliderItem slider = new SliderItem();
        slider.setTitle(dto.getTitle());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imgUrl = cloudinaryService.uploadImage(dto.getImage());
            slider.setImageUrl(imgUrl);
        }

        return sliderRepository.save(slider);
    }

    @Override
    public void deleteSlider(Long id) {
        SliderItem item = sliderRepository.findById(id).orElseThrow();
        if (item.getImageUrl() != null)
            cloudinaryService.deleteImage(item.getImageUrl());
        sliderRepository.delete(item);
    }
}
