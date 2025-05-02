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
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.ui.activity.FoodDetailActivity;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {
    private Context context;
    private List<Food> foodList;

    // Constructor
    public SearchResultAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    // Cập nhật danh sách mới từ Fragment/ViewModel
    public void updateList(List<Food> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_search_result, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Food food = foodList.get(position);
        Glide.with(context)
                .load(food.getFirstImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.foodImage);
        holder.foodName.setText(food.getFoodName());
        holder.foodPrice.setText(food.getFoodPrice().toString());

        // set su kien click item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("foodId", food.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.imgFoodSearch);
            foodName = itemView.findViewById(R.id.txtFoodNameSearch);
            foodPrice = itemView.findViewById(R.id.txtFoodPriceSearch);
        }
    }
}
