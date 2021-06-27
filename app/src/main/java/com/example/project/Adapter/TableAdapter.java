package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Listeners.OnTableClickListener;
import com.example.project.Models.TableObject;
import com.example.project.R;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TablesViewHolder> {

    ArrayList<TableObject> tables ;
    OnTableClickListener listener ;

    public TableAdapter(ArrayList<TableObject> tables, OnTableClickListener listener) {
        this.tables = tables;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_custom,null);
        TablesViewHolder holder = new TablesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TablesViewHolder holder, int position) {
        TableObject table = tables.get(position);
        holder.bind(table);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    class TablesViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_status ;
        TextView tv_tableNumber ;

        TableObject table ;

        public TablesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_status =itemView.findViewById(R.id.table_custom_iv_status);
            tv_tableNumber =itemView.findViewById(R.id.table_custom_tv_tableNum);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnTableClickListener(table);
                }
            });

        }

        public void bind(TableObject table) {
            this.table = table ;

            tv_tableNumber.setText(String.valueOf(table.getId()));
            if(table.isStatus()){
                iv_status.setImageResource(R.drawable.table_custom_img);
            }else{
                iv_status.setImageResource(R.drawable.table_custom_img_unavailable);
            }
        }
    }
}
