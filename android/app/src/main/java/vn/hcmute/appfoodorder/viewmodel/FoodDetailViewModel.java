package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.repository.FoodDetailRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class FoodDetailViewModel extends ViewModel {
    private final FoodDetailRepository foodDetailRepository;

    // live Data
    private final MutableLiveData<Food> _food = new MutableLiveData<>();
    private final LiveData<Food> food = _food;
    private final MutableLiveData<String> _messageError = new MutableLiveData<>();
    private final LiveData<String> messageError = _messageError;
    public FoodDetailViewModel() {
        this.foodDetailRepository = FoodDetailRepository.getInstance();
    }

    public void fetchFood(Long foodId){
        foodDetailRepository.getFoodById(foodId).observeForever(new Observer<Resource<Food>>() {
            @Override
            public void onChanged(Resource<Food> foodResource) {
                // kiem tra trang thai cua resource
                if(foodResource.isSuccess() && foodResource.getData() != null){
                    _food.setValue(foodResource.getData());
                }

                if (foodResource.isError()){
                    _messageError.setValue(foodResource.getMessage());
                }
            }
        });
    }

    public LiveData<Food> getFood() {
        return food;
    }

    public LiveData<String> getMessageError() {
        return messageError;
    }
}
