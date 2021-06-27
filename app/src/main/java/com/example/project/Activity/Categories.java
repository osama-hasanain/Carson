package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.Adapter.TabAdapter;
import com.example.project.Fragments.MenuFragment;
import com.example.project.Fragments.OrderDialogFragment;
import com.example.project.Models.Category;
import com.example.project.Models.Menu_Categories;
import com.example.project.Models.OrderItem;
import com.example.project.Models.Tab;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Categories extends AppCompatActivity implements MenuFragment.MenuItemSelected, View.OnClickListener {
    Toolbar toolbar ;
    TabLayout tabLayout ;
    ViewPager viewPager ;
    int tableNumber ;
    FirebaseFirestore db ;
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    FloatingActionButton btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        db = FirebaseFirestore.getInstance() ;


        Intent intent = getIntent() ;
        if(intent!=null){
            tableNumber = intent.getIntExtra(Table.TABLE_KEY_NAME,-1);
        }

        toolbar = findViewById(R.id.categories_toolbar);
        tabLayout = findViewById(R.id.categories_tab);
        viewPager = findViewById(R.id.categories_view_pager);
        btnOrder = findViewById(R.id.categories_btn_order);

        btnOrder.setOnClickListener(this);

        setuptoolbar();
        setupViewPagers();
    }

    private void setupViewPagers() {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        addTab(adapter);

    }

    private void addTab(final TabAdapter adapter){
       db.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               if(queryDocumentSnapshots!=null && queryDocumentSnapshots.size() > 0){
                   for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                       Category category = documentSnapshot.toObject(Category.class);
                       adapter.addTab(new Tab(category.getCategory_title(),new MenuFragment(category.getCategory_id())));
                   }
                    viewPager.setAdapter(adapter);
                   tabLayout.setupWithViewPager(viewPager);
               }
           }
       });
    }

    private void setuptoolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.notification :
//                OrderDialogFragment dialogFragment = new OrderDialogFragment(orderItems,this);
//                dialogFragment.show(getSupportFragmentManager()," ");
//                return true ;
            case R.id.exit :
                SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("islogin",false);
                editor.apply();
                startActivity(new Intent(getBaseContext(),Login.class));
                finish();
                return true ;

            case R.id.profile:
                startActivity(new Intent(getBaseContext(),ProfileActivity.class));
                return true ;
        }
        return false ;
    }


    @Override
    public void onMenuItemSelected(Menu_Categories item, int number) {
        orderItems.add(new OrderItem(item , number));
        Toast.makeText(this, "the order is added for list", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.categories_btn_order:
                OrderDialogFragment dialogFragment = new OrderDialogFragment(orderItems,this);
                dialogFragment.show(getSupportFragmentManager()," ");
                break;
        }
    }
}
