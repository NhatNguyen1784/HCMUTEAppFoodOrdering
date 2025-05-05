package vn.hcmute.appfoodorder.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaos.view.PinView;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.ResetPasswordResponse;
import vn.hcmute.appfoodorder.utils.Resource;
import vn.hcmute.appfoodorder.viewmodel.ResetPassViewModel;

public class VerifyOtpResetPassActivity extends AppCompatActivity {

    private TextView tvResendOtp;
    private PinView pinOtpCode;
    private Button btnVerifyCode;
    private ResetPassViewModel resetViewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp_reset_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy dữ liệu từ Intent và xử lý cho reset password
        ResetPasswordResponse resetPassResponse = (ResetPasswordResponse) getIntent().getSerializableExtra("resetPass");
        if (resetPassResponse == null) {
            Toast.makeText(this, "No data received!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ResetPassActivity.class));
            finish();
        }
        String email = resetPassResponse.getEmail();
        int expireIn = resetPassResponse.getExpiresIn();
        anhxa();
        setupViewModel();
        setupListener(email, expireIn);
    }

    private void setupListener(String email, int expireIn) {
        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinOtpCode.getText().toString().trim();
                resetViewmodel.verifyOtpResetPassword(email, code);
            }
        });

        resetViewmodel.getVerifyOtpResult().observe(this, new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> result) {
                if(result.isSuccess()){
                    // chuyen toi man hinh dang nhap
                    Intent intent = new Intent(VerifyOtpResetPassActivity.this, LoginActivity.class);
                    intent.putExtra("message", "Password has been reset successfully. Please log in again.");
                    startActivity(intent);
                    finish();
                }
                else if (result.isError()){
                    Toast.makeText(getApplicationContext(), "The OTP is incorrect or has expired. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetViewmodel.resendOtpResetPassword(email);
            }
        });

        resetViewmodel.getResendOtpResult().observe(this, new Observer<Resource<ResetPasswordResponse>>() {
            @Override
            public void onChanged(Resource<ResetPasswordResponse> result) {
                if(result.isSuccess()){
                    Toast.makeText(VerifyOtpResetPassActivity.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
                }
                else if (result.isError()) {
                    Toast.makeText(VerifyOtpResetPassActivity.this, "Failed to resend OTP: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupViewModel() {
        resetViewmodel = new ViewModelProvider(this).get(ResetPassViewModel.class);
    }

    private void anhxa() {
        tvResendOtp = findViewById(R.id.tvResendOtpResetPass);
        pinOtpCode = findViewById(R.id.pinvOtpCodeResetPass);
        btnVerifyCode = findViewById(R.id.btnVerifyOtpResetPass);
    }
}