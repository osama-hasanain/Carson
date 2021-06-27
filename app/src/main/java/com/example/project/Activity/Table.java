package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.Adapter.TableAdapter;
import com.example.project.Listeners.OnTableClickListener;
import com.example.project.Models.TableObject;
import com.example.project.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class Table extends AppCompatActivity {
    RecyclerView rv_tables ;
    FirebaseFirestore db ;
    public final static String TABLE_KEY_NAME = "table";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        db = FirebaseFirestore.getInstance() ;
        rv_tables = findViewById(R.id.table_recyclerView);

        db.collection("Tables").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<TableObject> tables = new ArrayList<>();
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        TableObject table = documentSnapshot.toObject(TableObject.class);
                        tables.add(table);
                    }
                    fillRecyclerViewWithTables(tables);
                }
            }
        });
    }
    private void  fillRecyclerViewWithTables(ArrayList<TableObject> tables){
        TableAdapter adapter = new TableAdapter(tables, new OnTableClickListener() {
            @Override
            public void OnTableClickListener(TableObject table) {
                Intent intent = new Intent(Table.this , Categories.class);
                intent.putExtra(TABLE_KEY_NAME,table.getId());
                startActivity(intent);
            }
        });
        rv_tables.setAdapter(adapter);
        rv_tables.setLayoutManager(new GridLayoutManager(this,2));
        rv_tables.setHasFixedSize(true);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
        boolean islogin = sharedPref.getBoolean("islogin", false);
        if (!islogin){
            finish();
        }
    }
}
