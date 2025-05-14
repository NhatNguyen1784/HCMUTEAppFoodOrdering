package vn.hcmute.appfoodorder.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.request.ReviewRequest;
import vn.hcmute.appfoodorder.utils.Constants;
import vn.hcmute.appfoodorder.utils.ImagePickerUtils;
import vn.hcmute.appfoodorder.utils.SessionManager;
import vn.hcmute.appfoodorder.viewmodel.ReviewViewModel;

public class ReviewActivity extends AppCompatActivity {

    private ImageView productImg, btnBack;
    private TextView tvProductName, tvProductDesc;
    private EditText reviewComment;
    private RatingBar ratingBarReview;
    private LinearLayout mediaLayout, addmediaLayout;
    private Button btnSubmit;
    private ReviewViewModel reviewViewModel;
    private SessionManager sessionManager;
    private List<File> uploadedImageFiles = new ArrayList<>();
    private ReviewRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);
        anhxa();
        setupReview();
        setupListener();
    }

    private void setupReview() {
        // lay du lieu tu order detail
        Intent intent = getIntent();
        long ordeDetailId = intent.getLongExtra("oddId", -1L);
        String foodName = intent.getStringExtra("foodName");
        double unitPrice = intent.getDoubleExtra("unitPrice", -99);
        String imageFood = intent.getStringExtra("foodImage");

        if(ordeDetailId != -1 && foodName != null && unitPrice != 99 && imageFood != null){
            // gan data
            tvProductName.setText(foodName);
            tvProductDesc.setText(String.format("Giá %,.0f đ", unitPrice));
            Glide.with(this)
                    .load(imageFood)
                    .transform(new CenterCrop(), new RoundedCorners(30))
                    .into(productImg);
        }

        // setup session and viewModel
        sessionManager = new SessionManager(this);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        request = new ReviewRequest();
        request.setOrderDetailId(ordeDetailId);

        reviewViewModel.getMessageSuccess().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        reviewViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupListener() {

        ratingBarReview.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (rating > 0) {
                btnSubmit.setEnabled(true);
                btnSubmit.setAlpha(1f); // bật lại nút
            } else {
                btnSubmit.setEnabled(false);
                btnSubmit.setAlpha(0.5f); // tắt nút
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addmediaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sessionManager.getUserInfor().getEmail();
                int star = (int) ratingBarReview.getRating();

                request.setRating(star);
                request.setComment(reviewComment.getText().toString().trim());
                request.setUserEmail(email);

                reviewViewModel.submitReview(request, uploadedImageFiles);
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

                    addImageWithDeleteButton(selectedImageUri);
                }
                else {
                    Log.e("DEBUG", "selectedImageUri == null");
                }
            } else if (requestCode == Constants.CAMERA_REQUEST_CODE) {
                File imageFile = ImagePickerUtils.getCurrentPhotoFile();
                if(imageFile != null && imageFile.exists()){
                    Uri imageUri = Uri.fromFile(imageFile);

                    addImageWithDeleteButton(imageUri);
                }
            }
        }
        else {
            Log.e("DEBUG", "Result not OK");
        }
    }

    private void addImageWithDeleteButton(Uri imageUri) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // ImageView để hiển thị ảnh
        ImageView imageView = new ImageView(this);

        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(imageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Convert Uri to File
        File imageFile = uriToFile(imageUri);
        if (imageFile != null) {
            uploadedImageFiles.add(imageFile);
        }

        // Nút xoá
        ImageButton deleteBtn = new ImageButton(this);
        deleteBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        deleteBtn.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(60, 60);
        deleteParams.gravity = Gravity.END | Gravity.TOP;
        deleteBtn.setLayoutParams(deleteParams);

        // Khi bấm xoá
        deleteBtn.setOnClickListener(v -> {
            mediaLayout.removeView(linearLayout);
            uploadedImageFiles.remove(imageFile); // remove khỏi danh sách
        });

        // Thêm vào FrameLayout
        linearLayout.addView(imageView);
        linearLayout.addView(deleteBtn);

        // Thêm vào layout hiển thị
        mediaLayout.addView(linearLayout);
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
                                    ImagePickerUtils.openCamera(ReviewActivity.this);
                                }
                            });

                        } else {
                            // chon anh tu thu vien
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                checkPermissionAndDo(Manifest.permission.READ_MEDIA_IMAGES, Constants.REQUEST_GALLERY_PERMISSION, new Runnable() {
                                    @Override
                                    public void run() {
                                        ImagePickerUtils.openGallery(ReviewActivity.this);
                                    }
                                });
                            }
                            else {
                                checkPermissionAndDo(Manifest.permission.READ_EXTERNAL_STORAGE, Constants.REQUEST_GALLERY_PERMISSION, new Runnable() {
                                    @Override
                                    public void run() {
                                        ImagePickerUtils.openGallery(ReviewActivity.this);
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
        btnBack = findViewById(R.id.btnBackReview);
        productImg = findViewById(R.id.productImage);
        tvProductName = findViewById(R.id.productTitle);
        tvProductDesc = findViewById(R.id.productVariant);
        reviewComment = findViewById(R.id.reviewInput);
        ratingBarReview = findViewById(R.id.ratingBarReview);
        addmediaLayout = findViewById(R.id.addMediaLayout);
        btnSubmit = findViewById(R.id.btnSubmitReview);
        mediaLayout = findViewById(R.id.mediaLayout);

        // Ban đầu disable nút Gửi
        btnSubmit.setEnabled(false);
        btnSubmit.setAlpha(0.5f); // làm mờ nút để thể hiện đã disable
    }
}