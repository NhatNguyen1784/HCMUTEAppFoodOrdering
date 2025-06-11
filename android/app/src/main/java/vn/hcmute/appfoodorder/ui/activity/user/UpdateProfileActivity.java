package vn.hcmute.appfoodorder.ui.activity.user;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.request.UserUpdateDTO;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.ui.activity.ReviewActivity;
import vn.hcmute.appfoodorder.utils.Constants;
import vn.hcmute.appfoodorder.utils.ImagePickerUtils;
import vn.hcmute.appfoodorder.utils.Resource;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.ReviewViewModel;
import vn.hcmute.appfoodorder.viewmodel.UpdateProfileViewModel;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText edtFullname, edtphone, edtaddress, email;
    private CircleImageView imgProfile;
    private ImageView iconCamera;
    private Button btnUpdate;
    private SessionManager sessionManager;
    private File imgFile;
    private UserUpdateDTO dto;
    private UpdateProfileViewModel updateProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);
        anhxa();
        setupRealTimeValidation();
        setupUpdateProfile();
        setupListener();
    }

    private void setupRealTimeValidation() {
        edtFullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    edtFullname.setError("Họ tên không được để trống");
                } else {
                    edtFullname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("^\\d{10}$")) {
                    edtphone.setError("Số điện thoại phải đủ 10 chữ số");
                } else {
                    edtphone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    edtaddress.setError("Địa chỉ không được để trống");
                } else {
                    edtaddress.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dto.setFullName(edtFullname.getText().toString().trim());
                dto.setPhone(edtphone.getText().toString().trim());
                dto.setAddress(edtaddress.getText().toString().trim());
                sessionManager = new SessionManager(UpdateProfileActivity.this);
                String token = sessionManager.getAuthHeader();
                updateProfileViewModel.updateProfile(token, dto, imgFile);
            }
        });

        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST){
                if (data == null) {
                    Log.e("DEBUG", "Intent data is null!");
                    Toast.makeText(this, "Không nhận được ảnh đã chọn", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri selectedImageUri = data.getData();
                Log.d("URI", "URI nhận được: " + selectedImageUri);

                if(selectedImageUri != null){

                    addImage(selectedImageUri);
                }
                else {
                    Log.e("DEBUG", "selectedImageUri == null");
                }
            } else if (requestCode == Constants.CAMERA_REQUEST_CODE) {
                File imageFile = ImagePickerUtils.getCurrentPhotoFile();
                if(imageFile != null && imageFile.exists()){
                    Uri imageUri = Uri.fromFile(imageFile);

                    addImage(imageUri);
                }
            }
        }
        else {
            Log.e("DEBUG", "Result not OK");
        }
    }

    private void addImage(Uri imageUri) {

        imgProfile.setImageURI(imageUri);

        // Convert Uri to File
        imgFile = uriToFile(imageUri);

    }

    private File uriToFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("image", ".jpg", getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupUpdateProfile() {
        // lay du lieu tu man hinh truoc
        Intent intent = getIntent();

        String fullName = intent.getStringExtra("fullName");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String urlImage = intent.getStringExtra("urlImage");

        edtFullname.setText(fullName);
        edtphone.setText(phone);
        edtaddress.setText(address);

        // Hiển thị ảnh từ urlImage sử dụng Glide
        if (urlImage != null && !urlImage.isEmpty()) {
            Glide.with(this)
                    .load(urlImage)
                    .placeholder(R.drawable.icon_default_avatar)
                    .error(R.drawable.icon_default_avatar)
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.icon_default_avatar);
        }

        sessionManager = new SessionManager(this);
        updateProfileViewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);

        dto = new UserUpdateDTO();


        updateProfileViewModel.getUpdateResult().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                if (stringResource.isSuccess()){
                    Toast.makeText(getApplicationContext(), "Update Thanh công", Toast.LENGTH_SHORT).show();
                    //Thanh công lưu lại User trong shared reference xử lý tiếp ở đay
                    finish();
                }

            }
        });

        updateProfileViewModel.getMessageError().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> stringResource) {
                if (!stringResource.getMessage().isEmpty()){
                    Toast.makeText(getApplicationContext(), stringResource.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void showImagePickerDialog() {
        String[] options = {"Chụp ảnh", "Chọn ảnh từ thư viện"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn hình ảnh")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            // chup anh
                            checkPermissionAndDo(Manifest.permission.CAMERA, Constants.REQUEST_CAMERA_PERMISSION, new Runnable() {
                                @Override
                                public void run() {
                                    ImagePickerUtils.openCamera(UpdateProfileActivity.this);
                                }
                            });

                        } else {
                            // chon anh tu thu vien
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                checkPermissionAndDo(Manifest.permission.READ_MEDIA_IMAGES, Constants.REQUEST_GALLERY_PERMISSION, new Runnable() {
                                    @Override
                                    public void run() {
                                        ImagePickerUtils.openGallery(UpdateProfileActivity.this);
                                    }
                                });
                            }
                            else {
                                checkPermissionAndDo(Manifest.permission.READ_EXTERNAL_STORAGE, Constants.REQUEST_GALLERY_PERMISSION, new Runnable() {
                                    @Override
                                    public void run() {
                                        ImagePickerUtils.openGallery(UpdateProfileActivity.this);
                                    }
                                });
                            }

                        }
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePickerUtils.openCamera(this);
            } else {
                Toast.makeText(this, "Cần cấp quyền CAMERA để chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePickerUtils.openGallery(this);
            } else {
                Toast.makeText(this, "Cần cấp quyền để chọn ảnh từ thư viện", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermissionAndDo(String permission, int requestCode, Runnable onGranted) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            onGranted.run();
        }
    }

    private void anhxa() {
        edtFullname = findViewById(R.id.etFullName);
        edtphone = findViewById(R.id.etPhone);
        edtaddress = findViewById(R.id.etAddress);
        email = findViewById(R.id.etEmail);
        imgProfile = findViewById(R.id.ivProfile);
        iconCamera = findViewById(R.id.ivEditImage);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
    }
}