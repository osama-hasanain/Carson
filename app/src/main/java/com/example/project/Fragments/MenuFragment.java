package com.example.project.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Adapter.MenuAdapter;
import com.example.project.Listeners.OnMenuOnClickListener;
import com.example.project.Models.Menu_Categories;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    int category_id;
    RecyclerView rv_menFragment ;
    FirebaseFirestore firestore ;
    FirebaseAuth mAuth ;
    MenuItemSelected listener ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MenuItemSelected){
            listener = (MenuItemSelected) context;
        }
    }

    public MenuFragment() {
        // Required empty public constructor
    }
    public MenuFragment(int category_id) {
        this.category_id = category_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.menu_fragment, container, false);
        rv_menFragment  = v.findViewById(R.id.categories_fragment_recycler_view);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestore.collection("Menu").whereEqualTo("item_category_id",category_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.size()>0){
                    ArrayList<Menu_Categories> menus = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                           Menu_Categories menu = queryDocumentSnapshot.toObject(Menu_Categories.class);
                           menus.add(menu);
                    }
                    fillRecycelerViewWithMenu(menus);
                }
            }
        });

        return v ;
    }

    private void fillRecycelerViewWithMenu(ArrayList<Menu_Categories> menu){
        MenuAdapter menuAdapter = new MenuAdapter(menu, new OnMenuOnClickListener() {
            @Override
            public void OnMenuOnClickListener(Menu_Categories menu, int num) {
                if(num > 0){
                    listener.onMenuItemSelected(menu,num);
                }
            }
        });
        rv_menFragment.setAdapter(menuAdapter);
        rv_menFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_menFragment.setHasFixedSize(true);

    }
    public interface MenuItemSelected {
        void onMenuItemSelected(Menu_Categories item, int number);
    }
}
