package vn.hcmute.appfoodorder.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.model.entity.FoodImage;
import vn.hcmute.appfoodorder.ui.adapter.ImageFoodSliderAdapter;
import vn.hcmute.appfoodorder.viewmodel.FoodDetailViewModel;

public class FoodDetailActivity extends AppCompatActivity {

    private FoodDetailViewModel foodDetailViewModel;
    private ImageFoodSliderAdapter foodSliderAdapter;
    private ViewPager2 viewpager2;
    private DotsIndicator dotsIndicator;
    private ImageView btnBack, btnFavorite;
    private TextView tvFoodName, tvPrice, tvRatingNum,
            tvDescription, btnMinus, btnAdd, tvQuantity, tvTotalPrice;
    private RatingBar ratingBar;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);

        anhxa();
        setUpAdapter();
        setupViewModel();
        setupListener();
    }

    private void setupListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpAdapter() {
        foodSliderAdapter = new ImageFoodSliderAdapter(this);
        viewpager2.setAdapter(foodSliderAdapter);
        dotsIndicator.setViewPager2(viewpager2);
    }

    private void setupViewModel() {

        // khoi tao viewmodel
        foodDetailViewModel = new ViewModelProvider(this).get(FoodDetailViewModel.class);

        // lay ID tu intent
        Long foodId = getIntent().getLongExtra("foodId", -1);
        if(foodId != -1)
            foodDetailViewModel.fetchFood(foodId);

        foodDetailViewModel.getFood().observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if(food != null){
                    // gan du lieu len view
                    List<FoodImage> foodImages = food.getFoodImages();
                    foodSliderAdapter.setData(foodImages);
                    tvFoodName.setText(food.getFoodName());
                    tvPrice.setText(food.getFoodPrice().toString());
                    tvDescription.setText(food.getFoodDescription());
                    // con thieu thuoc tinh: rating number
                }
            }
        });

        foodDetailViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void anhxa(){
        viewpager2 = findViewById(R.id.viewPagerFoodDetailImages);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        btnBack = findViewById(R.id.btnBackFoodDetail);
        btnFavorite = findViewById(R.id.btnFavoriteFoodDetail);
        tvFoodName = findViewById(R.id.tvFoodNameDetail);
        tvPrice = findViewById(R.id.tvPriceFoodDetail);
        tvRatingNum = findViewById(R.id.tvRatingNumFoodDetail);
        tvDescription = findViewById(R.id.tvDescriptionFoodDetail);
        btnMinus = findViewById(R.id.btnMinusFoodDetail);
        btnAdd = findViewById(R.id.btnAddFoodDetail);
        tvQuantity = findViewById(R.id.tvQuantityFoodDetail);
        tvTotalPrice = findViewById(R.id.tvTotalPriceFoodDetail);
        ratingBar = findViewById(R.id.ratingBarFoodDetail);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }
}