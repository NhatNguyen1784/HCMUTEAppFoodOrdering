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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;
import java.util.function.Consumer;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;
import vn.hcmute.appfoodorder.model.dto.response.UserResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.model.entity.FoodImage;
import vn.hcmute.appfoodorder.ui.adapter.ImageFoodSliderAdapter;
import vn.hcmute.appfoodorder.ui.adapter.ReviewAdapter;
import vn.hcmute.appfoodorder.viewmodel.CartViewModel;
import vn.hcmute.appfoodorder.viewmodel.FoodDetailViewModel;
import vn.hcmute.appfoodorder.viewmodel.ProfileViewModel;
import vn.hcmute.appfoodorder.viewmodel.ReviewViewModel;

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
    private ReviewViewModel reviewViewModel;
    private RecyclerView rcvReview;
    private ReviewAdapter reviewAdapter;

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
                    getCurrentUser(user -> {
                        String email = user.getEmail();
                        request.setEmail(email);
                    });
                    // JWT thi fix
                    request.setFoodId(food.getId());
                    int quantity = Integer.parseInt(tvQuantity.getText().toString());
                    request.setQuantity(quantity);

                    cartViewModel.addItemToCart(request);
                    Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());
                if(quantity > 1){
                    quantity--;
                    String  unitPriceString = tvPrice.getText().toString();
                    unitPriceString = unitPriceString.replace(",", "").replace("đ", "").trim();
                    double unitPrice = Double.parseDouble(unitPriceString);
                    double totalPrice = totalPrice(quantity, unitPrice);
                    tvQuantity.setText(String.valueOf(quantity));
                    tvTotalPrice.setText(String.format("%,.0f đ", totalPrice));
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());

                if(quantity >= 0 && quantity < 100){
                    quantity++;
                    String  unitPriceString = tvPrice.getText().toString();
                    unitPriceString = unitPriceString.replace(",", "").replace("đ", "").trim();
                    double unitPrice = Double.parseDouble(unitPriceString);
                    double totalPrice = totalPrice(quantity, unitPrice);
                    tvQuantity.setText(String.valueOf(quantity));
                    tvTotalPrice.setText(String.format("%,.0f đ", totalPrice));
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

    private void getCurrentUser(Consumer<UserResponse> callback) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(getApplicationContext());
        profileViewModel.getUserInfor().observe(this, userResponse -> {
            if (userResponse != null) {
                callback.accept(userResponse); // gọi lại khi có dữ liệu
            }
        });
    }

    private void setupViewModel() {

        // khoi tao viewmodel
        foodDetailViewModel = new ViewModelProvider(this).get(FoodDetailViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        // lay ID tu intent
        Long foodId = getIntent().getLongExtra("foodId", -1);
        if(foodId != -1){
            foodDetailViewModel.fetchFood(foodId);
            reviewViewModel.getReviewByFoodId(foodId);
        }


        foodDetailViewModel.getFood().observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if(food != null){
                    // gan du lieu len view
                    List<FoodImage> foodImages = food.getFoodImages();
                    foodSliderAdapter.setData(foodImages);
                    tvFoodName.setText(food.getFoodName());
                    tvPrice.setText(String.format("%,.0f đ", food.getFoodPrice()));
                    tvDescription.setText(food.getFoodDescription());
                    tvTotalPrice.setText(tvPrice.getText());
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

        // lay danh gia
        reviewViewModel.getListReviewLiveData().observe(this, new Observer<ReviewListResponse>() {
            @Override
            public void onChanged(ReviewListResponse reviewListResponse) {
                long totalReview = reviewListResponse.getTotalReviews();
                tvRatingNum.setText(totalReview + " đánh giá"); // tong so luon danh gia

                float avgRating = (float) reviewListResponse.getAvgRating();
                ratingBar.setRating(avgRating); // danh gia trung binh

                List<ReviewResponse> reviews = reviewListResponse.getReviews();
                reviewAdapter = new ReviewAdapter(FoodDetailActivity.this);
                reviewAdapter.setData(reviews);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FoodDetailActivity.this, RecyclerView.VERTICAL, false);
                rcvReview.setLayoutManager(layoutManager);
                rcvReview.setAdapter(reviewAdapter);
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
        rcvReview = findViewById(R.id.rvUserReviews);
    }
}