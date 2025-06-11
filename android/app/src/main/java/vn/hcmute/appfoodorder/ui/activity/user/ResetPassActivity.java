package vn.hcmute.appfoodorder.ui.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.ResetPasswordResponse;
import vn.hcmute.appfoodorder.utils.Resource;
import vn.hcmute.appfoodorder.viewmodel.ResetPassViewModel;

public class ResetPassActivity extends AppCompatActivity {

    private ResetPassViewModel resetPassViewModel;
    private TextInputLayout layoutEmail, layoutPassword, layoutConfirmPassword;
    private TextInputEditText edtEmail, edtPass, edtConfirmPass;
    private Button btnResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_pass);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhxa();
        setupViewModel();
        setupValidation();
        setupListener();
    }

    @SuppressLint("WrongViewCast")
    private void anhxa() {
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);

        edtEmail = findViewById(R.id.edtResetEmail);
        edtPass = findViewById(R.id.edtResetPassword);
        edtConfirmPass = findViewById(R.id.edtConfirmPassword);
        btnResetPass = findViewById(R.id.btnResetPassword);
    }

    private void setupViewModel() {
        resetPassViewModel = new ViewModelProvider(this).get(ResetPassViewModel.class);
    }

    private void setupValidation() {
        edtEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    layoutEmail.setError("Email không hợp lệ");
                } else {
                    layoutEmail.setError(null);
                }
            }
        });

        edtPass.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    layoutPassword.setError("Mật khẩu phải từ 6 ký tự");
                } else {
                    layoutPassword.setError(null);
                }
            }
        });

        edtConfirmPass.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(edtPass.getText().toString())) {
                    layoutConfirmPassword.setError("Mật khẩu không khớp");
                } else {
                    layoutConfirmPassword.setError(null);
                }
            }
        });
    }

    private void setupListener() {
        btnResetPass.setOnClickListener(view -> {
            if (isValidInput()) {
                String email = edtEmail.getText().toString().trim();
                String newPass = edtPass.getText().toString().trim();
                resetPassViewModel.requestResetPassword(email, newPass);

            }
        });

        resetPassViewModel.getResetPasswordResult().observe(this, new Observer<Resource<ResetPasswordResponse>>() {
            @Override
            public void onChanged(Resource<ResetPasswordResponse> result) {
                if (result.isSuccess()) {
                    ResetPasswordResponse response = result.getData();
                    Intent intent = new Intent(ResetPassActivity.this, VerifyOtpResetPassActivity.class);
                    intent.putExtra("resetPass", response);
                    startActivity(intent);
                }
                else if (result.isError()){
                    Toast.makeText(ResetPassActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isValidInput() {
        boolean valid = true;

        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("Email không hợp lệ");
            valid = false;
        }

        if (pass.length() < 6) {
            layoutPassword.setError("Mật khẩu phải từ 6 ký tự");
            valid = false;
        }

        if (!pass.equals(confirmPass)) {
            layoutConfirmPassword.setError("Mật khẩu không khớp");
            valid = false;
        }

        return valid;
    }

    // Helper class để viết gọn TextWatcher
    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
