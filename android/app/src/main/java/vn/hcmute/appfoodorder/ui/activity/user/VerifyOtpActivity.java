package vn.hcmute.appfoodorder.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.databinding.ActivityVerifyOtpBinding;
import vn.hcmute.appfoodorder.model.dto.InformationRegisterAccount;
import vn.hcmute.appfoodorder.viewmodel.VerifyOtpViewModel;

public class VerifyOtpActivity extends AppCompatActivity {

    private VerifyOtpViewModel viewModel;
    private ActivityVerifyOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);

        viewModel = new ViewModelProvider(this).get(VerifyOtpViewModel.class);
        binding.setVerify(viewModel);
        binding.setLifecycleOwner(this);

        InformationRegisterAccount in = (InformationRegisterAccount)getIntent().getSerializableExtra("Infor");
        if(in != null){
            viewModel.setInf(in);
            Toast.makeText(this, "Email: "+ viewModel.getInf().getEmail(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No data received!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
        verifyOtpRegister();
    }

    private void verifyOtpRegister() {
        viewModel.verifyOtp.observe(this, response -> {
            if(response.getCode() == 200){
                Toast.makeText(this, "Register successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(VerifyOtpActivity.this, LoginActivity.class));
                finish();
            }
            else Toast.makeText(this, "Error: " + response.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}