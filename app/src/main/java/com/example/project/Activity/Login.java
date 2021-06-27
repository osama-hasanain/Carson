package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText et_email , et_password , et_code ;
    Button bt_login , bt_regester , bt_admin;
    ImageView bt_back ;
    TextView  toAdmin;
    LinearLayout ly1;
    RelativeLayout ly2;
    FirebaseAuth mAuth ;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        et_email = findViewById(R.id.login_et_email);
        et_password = findViewById(R.id.login_et_password);
        bt_login = findViewById(R.id.login_btn_login);
        bt_regester = findViewById(R.id.login_btn_regester);
        bt_admin = findViewById(R.id.login_btn_admin);
        et_code = findViewById(R.id.login_et_code);
        bt_back = findViewById(R.id.login_img_back);
        toAdmin = findViewById(R.id.login_txt_admin);
        ly1 = findViewById(R.id.login_linear1);
        ly2 = findViewById(R.id.login_linear2);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if(!email.isEmpty() || !password.isEmpty()){

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getBaseContext(),R.string.login_success,Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("email",user.getEmail());
                                editor.putBoolean("islogin",true);
                                editor.apply();
                                Navigation(user.getEmail());
//                                updateUI(user);
                            }else {
                                Toast.makeText(getBaseContext(),R.string.login_fail,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else{
                    Toast.makeText(getBaseContext(),R.string.fill_box,Toast.LENGTH_LONG).show();
                }

            }
        });
        bt_regester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);

            }
        });

        toAdmin.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_admin.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        boolean islogin = sharedPref.getBoolean("islogin", false);
        if (islogin)
            Navigation(email);
//        FirebaseUser user = mAuth.getCurrentUser();
//        updateUI(user);
    }

    private void Navigation(String email) {
            Intent intent = new Intent(getBaseContext(), Table.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_img_back :
            ly1.setVisibility(View.VISIBLE);
            ly2.setVisibility(View.GONE);
                break;
            case R.id.login_btn_admin:
                if (et_code.getText().toString()!=null&&!et_code.getText().toString().isEmpty())
                     getCode();
                else
                    Toast.makeText(getBaseContext(),"Please write admin code",Toast.LENGTH_LONG).show();
            break;
            case R.id.login_txt_admin :
             ly1.setVisibility(View.GONE);
             ly2.setVisibility(View.VISIBLE);
            break;

        }
    }

    private void getCode(){
        firestore.collection("Admin")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i=0;i<queryDocumentSnapshots.size();i++){
                   String code = String.valueOf(queryDocumentSnapshots.getDocuments().get(i).get("code"));
                   if (et_code.getText().toString().equals(code)){
                        startActivity(new Intent(getBaseContext(),AdminActivity.class));
                   }
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }

//
//    public void updateUI(FirebaseUser currentuser){
//        if(currentuser != null ){
//            Intent intent = new Intent(getBaseContext(),Table.class);
//            startActivity(intent);
//            finish();
//        }else {
//            Toast.makeText(this,R.string.no_user,Toast.LENGTH_LONG).show();
//        }
//
//    }
}
