package vn.hcmute.appfoodorder.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityLoginBinding;
import vn.hcmute.appfoodorder.ui.activity.MainActivity;
import vn.hcmute.appfoodorder.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Khởi tạo data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // Khởi tạo ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setAuth(loginViewModel) ;// Gán ViewModel vào layout
        binding.setLifecycleOwner(this); //Cho phép binding tự động cập nhật

        // Quan sát LiveData để phản hồi kết quả đăng nhập
        observeLoginResult();

        //Chuyển sang activity đăng kí
        changeRegister();

        //Chuyển sang activity forgot password
        changeForgotPassword();
    }

    private void changeForgotPassword() {
        binding.tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(LoginActivity.this, )
                startActivity(intent);
                finish();

                 */
            }
        });
    }

    //Chuyển sang Register Activity
    private void changeRegister() {
        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Quan sát LiveData để phản hồi kết quả đăng nhập
    private void observeLoginResult() {
        loginViewModel.loginResponse.observe(this, response -> {
            if (response != null) {
                if (response.getCode() == 200) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Login failed: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}