package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.File;

import vn.hcmute.appfoodorder.model.dto.request.UserUpdateDTO;
import vn.hcmute.appfoodorder.repository.AuthRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class UpdateProfileViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Resource<String>> updateResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<String>> messageError = new MutableLiveData<>();
    public UpdateProfileViewModel() {
        this.authRepository = AuthRepository.getInstance();
    }

    public void updateProfile(UserUpdateDTO dto, File image){
        authRepository.updateProfile(dto, image).observeForever(new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> objectResource) {
                if (objectResource.isSuccess()){
                    updateResult.setValue(Resource.success("Update thành công"));
                }
                else if (objectResource.isError()){
                    updateResult.setValue(Resource.error(objectResource.getMessage(), null));
                    messageError.setValue(Resource.error(objectResource.getMessage(), null));
                }
            }
        });
    }

    public LiveData<Resource<String>> getMessageError() {
        return messageError;
    }

    public LiveData<Resource<String>> getUpdateResult() {
        return updateResult;
    }
}
