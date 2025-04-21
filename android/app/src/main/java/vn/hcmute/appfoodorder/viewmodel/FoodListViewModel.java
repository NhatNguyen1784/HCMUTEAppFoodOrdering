package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.repository.FoodListRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodListViewModel extends ViewModel {
    private final FoodListRepository foodRepository;

    // LiveData cho UI quan sát (bên ngoài chỉ được đọc)
    private final MutableLiveData<List<Food>> _foodList = new MutableLiveData<>();
    private LiveData<List<Food>> foodList = _foodList;

    private final MutableLiveData<String> _messageError = new MutableLiveData<>();
    private LiveData<String> messageError = _messageError;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    private LiveData<Boolean> isLoading = _isLoading;

    // Dữ liệu gốc để lọc/tìm kiếm
    private List<Food> originalFoodList = new ArrayList<>();
    private Long currentCategoryId;

    public FoodListViewModel() {
        foodRepository = FoodListRepository.getInstance();
    }

    public void fetchListFood(Long categoryId) {
        currentCategoryId = categoryId;
        _isLoading.setValue(true);

        foodRepository.getFoodByCategory(categoryId).observeForever(new Observer<Resource<List<Food>>>() {
            @Override
            public void onChanged(Resource<List<Food>> resource) {
                _isLoading.setValue(resource.isLoading());

                if (resource.isSuccess() && resource.getData() != null) {
                    originalFoodList = resource.getData();
                    _foodList.setValue(originalFoodList);
                }

                if (resource.isError()) {
                    _messageError.setValue(resource.getMessage());
                }
            }
        });
    }

    public void refreshData() {
        if (currentCategoryId != null) {
            fetchListFood(currentCategoryId);
        }
    }


    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public LiveData<String> getMessageError() {
        return messageError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}

