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
    private final MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    private final MutableLiveData<String> messageError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> getError = new MutableLiveData<>();

    public CategoryViewModel(){
        categoryRepository = CategoryRepository.getInstance();
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
// tang nay se xu li logic của data lay' tu` repo va day ra view
    public void fetchCategories(){
        categoryRepository.getAllCategory().observeForever(new Observer<ApiResponse<List<Category>>>() {
            @Override
            public void onChanged(ApiResponse<List<Category>> apiResponse) { // apiResponse se chua du lieu moi nhat' ma livedata gui ve
                if(apiResponse == null || apiResponse.getCode() != 200){
                    getError.setValue(true);
                    messageError.setValue("ERROR: " + apiResponse.getMessage());
                }
                else {
                    categoryList.setValue(apiResponse.getResult());
                }
            }
        });
    }
}
