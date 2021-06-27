package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Listeners.OnMenuOnClickListener;
import com.example.project.Models.Menu_Categories;
import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenusViewHolder> {
    ArrayList<Menu_Categories> menus ;
    OnMenuOnClickListener listener ;
    boolean isAdmin ;

    public MenuAdapter(ArrayList<Menu_Categories> menus, OnMenuOnClickListener listener) {
        this.menus = menus;
        this.listener = listener;
    }

    public MenuAdapter(ArrayList<Menu_Categories> menus,boolean isAdmin) {
        this.menus = menus;
        this.isAdmin = isAdmin;
    }


    @NonNull
    @Override
    public MenusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu_item,null);
        MenusViewHolder holder = new MenusViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenusViewHolder holder, int position) {
       Menu_Categories menu= menus.get(position);
       if (isAdmin) {
           //holder.tv_delete.setVisibility(View.VISIBLE);
           holder.iv_plus.setVisibility(View.GONE);
           holder.iv_minus.setVisibility(View.GONE);
           holder.tv_num.setVisibility(View.GONE);
       }
       holder.bind(menu);
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class MenusViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_photo , iv_plus, iv_minus ;
        TextView tv_titl ,tv_desc ,tv_price , tv_num,tv_delete ;
        Menu_Categories menu ;

        public MenusViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.custom_menu_item_iv_photo);
            iv_plus = itemView.findViewById(R.id.custom_menu_item_img_plus);
            iv_minus = itemView.findViewById(R.id.custom_menu_item_img_minus);
            tv_titl = itemView.findViewById(R.id.custom_menu_item_tv_title);
            tv_desc = itemView.findViewById(R.id.custom_menu_item_tv_desc);
            tv_num = itemView.findViewById(R.id.custom_menu_item_tv_num);
            tv_price = itemView.findViewById(R.id.custom_menu_item_tv_price);
            tv_delete = itemView.findViewById(R.id.custom_menu_item_txt_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Current_number = Integer.parseInt(tv_num.getText().toString());
                    if (!isAdmin) {
                        listener.OnMenuOnClickListener(menu, Current_number);
                    }
                }
            });

            iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Current_number =Integer.parseInt(tv_num.getText().toString());
                    tv_num.setText(String.valueOf(Current_number + 1));
                }
            });
            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Current_number =Integer.parseInt(tv_num.getText().toString());
                    if(Current_number != 0){
                        tv_num.setText(String.valueOf(Current_number - 1));
                    }
                }
            });

        }

        public void bind(Menu_Categories menu) {
            this.menu = menu ;

            tv_titl.setText(menu.getItem_title());
            tv_desc.setText(menu.getItem_description());
            tv_price.setText(String.valueOf(menu.getItem_price()));

            if(menu.getItem_image()!=null){
                Picasso.get().load(menu.getItem_image()).into(iv_photo);
            }


        }
    }
}
