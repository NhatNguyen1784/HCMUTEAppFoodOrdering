package vn.hcmute.appfoodorder.viewmodel;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.utils.SessionManager;

public class ProfileViewModel extends ViewModel {
    private SessionManager session;
    private MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();
    private MutableLiveData<UserResponse> userInf = new MutableLiveData<>();

    public void init(Context context) {
        session = new SessionManager(context);
        UserResponse u = session.getUserInfor();
        userInf.setValue(u);
    }
    public LiveData<Boolean> getLogoutSuccess() {
        return logoutSuccess;
    }
    public void logoutAccount(Context context){
        if(session.isLogin()){
            session.cleanSesion();
            // Đánh dau logout thanh cong
            logoutSuccess.setValue(true);
        }
    }

    public LiveData<UserResponse> getUserInfor(){
        return userInf;
    }
}