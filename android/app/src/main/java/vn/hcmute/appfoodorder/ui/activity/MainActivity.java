package vn.hcmute.appfoodorder.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.activity.user.LoginActivity;
import vn.hcmute.appfoodorder.ui.fragment.HomeFragment;
import vn.hcmute.appfoodorder.ui.fragment.ProfileFragment;
import vn.hcmute.appfoodorder.ui.fragment.SearchFragment;
import vn.hcmute.appfoodorder.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this); // splash
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        session = new SessionManager(this);
        // mapping
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // set default fragment
        setCurrentFragment(homeFragment);

        // set fragment khi click navigation
        setBottomNavigationView();
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

    private void setBottomNavigationView(){
        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.itemHome){
                setCurrentFragment(homeFragment);
            }
            else if(itemId == R.id.itemSearch){
                setCurrentFragment(searchFragment);
            }
            else if(itemId == R.id.itemProfile){
                if(session.isLogin()){
                    setCurrentFragment(profileFragment);
                }
                else{
                    // Show dialog required login account
                    showLoginDialog();
                }
            }
            else if(itemId == R.id.itemCart){
                if(session.isLogin()){

                }
                else{
                    // Show dialog required login account
                    showLoginDialog();
                }
            }
            return true;
        });
    }

    //Dialog show
    private void showLoginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Required")
                .setMessage("You need to log in to view your profile.")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}