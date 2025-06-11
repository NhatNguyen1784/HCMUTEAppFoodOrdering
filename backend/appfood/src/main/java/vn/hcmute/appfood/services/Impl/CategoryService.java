package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hcmute.appfood.dto.CategoryDTO;
import vn.hcmute.appfood.entity.Category;
import vn.hcmute.appfood.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // lay tat ca category
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    // lay theo id
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    // lay theo ten
    public Optional<Category> getCategoryByName(String name){
        return categoryRepository.findByCategoryName(name);
    }

    // Create category
    public Category saveCategory(CategoryDTO cateDTO){
        Category cate = new Category();
        cate.setCategoryName(cateDTO.getCategoryName());

        // neu khong up anh thi luu
        Category saveCate = categoryRepository.save(cate);

        // kiem tra neu co up anh thi luu anh
        if(cateDTO.getImage() != null && !cateDTO.getImage().isEmpty()){
            MultipartFile img = cateDTO.getImage();
            String imgUrl = cloudinaryService.uploadImage(img);
            cate.setImageUrl(imgUrl);
            saveCate = categoryRepository.save(cate);
        }

        return saveCate;
    }

    public Category updateCategory(Long id, CategoryDTO cateDTO){
        Category cate = categoryRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        cate.setCategoryName(cateDTO.getCategoryName());

        Category saveCate = categoryRepository.save(cate);

        // neu co anh thi update anh moi
        if(cateDTO.getImage() != null && !cateDTO.getImage().isEmpty()){

            // xoa anh cu tren cloud
            String oldImgUrl = cate.getImageUrl();
            if(oldImgUrl != null && !oldImgUrl.isEmpty()){
                cloudinaryService.deleteImage(oldImgUrl);
            }

            // luu anh moi tren cloud va luu trong db
            MultipartFile img = cateDTO.getImage();

            String newImgUrl = cloudinaryService.uploadImage(img);
            cate.setImageUrl(newImgUrl);
            saveCate = categoryRepository.save(cate);
        }

        return saveCate;
    }

    public void deleteCategoryById(Long id){
        Optional<Category> cate = categoryRepository.findById(id);

        if(cate.isPresent()){
            String imgUrl = cate.get().getImageUrl();
            cloudinaryService.deleteImage(imgUrl); // xoa anh tren cloud
            categoryRepository.delete(cate.get()); // xoa category trong db
        }
    }
}
