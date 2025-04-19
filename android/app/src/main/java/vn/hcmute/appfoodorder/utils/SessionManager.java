package vn.hcmute.appfoodorder.utils;

import static vn.hcmute.appfoodorder.utils.Constants.*;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;//editor.apply() không đồng bộ, editor.commit() đồng bộ có giá trị boolean trả về

    public SessionManager(Context context) {//Khởi tạo
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

    public boolean isLogin(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false); //Default isLogin = false
    }
}
