package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project.Models.User;
import com.example.project.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText et_email ,et_name, et_password , et_re_password ;
    Button bt_regester ;
    FirebaseAuth auth ;
    ImageView img ;
    FrameLayout im_ly ;
    StorageReference storageRef , mStorage;
    FirebaseFirestore firestore ;
    String TAG = "Register";
    byte[] bytes ;
    User user ;
    ProgressDialog progress ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        im_ly = findViewById(R.id.regester_img_ly);
        img = findViewById(R.id.regester_img);
        et_email = findViewById(R.id.regester_et_email);
        et_name = findViewById(R.id.regester_et_name);
        et_password = findViewById(R.id.regester_et_password);
        et_re_password = findViewById(R.id.regester_et_repeat_password);
        bt_regester = findViewById(R.id.regester_btn_regester);

        init();

        im_ly.setOnClickListener(this);
        bt_regester.setOnClickListener(this);

    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        mStorage = storageRef.child("usersImages");
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = new User();
        progress = new ProgressDialog(this);
        progress.setMessage("Loading ..");
        progress.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regester_img_ly:
                ImagePicker.with(this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;

            case R.id.regester_btn_regester:
                if (!et_email.getText().toString().isEmpty()&&!et_name.getText().toString().isEmpty()&&!et_password.getText().toString().isEmpty()
                    &&et_password.getText().toString().equals(et_re_password.getText().toString())&&
                        (et_email.getText().toString().endsWith("@gmail.com")||et_email.getText().toString().endsWith("@hotmail.com"))) {
                    progress.show();
                    user.setEmail(et_email.getText().toString());
                    user.setName(et_name.getText().toString());
                    user.setPassword(et_password.getText().toString());
                    uploadImages(bytes);
                }else if(!et_password.getText().toString().equals(et_re_password.getText().toString()))
                    et_re_password.setError("Should Equal with Password");
                else if (!et_email.getText().toString().endsWith("@gmail.com")||!et_email.getText().toString().endsWith("@hotmail.com"))
                    Toast.makeText(getBaseContext(),"Error in email Syntax",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(),"Fill all boxes",Toast.LENGTH_LONG).show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            img.setImageURI(uri);
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(uri);
                bytes = getBytes(iStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private void uploadImages(byte[] data){
        StorageReference childRef = storageRef.child(String.valueOf(System.currentTimeMillis()));
        childRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        childRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        user.setImg(String.valueOf(uri));
                                       registerUser();
                                    }
                                });
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progress.dismiss();
                Toast.makeText(getBaseContext(),"Error in upload data",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void registerUser(){
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pushUser();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progress.dismiss();
                Toast.makeText(getBaseContext(), "Error withen loading", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void pushUser(){
        if (user!=null)
            firestore.collection("User")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                        toTable();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(getBaseContext(), "Error withen loading", Toast.LENGTH_SHORT).show();
                }
            });
    }
    private void toTable(){
        SharedPreferences sharedPref = getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email",user.getEmail());
        editor.putBoolean("islogin",true);
        editor.apply();
        startActivity(new Intent(getBaseContext(),Table.class));
        finish();
    }
}
