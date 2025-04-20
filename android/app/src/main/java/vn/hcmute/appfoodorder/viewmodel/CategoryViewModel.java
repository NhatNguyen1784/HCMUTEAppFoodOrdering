package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;
import vn.hcmute.appfoodorder.repository.CategoryRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private final MutableLiveData<String> messageError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public CategoryViewModel(){
        categoryRepository = CategoryRepository.getInstance();
    }

    public void fetchCategories(){
        categoryRepository.getAllCategory().observeForever(new Observer<Resource<List<Category>>>() {
            @Override
            public void onChanged(Resource<List<Category>> resource) {
                if (resource.isSuccess() && resource.getData() != null){
                    categoryList.setValue(resource.getData());
                }

                if (resource.isError()){
                    messageError.setValue(resource.getMessage());
                }
            }
        });
    }

    // getter
    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList; // UI se lay du lieu tu đay
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
