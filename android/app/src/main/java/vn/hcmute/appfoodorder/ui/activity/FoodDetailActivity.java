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
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.model.entity.FoodImage;
import vn.hcmute.appfoodorder.ui.adapter.ImageFoodSliderAdapter;
import vn.hcmute.appfoodorder.viewmodel.CartViewModel;
import vn.hcmute.appfoodorder.viewmodel.FoodDetailViewModel;

public class FoodDetailActivity extends AppCompatActivity {
    private FoodDetailViewModel foodDetailViewModel;
    private CartViewModel cartViewModel;
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

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lay thong tin mon an
                Food food = foodDetailViewModel.getFood().getValue();
                if (food != null){
                    CartRequest request = new CartRequest();
                    // JWT thi fix
                    request.setEmail("nguyennhatnguyen1782004@gmail.com");
                    request.setFoodId(food.getId());
                    int quantity = Integer.parseInt(tvQuantity.getText().toString());
                    request.setQuantity(quantity);

                    cartViewModel.addItemToCart(request);
                    Toast.makeText(getApplicationContext(), "Add item to cart successfull", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if(quantity > 1){
                    quantity--;
                    double unitPrice = Double.valueOf(tvPrice.getText().toString());
                    double totalPrice = totalPrice(quantity, unitPrice);
                    tvQuantity.setText(String.valueOf(quantity));
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());

                if(quantity >= 0 && quantity < 100){
                    quantity++;
                    double unitPrice = Double.valueOf(tvPrice.getText().toString());
                    double totalPrice = totalPrice(quantity, unitPrice);
                    tvQuantity.setText(String.valueOf(quantity));
                    tvTotalPrice.setText(String.valueOf(totalPrice));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Your quantity is so much", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private double totalPrice(int quantity, double unitPrice){
        double totalPrice = quantity * unitPrice;
        return totalPrice;
    }

    private void setUpAdapter() {
        foodSliderAdapter = new ImageFoodSliderAdapter(this);
        viewpager2.setAdapter(foodSliderAdapter);
        dotsIndicator.setViewPager2(viewpager2);
    }

    private void setupViewModel() {

        // khoi tao viewmodel
        foodDetailViewModel = new ViewModelProvider(this).get(FoodDetailViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // lay ID tu intent
        Long foodId = getIntent().getLongExtra("foodId", -1);
        if (foodId != -1)
            foodDetailViewModel.fetchFood(foodId);

        foodDetailViewModel.getFood().observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if (food != null) {
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

    private void anhxa() {
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