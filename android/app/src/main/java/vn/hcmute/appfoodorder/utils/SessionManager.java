package vn.hcmute.appfoodorder.utils;

import static vn.hcmute.appfoodorder.utils.Constants.*;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.hcmute.appfoodorder.model.dto.InforRegisAccount;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;//editor.apply() không đồng bộ, editor.commit() đồng bộ có giá trị boolean trả về

    public SessionManager(Context context) {
        //Khởi tạo
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginSession(String email){
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    //Clear
    public void cleanSesion(){
        editor.clear();
        editor.apply();
    }

    //Check login
    public boolean isLogin(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false); //Default isLogin = false
    }

    //Save user infor
    public void saveUserInfor(UserResponse user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER_INFOR, json);
        editor.apply();
    }

    //Get user infor
    public UserResponse getUserInfor(){
        String userInfJson = sharedPreferences.getString(KEY_USER_INFOR, null);
        if(userInfJson == null) return null;
        return new Gson().fromJson(userInfJson, UserResponse.class);
    }
}
