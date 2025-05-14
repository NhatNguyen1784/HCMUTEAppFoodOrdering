package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.List;

import vn.hcmute.appfoodorder.model.dto.request.ReviewRequest;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;
import vn.hcmute.appfoodorder.repository.ReviewRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class ReviewViewModel extends ViewModel {
    private final ReviewRepository reviewRepository;

    private final MutableLiveData<ReviewResponse> reviewLiveData = new MutableLiveData<>();

    private final MutableLiveData<ReviewListResponse> listReviewLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> messageError = new MutableLiveData<>();
    private final MutableLiveData<String> messageSuccess = new MutableLiveData<>();

    public ReviewViewModel() {
        this.reviewRepository = ReviewRepository.getInstance();
    }

    public void submitReview(ReviewRequest request, List<File> imageFiles){
        reviewRepository.submitReview(request, imageFiles).observeForever(new Observer<Resource<ReviewResponse>>() {
            @Override
            public void onChanged(Resource<ReviewResponse> result) {
                if (result.isSuccess()){
                    reviewLiveData.setValue(result.getData());
                    messageSuccess.setValue(result.getMessage());
                }
                else if (result.isError()){
                    messageError.setValue(result.getMessage());
                }
            }

        });
    }

    public void getReviewByFoodId(Long foodId){
        reviewRepository.getReviewByFoodName(foodId).observeForever(new Observer<Resource<ReviewListResponse>>() {
            @Override
            public void onChanged(Resource<ReviewListResponse> result) {
                if(result.isSuccess()){
                    listReviewLiveData.setValue(result.getData());
                }
                else {
                    messageError.setValue(result.getMessage());
                }
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<ReviewResponse> getReviewLiveData() {
        return reviewLiveData;
    }

    public MutableLiveData<String> getMessageSuccess() {
        return messageSuccess;
    }

    public MutableLiveData<ReviewListResponse> getListReviewLiveData() {
        return listReviewLiveData;
    }
}
