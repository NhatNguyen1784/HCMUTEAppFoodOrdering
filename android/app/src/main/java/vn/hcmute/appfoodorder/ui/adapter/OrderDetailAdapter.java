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
import vn.hcmute.appfoodorder.model.dto.response.OrderDetailDTO;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private List<OrderDetailDTO> orderDetails;

    public OrderDetailAdapter(Context context, List<OrderDetailDTO> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_order, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailDTO item = orderDetails.get(position);
        Glide.with(context)
                .load(item.getFoodImage())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imageFood);
        holder.tvFoodName.setText(item.getQuantity() + " x "+ item.getFoodName());
        holder.tvTotalPrice.setText(String.format("Giá %,.0f đ", item.getUnitPrice()));
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageFood;
        private TextView tvFoodName, tvTotalPrice;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imgFoodOrder);
            tvTotalPrice = itemView.findViewById(R.id.txtFoodPriceOrder);
            tvFoodName = itemView.findViewById(R.id.txtFoodNameOrder);
        }
    }
}
