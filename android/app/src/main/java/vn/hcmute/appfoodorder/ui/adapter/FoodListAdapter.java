package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.FoodWithStarResponse;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.ui.activity.FoodDetailActivity;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private List<FoodWithStarResponse> foods;
    private Context context;

    public FoodListAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<FoodWithStarResponse> foods){
        this.foods = foods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.FoodViewHolder holder, int position) {
        // gan data cho view
        FoodWithStarResponse food = foods.get(position);
        Glide.with(context)
                .load(food.getFoodImageUrls())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imgFood);
        holder.tvFoodName.setText(food.getFoodName());
        holder.tvPrice.setText(String.format("%,.0f đ", food.getFoodPrice()));
        holder.tvRate.setText(food.getAvgRating().toString()); // fix set rate
        holder.tvSl.setText("Đã bán: " + food.getTotalSold()); // fix só luong da ban

        // set su kien click item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("foodId", food.getFoodId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foods == null ? 0 : foods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView tvFoodName, tvSl, tvPrice, tvRate;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFoodSearch);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvSl = itemView.findViewById(R.id.tvQuantitySale);
        }
    }

}
