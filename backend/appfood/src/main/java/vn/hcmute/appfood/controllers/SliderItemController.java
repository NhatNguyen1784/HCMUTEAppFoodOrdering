package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.ApiResponse;
import vn.hcmute.appfood.dto.SliderDTO;
import vn.hcmute.appfood.entity.SliderItem;
import vn.hcmute.appfood.services.Impl.SliderService;
import java.util.List;

@RestController
@RequestMapping("/api/slider")
public class SliderItemController {
    @Autowired
    private SliderService sliderService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<SliderItem> sliders = sliderService.getAllSliders();
        return ResponseEntity.ok(ApiResponse.success("Get all sliders successfully", sliders));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSlider(@ModelAttribute SliderDTO dto) {
        SliderItem saved = sliderService.addSlider(dto);
        return ResponseEntity.ok(ApiResponse.success("Add slider successfully", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        sliderService.deleteSlider(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }

}
