package vn.hcmute.appfoodorder.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.fragment.CartFragment;
import vn.hcmute.appfoodorder.ui.activity.user.LoginActivity;
import vn.hcmute.appfoodorder.ui.fragment.HomeFragment;
import vn.hcmute.appfoodorder.ui.fragment.ProfileFragment;
import vn.hcmute.appfoodorder.ui.fragment.SearchFragment;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.CartViewModel;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private HomeFragment homeFragment = new HomeFragment();
	private CartFragment cartFragment = new CartFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private SessionManager session;
    private CartViewModel cartViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this); // splash
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);

        session = new SessionManager(this);
        // mapping
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // set default fragment
        setCurrentFragment(homeFragment);

        // set fragment khi click navigation
        setBottomNavigationView();

        // getNumberCartItem
        getNumberCartItem();
    }

    private void getNumberCartItem() {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        if(session.isLogin()){
            //String email = session.getUserInfor().getEmail();
            String authHeader = new SessionManager(MainActivity.this).getAuthHeader();
            cartViewModel.getMyCart(authHeader);
        }

        cartViewModel.getCartLiveData().observe(this, cart -> {
            if (cart != null && cart.getCartDetails() != null) {
                int itemCount = cart.getCartDetails().size();
                updateCartBadge(itemCount);
            } else {
                updateCartBadge(0);
            }
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }


    //update so luong trong gio hang
    private void updateCartBadge(int count) {
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.itemCart);
        if (count > 0) {
            badge.setVisible(true);
            badge.setNumber(count);
        } else {
            badge.setVisible(false);
            badge.clearNumber();
        }
    }

    private void setBottomNavigationView(){
        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.itemHome){
                setCurrentFragment(homeFragment);
            }
            else if( itemId == R.id.itemSearch){
                if(session.isLogin()){
                    setCurrentFragment(searchFragment);
                }
                else{
                    // Show dialog required login account
                    showLoginDialog();
                }
            }
            else if(itemId == R.id.itemCart){
                if(session.isLogin()){
                    setCurrentFragment(cartFragment);
                }
                else{
                    // Show dialog required login account
                    showLoginDialog();
                }
            }
            else if(itemId == R.id.itemNotification){
                if(session.isLogin()){
                    //setCurrentFragment(cartFragment);
                }
                else{
                    // Show dialog required login account
                    showLoginDialog();
                }
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
            return true;
        });
    }

    //Change fragment
    public void  showFragment(Fragment fragment){
        setCurrentFragment(fragment);
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