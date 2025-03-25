package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.hcmute.appfood.services.Impl.FoodImageService;
import vn.hcmute.appfood.services.Impl.FoodService;

@RestController
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodImageService foodImageService;
    
}
