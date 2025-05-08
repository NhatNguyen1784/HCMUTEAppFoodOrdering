package vn.hcmute.appfoodorder.utils;

import static vn.hcmute.appfoodorder.utils.Constants.KEY_IS_FIRST;
import static vn.hcmute.appfoodorder.utils.Constants.PREF_FIRST;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstTimeManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public FirstTimeManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FIRST, Context.MODE_PRIVATE );
        editor = sharedPreferences.edit();
    }

    //Save is first
    public void saveIsFirst(boolean isFirst){
        editor.putBoolean(KEY_IS_FIRST, isFirst);
        editor.apply();
    }

    //Clear
    public void clear(){
        editor.clear();
        editor.apply();
    }

    public boolean isFirst() {
        return sharedPreferences.getBoolean(KEY_IS_FIRST, false);
    }
}
