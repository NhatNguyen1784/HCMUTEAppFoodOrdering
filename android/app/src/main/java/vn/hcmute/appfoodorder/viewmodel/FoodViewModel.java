package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.repository.FoodRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodViewModel extends ViewModel {
    private final FoodRepository foodRepository;
    // LiveData cho View quan sát
    private final MutableLiveData<List<Food>> foodList = new MutableLiveData<>();
    private final MutableLiveData<String> messageError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // Dữ liệu gốc để lọc/tìm kiếm
    private List<Food> originalFoodList = new ArrayList<>();
    private Long currentCategoryId;

    public FoodViewModel(){
        foodRepository = FoodRepository.getInstance();
    }

    public void fetchListFood(Long categoryId){
        currentCategoryId = categoryId;
        isLoading.setValue(true);

        foodRepository.getFoodByCategory(categoryId).observeForever(new Observer<Resource<List<Food>>>() {
            @Override
            public void onChanged(Resource<List<Food>> resource) {
                isLoading.setValue(resource.isLoading());

                if(resource.isSuccess() && resource.getData() != null){
                    // luu du lieu goc
                    originalFoodList = resource.getData();
                    foodList.setValue(originalFoodList);

                }
                if (resource.isError()){
                    messageError.setValue(resource.getMessage());
                }
            }
        });
    }

    public void refreshData(){
        if(currentCategoryId != null){
            fetchListFood(currentCategoryId);
        }
    }

    // getter cho liveData
    public MutableLiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
