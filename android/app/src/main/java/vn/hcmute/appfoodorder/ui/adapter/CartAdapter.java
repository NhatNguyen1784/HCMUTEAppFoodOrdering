package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.util.Log;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private CartItemListener cartItemListener;

    public void setCartItemListener(CartItemListener cartItemListener) {
        this.cartItemListener = cartItemListener;
    }

    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CartItem> cartItems){
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
//        Log.d("CartAdapter", "Image URL: " + item.getFirstImageUrl());
        Glide.with(context)
                .load(item.getFirstImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imgFood);
        holder.tvFoodName.setText(item.getFoodName().trim());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        holder.tvPrice.setText(String.valueOf(item.getPrice()));

        // xu li su kien khi bam tang/giam so luong
        holder.tvPlus.setOnClickListener(view -> {
            if(cartItemListener != null){
                cartItemListener.onIncreaseQuantity(item);
            }
        });

        holder.tvMinus.setOnClickListener(view -> {
            if(cartItemListener != null){
                cartItemListener.onDecreaseQuantity(item);
            }
        });
        holder.btnRemoveItem.setOnClickListener(view -> {
            if(cartItemListener != null){
                cartItemListener.removeCartItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }

    // interface cho thao tac tren cac item trong recycle view
    public interface CartItemListener {
        void onIncreaseQuantity(CartItem item);
        void onDecreaseQuantity(CartItem item);
        void removeCartItem(CartItem item);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFood, btnRemoveItem;

        private TextView tvFoodName, tvUnitPrice,
        tvQuantity, tvPlus, tvMinus, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgItemCart);
            btnRemoveItem = itemView.findViewById(R.id.btnRemoveItemCart);
            tvFoodName = itemView.findViewById(R.id.tvFoodNameItemCart);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPriceFoodItemCart);
            tvQuantity = itemView.findViewById(R.id.tvQTYItemCart);
            tvPlus = itemView.findViewById(R.id.tvPlusItemCart);
            tvMinus = itemView.findViewById(R.id.tvMinusItemCart);
            tvPrice = itemView.findViewById(R.id.tvPriceItemCart);
        }
    }
}
