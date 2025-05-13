package vn.hcmute.appfoodorder.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.hcmute.appfoodorder.R;

public class ReviewActivity extends AppCompatActivity {

    private ImageView productImg;
    private TextView tvProductName, tvProductDesc;
    private EditText reviewComment;
    private RatingBar ratingBarReview;
    private LinearLayout mediaLayout;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);
        anhxa();
    }

    private void anhxa() {
        productImg = findViewById(R.id.productImage);
        tvProductName = findViewById(R.id.productTitle);
        tvProductDesc = findViewById(R.id.productVariant);
        reviewComment = findViewById(R.id.reviewInput);
        ratingBarReview = findViewById(R.id.ratingBarReview);
        mediaLayout = findViewById(R.id.addMediaLayout);
        btnSubmit = findViewById(R.id.btnSubmitReview);
    }
}