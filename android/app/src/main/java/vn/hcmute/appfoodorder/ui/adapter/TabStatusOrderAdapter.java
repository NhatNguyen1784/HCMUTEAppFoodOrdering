package vn.hcmute.appfoodorder.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.OrderResponse;
import vn.hcmute.appfoodorder.ui.activity.order.OrderDetailActivity;
import vn.hcmute.appfoodorder.ui.activity.order.OrderStatusActivity;
import vn.hcmute.appfoodorder.viewmodel.OrderStatusViewModel;

public class TabStatusOrderAdapter extends RecyclerView.Adapter<TabStatusOrderAdapter.TabViewHolder> {
    private Context context;
    private int isView;
    private List<OrderResponse> orders;
    private OrderStatusViewModel viewModel;

    public TabStatusOrderAdapter(Context context, int isView, List<OrderResponse> orders, OrderStatusViewModel viewModel) {
        this.context = context;
        this.isView = isView;
        this.orders = orders;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isView == 0) {//item_order_pending
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pending, parent, false);
            return new TabViewHolder(view);
        } else if(isView == 1) {//item_order_delivered
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_successful, parent, false);
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
            holder.status.setText("Trạng thái: Chờ xác nhận");
        }
        else if(order.getOrderStatus().equals("SHIPPING")){
            holder.status.setText("Trạng thái: Xác nhận");
            holder.pendingCancelBtn.setVisibility(View.GONE);
            holder.pendingNotCancelBtn.setVisibility(View.VISIBLE);
        }
        else if(order.getOrderStatus().equals("CANCELLED")){
            holder.status.setText("Trạng thái: Đã hủy");
            holder.cancelTv.setVisibility(View.VISIBLE);
            holder.deliveredTv.setVisibility(View.GONE);
            holder.supportBtn.setVisibility(View.VISIBLE);
            holder.ratingBtn.setVisibility(View.GONE);
        }
        else if(order.getOrderStatus().equals("DELIVERED")){
            holder.status.setText("Trạng thái: Giao thành công");
            holder.pendingCancelBtn.setVisibility(View.GONE);
            holder.pendingNotCancelBtn.setVisibility(View.GONE);
            holder.btnConfirmOrder.setVisibility(View.VISIBLE);
        }
        else if(order.getOrderStatus().equals("SUCCESSFUL")){
            holder.status.setText("Trạng thái: Hoàn tất");
            holder.deliveredTv.setVisibility(View.VISIBLE);
        }
        holder.createdDate.setText(order.getCreatedDate());
        holder.totalQuantity.setText("Số lượng: "+String.valueOf(order.getTotalQuantity()));
        holder.totalBill.setText("Tổng đơn hàng: "+ String.format("%,.0f đ", order.getTotalPrice()));

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
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                }
            }
        });

        if(isView == 0){
            holder.pendingCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Xác nhận hủy đơn hàng")
                            .setMessage("Bạn có chắc muốn hủy đơn hàng " + order.getOrderId() + " không?")
                            .setPositiveButton("Hủy đơn", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.cancelOrderByOrderId(order.getOrderId());
                                    Toast.makeText(context, "Bạn đã hủy đơn hàng " + order.getOrderId() + " thành công", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();

                                    if (context instanceof Activity) {
                                        ((Activity) context).finish();
                                        context.startActivity(new Intent(context, OrderStatusActivity.class));
                                    }
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
            });

            holder.btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.confirmOrderByOrderId(order.getOrderId());
                    notifyDataSetChanged();
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                        context.startActivity(new Intent(context, OrderStatusActivity.class));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders !=null ? orders.size() : 0;
    }

    public class TabViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, totalBill, totalQuantity, status, createdDate, cancelTv, deliveredTv;
        private Button pendingCancelBtn, pendingNotCancelBtn;
        private Button reOrderBtn, ratingBtn, ratedBtn, supportBtn, btnConfirmOrder;
        public TabViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId_tv);
            totalBill = itemView.findViewById(R.id.totalBill_tv);
            totalQuantity = itemView.findViewById(R.id.totalQuantity_tv);
            status = itemView.findViewById(R.id.statusOrder_tv);
            createdDate = itemView.findViewById(R.id.createdDate_tv);
            if(isView == 0){
                pendingCancelBtn = itemView.findViewById(R.id.pendingCancel_btn);
                pendingNotCancelBtn = itemView.findViewById(R.id.pendingNotCancel_btn);
                btnConfirmOrder = itemView.findViewById(R.id.btnConfirmOrder);
            }
            else if(isView == 1){
                cancelTv = itemView.findViewById(R.id.cancelled_tv);
                reOrderBtn = itemView.findViewById(R.id.reorder_btn);
                ratedBtn = itemView.findViewById(R.id.rated_btn);
                ratingBtn = itemView.findViewById(R.id.rating_btn);
                supportBtn = itemView.findViewById(R.id.supportBtn);
                deliveredTv = itemView.findViewById(R.id.delivered_tv);
            }
        }
    }
}
