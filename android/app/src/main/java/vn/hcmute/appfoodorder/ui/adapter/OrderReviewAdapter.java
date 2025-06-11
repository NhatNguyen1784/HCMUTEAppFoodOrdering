package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
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
import vn.hcmute.appfoodorder.model.entity.CartItem;

public class OrderReviewAdapter extends RecyclerView.Adapter<OrderReviewAdapter.OrderReviewViewHolder> {

    private Context context;
    private List<CartItem> cartItems;

    public OrderReviewAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public OrderReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_order, parent, false);
        return new OrderReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReviewViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Glide.with(context)
                .load(item.getFirstImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imageFood);
        holder.tvFoodName.setText(item.getQuantity() + " x "+ item.getFoodName());
        holder.tvTotalPrice.setText(String.format("Giá + %,.0f đ", item.getUnitPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public class OrderReviewViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageFood;
        private TextView tvFoodName, tvTotalPrice;
        public OrderReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imgFoodOrder);
            tvTotalPrice = itemView.findViewById(R.id.txtFoodPriceOrder);
            tvFoodName = itemView.findViewById(R.id.txtFoodNameOrder);
        }
    }
}
