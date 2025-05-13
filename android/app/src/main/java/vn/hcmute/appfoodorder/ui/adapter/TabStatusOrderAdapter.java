package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.activity.OrderDetailActivity;

public class TabStatusOrderAdapter extends RecyclerView.Adapter<TabStatusOrderAdapter.TabViewHolder> {
    private Context context;
    private int isView;
    private List<OrderResponse> orders;

    public TabStatusOrderAdapter(Context context, int isView, List<OrderResponse> orders) {
        this.context = context;
        this.isView = isView;
        this.orders = orders;
    }

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isView == 0) {//item_order_pending
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending, parent, false);
            return new TabViewHolder(view);
        } else if(isView == 1) {//item_order_delivered
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_delivered, parent, false);
            return new TabViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending, parent, false);
            return new TabViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
        OrderResponse order = orders.get(position);
        holder.orderId.setText("Mã đơn hàng: " + order.getOrderId().toString());
        if(order.getOrderStatus().equals("PENDING")){
            holder.status.setText(holder.status.getText()+" "+ "Chờ xác nhận");
        }
        else if(order.getOrderStatus().equals("SHIPPING")){
            holder.status.setText(holder.status.getText()+" "+ "Xác nhận");
        }
        else if(order.getOrderStatus().equals("CANCELLED")){
            holder.status.setText(holder.status.getText()+" "+ "Đã hủy");
        }
        else if(order.getOrderStatus().equals("DELIVERED")){
            holder.status.setText(holder.status.getText()+" "+ "Thành công");
        }
        holder.createdDate.setText(order.getCreatedDate());
        holder.totalQuantity.setText(holder.totalQuantity.getText()+" "+ String.valueOf(order.getTotalQuantity()));
        holder.totalBill.setText(holder.totalBill.getText()+" "+ String.format("%.0f đ", order.getTotalPrice()));

        //Next activity order detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    OrderResponse clickedOrder = orders.get(pos);
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("orderId", clickedOrder.getOrderId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders !=null ? orders.size() : 0;
    }

    public class TabViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, totalBill, totalQuantity, status, createdDate;
        private Button pendingCancelBtn, reOrderBtn;
        public TabViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId_tv);
            totalBill = itemView.findViewById(R.id.totalBill_tv);
            totalQuantity = itemView.findViewById(R.id.totalQuantity_tv);
            status = itemView.findViewById(R.id.statusOrder_tv);
            createdDate = itemView.findViewById(R.id.createdDate_tv);
            if(isView==0){
                pendingCancelBtn = itemView.findViewById(R.id.pendingCancel_btn);
            }
            else if(isView==1){
                reOrderBtn = itemView.findViewById(R.id.reorder_btn);
            }
        }
    }
}
