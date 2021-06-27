package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.OrderItemAdapter;
import com.example.project.Models.Order;
import com.example.project.Models.OrderItem;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firestore ;
    String email ;
    ImageView img ;
    TextView txt_title , txt_email;
    RecyclerView recycler ;
    ArrayList<Order> orders ;
    ArrayList<OrderItem> orderItems ;
    OrderItemAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getUserData();
    }

    private void getOrder() {
      firestore.collection("Order")
              .get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      for (int i=0;i<queryDocumentSnapshots.size();i++){
                          Order order = queryDocumentSnapshots.getDocuments().get(i).toObject(Order.class);
                          if (order.getEmail().equals(email)){
                              orders.add(order);
                          }
                      }
                      initRecycler();
                  }
              });
    }
    private void initRecycler(){
        for (int i=0;i<orders.size();i++){
            if(orders.get(i).getOrder_list()!=null&&orders.get(i).getOrder_list().size()>0)
              orderItems.addAll(orders.get(i).getOrder_list());
        }
        adapter.notifyDataSetChanged();
    }
    private void getUserData() {
        firestore.collection("User")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (int i=0 ; i<queryDocumentSnapshots.size();i++){
                            if (String.valueOf(queryDocumentSnapshots.getDocuments().get(i).get("email")).equals(email)) {{
                                Picasso.get().load(String.valueOf(queryDocumentSnapshots.getDocuments().get(i).get("img"))).into(img);
                                txt_title.setText(String.valueOf(queryDocumentSnapshots.getDocuments().get(i).get("name")));
                                txt_email.setText(String.valueOf(queryDocumentSnapshots.getDocuments().get(i).get("email")));
                                getOrder();
                                break;
                            }};
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getBaseContext(),"Fail in get data",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void init() {
        SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
        email = sharedPref.getString("email", "");
        firestore = FirebaseFirestore.getInstance();
        txt_title = findViewById(R.id.profile_txt_name);
        txt_email = findViewById(R.id.profile_txt_email);
        img = findViewById(R.id.profile_img);
        recycler = findViewById(R.id.profile_recycler);
        orders = new ArrayList<>();
        orderItems = new ArrayList<>();
        adapter = new OrderItemAdapter(orderItems,false);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }
}