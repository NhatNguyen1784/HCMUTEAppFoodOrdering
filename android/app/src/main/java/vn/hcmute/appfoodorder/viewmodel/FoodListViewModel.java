package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.model.dto.response.FoodWithStarResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.repository.FoodListRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodListViewModel extends ViewModel {
    private final FoodListRepository foodRepository;

    // LiveData cho UI quan sát (bên ngoài chỉ được đọc)
    private final MutableLiveData<List<FoodWithStarResponse>> foodList = new MutableLiveData<>();

    private final MutableLiveData<String> messageError = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private Long currentCategoryId;

    public FoodListViewModel() {
        foodRepository = FoodListRepository.getInstance();
    }

    public void fetchListFood(Long categoryId) {
        currentCategoryId = categoryId;
        isLoading.setValue(true);

        foodRepository.getFoodByCategory(categoryId).observeForever(new Observer<Resource<List<FoodWithStarResponse>>>() {
            @Override
            public void onChanged(Resource<List<FoodWithStarResponse>> listResource) {

                if (listResource.isSuccess()){
                    foodList.setValue(listResource.getData());

                }
                else if (listResource.isError()){
                    messageError.setValue(listResource.getMessage());
                }
                isLoading.setValue(false);
            }
        });
    }

    public void refreshData() {
        if (currentCategoryId != null) {
            fetchListFood(currentCategoryId);
        }
    }

    public MutableLiveData<List<FoodWithStarResponse>> getFoodList() {
        return foodList;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}

