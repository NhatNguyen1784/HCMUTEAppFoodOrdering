package vn.hcmute.appfoodorder.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.OrderDetailDTO;
import vn.hcmute.appfoodorder.ui.activity.ReviewActivity;
import vn.hcmute.appfoodorder.ui.activity.order.OrderStatusActivity;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private Context context;
    private List<OrderDetailDTO> orderDetails;
    private boolean isSuccessful = false;

    public OrderDetailAdapter(Context context, List<OrderDetailDTO> orderDetails, boolean isSuccessful) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.isSuccessful = isSuccessful;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_details, parent, false);
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
        if(isSuccessful && item.getReview() == false){
            holder.btnRatingOrderDetail.setVisibility(View.VISIBLE);
            holder.btnRatingOrderDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context instanceof Activity) {
                        Intent intent = new Intent(context, ReviewActivity.class);
                        intent.putExtra("oddId", item.getId());
                        intent.putExtra("foodName", item.getFoodName().toString().trim());
                        intent.putExtra("unitPrice", item.getUnitPrice());
                        intent.putExtra("foodImage", item.getFoodImage().trim());
                        ((Activity) context).finish();
                        context.startActivity(intent);
                    }

//                    Toast.makeText(context, "Orderdetail id "+item.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageFood;
        private TextView tvFoodName, tvTotalPrice;
        private Button btnRatingOrderDetail;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imgFoodOrder);
            tvTotalPrice = itemView.findViewById(R.id.txtFoodPriceOrder);
            tvFoodName = itemView.findViewById(R.id.txtFoodNameOrder);
            btnRatingOrderDetail = itemView.findViewById(R.id.btnRatingOrderDetail);
        }
    }
}
