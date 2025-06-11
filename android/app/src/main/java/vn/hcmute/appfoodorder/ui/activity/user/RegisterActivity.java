package vn.hcmute.appfoodorder.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityRegisterBinding;
import vn.hcmute.appfoodorder.model.dto.InforRegisAccount;
import vn.hcmute.appfoodorder.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Khoi tao binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setRegister(registerViewModel);
        binding.setLifecycleOwner(this);

        //on click register button
        onClickRegister();

        //Change login activity
        changeLogin();
    }

    private void onClickRegister() {
        registerViewModel.registerResponse.observe(this, response ->{
            if(response.getCode() == 200){
                Toast.makeText(RegisterActivity.this, "Send OTP", Toast.LENGTH_SHORT).show();
                InforRegisAccount inf = registerViewModel.getInformation();
                if(inf != null){
                    Intent intent = new Intent(RegisterActivity.this, VerifyOtpActivity.class);
                    intent.putExtra("Infor", inf);
                    startActivity(intent);
                    finish();
                }
            }
            else Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
        } );
    }

    private void changeLogin() {
        binding.btnReLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}