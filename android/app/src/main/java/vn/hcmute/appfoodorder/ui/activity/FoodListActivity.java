package vn.hcmute.appfoodorder.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.ui.adapter.FoodListAdapter;
import vn.hcmute.appfoodorder.viewmodel.FoodListViewModel;

public class FoodListActivity extends AppCompatActivity {
    private FoodListViewModel foodViewModel;
    private RecyclerView rcvListFood;
    private TextView tvCateName;
    private FoodListAdapter foodAdapter;
    private ProgressBar progressBar;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhxa();
        setupRecyclerView();
        setupViewModel();
        setupListener();

    }

    private void setupListener() {
        // listener button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupViewModel() {
        // lay cat_id va cate_name
        Long cate_id = getIntent().getLongExtra("cate_id", -1);
        String cate_name = getIntent().getStringExtra("cate_name");
        if(cate_name != null && !cate_name.isEmpty())
            tvCateName.setText(cate_name.trim());

        // khoi tao viewmodel
        foodViewModel = new ViewModelProvider(this).get(FoodListViewModel.class);

        // goi API
        getFoodByCategory(cate_id);
    }

    private void setupRecyclerView() {
        // set layout rcv
        rcvListFood.setLayoutManager(new GridLayoutManager(FoodListActivity.this, 2));
        foodAdapter = new FoodListAdapter(this);
        rcvListFood.setAdapter(foodAdapter);
    }

    private void anhxa(){
        progressBar = findViewById(R.id.progressBar);
        tvCateName = findViewById(R.id.tvTitle);
        rcvListFood = findViewById(R.id.rcvListFood);
        btnBack = findViewById(R.id.btnBack);
        progressBar.setVisibility(View.VISIBLE); // khi bat dau load du lieu
    }

    private void getFoodByCategory(Long cate_id){
        if(cate_id != -1){
            foodViewModel.fetchListFood(cate_id);
        }

        foodViewModel.getFoodList().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                foodAdapter.setData(foods);
            }
        });

        foodViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Error load List food: ", errorMessage);
                }
            }
        });

        foodViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}