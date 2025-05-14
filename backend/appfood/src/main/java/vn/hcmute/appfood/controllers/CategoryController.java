package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.response.ApiResponse;
import vn.hcmute.appfood.dto.CategoryDTO;
import vn.hcmute.appfood.entity.Category;
import vn.hcmute.appfood.services.Impl.CategoryService;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories") // http://localhost:8081/api/categories
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        return new ResponseEntity<>(new ApiResponse(200, "All category", categoryService.getAllCategory()), HttpStatus.OK);
    }

    // http://localhost:8081/api/categories/getByName
    @GetMapping("/getByName")
    public ResponseEntity<?> getCategoryByName(@RequestParam("categoryName") String categoryName) {
        return new ResponseEntity<>(new ApiResponse<>(200, "category", categoryService.getCategoryByName(categoryName)), HttpStatus.OK);
    }

    // http://localhost:8081/api/categories/{cateId}
    @GetMapping("/{cateId}")
    public ResponseEntity<?> getCategoryByName(@PathVariable("cateId") Long cateId) {
        Optional<Category> category = categoryService.getCategoryById(cateId);
        if (category.isPresent()) {
            return new ResponseEntity<>(new ApiResponse<>(200, "category", categoryService.getCategoryById(cateId)), HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category not found with id: " + cateId);
    }

    // http://localhost:8081/api/categories/add
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@ModelAttribute CategoryDTO cateDTO) {
        Optional<Category> category = categoryService.getCategoryByName(cateDTO.getCategoryName());
        if (category.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Add category failed, Category already exists");
        }

        return new ResponseEntity<>(new ApiResponse<>(200, "Add category successfully", categoryService.saveCategory(cateDTO)), HttpStatus.OK);
    }

    // http://localhost:8081/api/categories/update/{}
    @PutMapping("/update/{cateId}")
    public ResponseEntity<?> updateCategory(@PathVariable("cateId") Long cateId, @ModelAttribute CategoryDTO cateDTO) {
        Optional<Category> category = categoryService.getCategoryById(cateId);
        if (category.isPresent()) {
            return new ResponseEntity<>(new ApiResponse<>(200, "Update category successfully", categoryService.updateCategory(cateId, cateDTO)), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update category failed, Category not found with id: " + cateId);
    }
}
