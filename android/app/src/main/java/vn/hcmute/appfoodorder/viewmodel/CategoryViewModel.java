package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;
import vn.hcmute.appfoodorder.repository.CategoryRepository;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList;
    private final MutableLiveData<String> messageError;
    private final MutableLiveData<Boolean> getError;

    public CategoryViewModel(){
        categoryRepository = CategoryRepository.getInstance();
        categoryList = categoryRepository.getCategoryList();
        messageError = categoryRepository.getMessageError();
        getError = categoryRepository.getGetError();
    }

    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList; // UI se lay du lieu tu đay
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getGetError() {
        return getError;
    }

    public void fetchCategories(){
        categoryRepository.getAllCategory();
    }

}
