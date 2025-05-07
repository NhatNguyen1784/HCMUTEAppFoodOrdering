package vn.hcmute.appfoodorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.hcmute.appfoodorder.R;

public class PaymentNotification extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_notification);
        textView = findViewById(R.id.tvNoti);
        Intent intent = new Intent();
        textView.setText(intent.getSerializableExtra("result").toString());
    }
}