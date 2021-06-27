package com.example.project.Adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Listeners.OnOrderItemListener;
import com.example.project.Models.Menu_Categories;
import com.example.project.Models.OrderItem;
import com.example.project.R;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    ArrayList<OrderItem> items ;
    OnOrderItemListener listener ;
    boolean isDelete ;

    public OrderItemAdapter(ArrayList<OrderItem> items, OnOrderItemListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public OrderItemAdapter(ArrayList<OrderItem> items , boolean isDelete ){
        this.items = items ;
        this.isDelete = isDelete;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_item_layout,null);
        OrderItemViewHolder holder = new OrderItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
      OrderItem orderItem =  items.get(position);
      if (!isDelete)
          holder.iv_delete.setVisibility(View.GONE);
      holder.bind(orderItem);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title , tv_num , tv_price ;
        ImageView iv_delete ;
        OrderItem orderItem ;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.custom_order_item_title);
            tv_num = itemView.findViewById(R.id.custom_order_item_num);
            tv_price = itemView.findViewById(R.id.custom_order_item_price);
            iv_delete= itemView.findViewById(R.id.custom_order_item_delete);

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnOrderItemDelete(orderItem.getOrderItem());
                    notifyDataSetChanged();
                }
            });


        }

        public void bind(OrderItem orderItem) {
            this.orderItem = orderItem ;
            tv_title.setText(orderItem.getOrderItem().getItem_title());
            tv_price.setText(String.valueOf(orderItem.getOrderItem().getItem_price()));
            tv_num.setText(String.valueOf(orderItem.getNumber()));


        }
    }
}
