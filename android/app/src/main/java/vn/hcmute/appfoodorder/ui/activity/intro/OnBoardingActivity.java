package vn.hcmute.appfoodorder.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.activity.MainActivity;
import vn.hcmute.appfoodorder.ui.adapter.OnBoardingAdapter;
import vn.hcmute.appfoodorder.utils.FirstTimeManager;
import vn.hcmute.appfoodorder.utils.SessionManager;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageButton btnSkip;
    private Button btnNext;
    private CircleIndicator3 indicator;
    private FirstTimeManager first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this); // splash
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        first = new FirstTimeManager(this);
        if(first.isFirst() == true){
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_on_boarding);
        mapping();

        // List layout resource ids instead of pre-inflated views
        List<Integer> layoutIds = new ArrayList<>();
        layoutIds.add(R.layout.onboarding_screen_1);
        layoutIds.add(R.layout.onboarding_screen_2);
        layoutIds.add(R.layout.onboarding_screen_3);

        OnBoardingAdapter adapter = new OnBoardingAdapter(layoutIds);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        btnSkip.setOnClickListener(v -> viewPager.setCurrentItem(layoutIds.size() - 1));

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < layoutIds.size() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                first.saveIsFirst(true);
                startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
                finish();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == layoutIds.size() - 1) {
                    btnNext.setText("GET STARTED");
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mapping() {
        viewPager = findViewById(R.id.viewPagerOB);
        btnSkip = findViewById(R.id.imgBtnSkipOB);
        btnNext = findViewById(R.id.btn_nextOB);
        indicator = findViewById(R.id.circleIndicatorOB);
    }
}
