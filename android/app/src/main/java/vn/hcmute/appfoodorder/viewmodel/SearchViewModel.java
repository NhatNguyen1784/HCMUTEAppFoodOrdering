package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.repository.SearchFoodRepository;

public class SearchViewModel extends ViewModel {
    private SearchFoodRepository repository;
    private final MutableLiveData<List<Food>> searchResults = new MutableLiveData<>();

    public SearchViewModel() {
        repository = new SearchFoodRepository();
    }

    public LiveData<List<Food>> getSearchResults() {
        return searchResults;
    }

    public void searchFoods(String keyword) {
        repository.searchFoodByName(keyword).observeForever(apiResponse -> {
            if (apiResponse.getResult() != null) {
                searchResults.setValue(apiResponse.getResult());
            } else {
                searchResults.setValue(new ArrayList<>()); // hoặc giữ nguyên danh sách cũ
            }
        });
    }

}