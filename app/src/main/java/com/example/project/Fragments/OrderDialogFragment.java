package com.example.project.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Activity.Categories;
import com.example.project.Adapter.OrderItemAdapter;
import com.example.project.Listeners.OnOrderItemListener;
import com.example.project.Models.Menu_Categories;
import com.example.project.Models.Order;
import com.example.project.Models.OrderItem;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDialogFragment extends DialogFragment {
    ArrayList<OrderItem> items = new ArrayList<>();
    RecyclerView recyclerView ;
    Context context;
    TextView txt_empty ;

    public OrderDialogFragment(ArrayList<OrderItem> orderItems, Context context) {
        this.items = orderItems;
        this.context = context;
    }

    public OrderDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_dialog, null);
        recyclerView = v.findViewById(R.id.fragment_order_rv);
        txt_empty =  v.findViewById(R.id.fragment_order_empty);
        final OrderItemAdapter adapter = new OrderItemAdapter(items, new OnOrderItemListener() {
            @Override
            public void OnOrderItemDelete(Menu_Categories menu) {
                for(int i = 0 ; i <items.size() ;i++ ){
                    if (items.get(i).getOrderItem().getItem_id() == menu.getItem_id()){
                        items.remove(i);
                        Toast.makeText(getContext(),"Deleting operation is successful",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

        if (items.isEmpty())
            txt_empty.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Order List")
                .setView(v)
                .setPositiveButton("Make order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        Order order = new Order(0,auth.getCurrentUser().getEmail(),items);
                        firestore.collection("Order").add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                              if (task.isSuccessful()){
                                  Toast.makeText(context, "the order is sender", Toast.LENGTH_SHORT).show();
                              }else{

                              }
                            }
                        });

                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),"Removo order",Toast.LENGTH_SHORT).show();
                        items.clear();
                        adapter.notifyDataSetChanged();

                    }
                }).create();
    }



}
