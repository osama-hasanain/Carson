package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.project.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //عشان نجعل الشاشة فل بالشاشة
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation();
            }
        },5000);
    }
    private void Navigation() {
        SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        boolean islogin = sharedPref.getBoolean("islogin", false);
        if (islogin){
            Intent intent = new Intent(getBaseContext(), Table.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }
}
