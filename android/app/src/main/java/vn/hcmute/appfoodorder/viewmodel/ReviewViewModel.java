package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.request.ReviewRequest;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;
import vn.hcmute.appfoodorder.repository.ReviewRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class ReviewViewModel extends ViewModel {
    private final ReviewRepository reviewRepository;

    private final MutableLiveData<ReviewResponse> reviewLiveData = new MutableLiveData<>();

    private final MutableLiveData<ReviewListResponse> listReviewLiveData = new MutableLiveData<>();

    public ReviewViewModel() {
        this.reviewRepository = ReviewRepository.getInstance();
    }

    public void submitReview(ReviewRequest request){
        reviewRepository.submitReview(request).observeForever(new Observer<Resource<ReviewResponse>>() {
            @Override
            public void onChanged(Resource<ReviewResponse> result) {
                reviewLiveData.setValue(result.getData());
            }
        });
    }

    public void getReviewByFoodName(String foodName){
        reviewRepository.getReviewByFoodName(foodName).observeForever(new Observer<Resource<ReviewListResponse>>() {
            @Override
            public void onChanged(Resource<ReviewListResponse> result) {
                listReviewLiveData.setValue(result.getData());
            }
        });
    }

    public MutableLiveData<ReviewResponse> getReviewLiveData() {
        return reviewLiveData;
    }

    public MutableLiveData<ReviewListResponse> getListReviewLiveData() {
        return listReviewLiveData;
    }
}
