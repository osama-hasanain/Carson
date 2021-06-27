package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project.Adapter.MenuAdapter;
import com.example.project.Models.Category;
import com.example.project.Models.Menu_Categories;
import com.example.project.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    boolean addIsShow = false ;
    RecyclerView recycler ;
    ArrayList<Menu_Categories> menus ;
    MenuAdapter adapter;
    FirebaseFirestore firestore ;
    LinearLayout addTitle,addLayout ;
    ImageView imgadd , img ;
    Spinner spinner ;
    byte[] bytes;
    String title , desc , price , type ;
    EditText etTitle , etDesc , etPrice ;
    Button btnAdminAdd ;
    ProgressDialog progress ;
    StorageReference storageRef , mStorage;
    int cateogery ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();
        addTitle.setOnClickListener(this);
        img.setOnClickListener(this);
        btnAdminAdd.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler();
        initSpinner();
    }

    private void init(){
        recycler = findViewById(R.id.admin_recycler);
        menus = new ArrayList<>();
        adapter = new MenuAdapter(menus,true);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        firestore = FirebaseFirestore.getInstance();
        addLayout = findViewById(R.id.admin_add_layout);
        addTitle = findViewById(R.id.admin_add_title);
        imgadd = findViewById(R.id.admin_add_title_forward);
        img = findViewById(R.id.admin_img);
        spinner = findViewById(R.id.admin_spinner);
        etTitle = findViewById(R.id.admin_et_title);
        etDesc = findViewById(R.id.admin_et_desc);
        etPrice = findViewById(R.id.admin_et_price);
        btnAdminAdd = findViewById(R.id.admin_add_btn);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading ..");
        progress.setCancelable(false);
        storageRef = FirebaseStorage.getInstance().getReference();
        mStorage = storageRef.child("orderImage");
    }
    private void initRecycler(){
        menus.removeAll(menus);
        menus.clear();
        firestore.collection("Menu")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (int i=0;i<queryDocumentSnapshots.size();i++){
                            Menu_Categories menu =  queryDocumentSnapshots.getDocuments().get(i).toObject(Menu_Categories.class);
                            menus.add(menu);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getBaseContext(),"Error in get data",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void initSpinner(){
        ArrayList<String> categories = new ArrayList<>();
        firestore.collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (int i=0;i<queryDocumentSnapshots.size();i++){
                           Category category= queryDocumentSnapshots.getDocuments().get(i).toObject(Category.class);
                            categories.add(category.getCategory_title());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, categories);
                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("osm","Error in get data");
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_add_title :
                if (addIsShow){
                    addIsShow = false;
                    addLayout.setVisibility(View.VISIBLE);
                    imgadd.setImageResource(R.drawable.ic_expand);
                }else{
                    addIsShow = true;
                    addLayout.setVisibility(View.GONE);
                    imgadd.setImageResource(R.drawable.ic_forward);
                }
                break;
            case R.id.admin_img:
                ImagePicker.with(this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
            case R.id.admin_add_btn:
                if (bytes==null || bytes.length<1){
                    Toast.makeText(getBaseContext(), "please choice image", Toast.LENGTH_SHORT).show();
                }else if ((etTitle.getText().toString().isEmpty()||etTitle.getText().toString()==null)
                || (etDesc.getText().toString().isEmpty()||etDesc.getText().toString()==null ||
                        (etPrice.getText().toString().isEmpty()||etPrice.getText().toString()==null))){
                    Toast.makeText(getBaseContext(), "please fill all boxes", Toast.LENGTH_SHORT).show();
                }else{
                    progress.show();
                    title = etTitle.getText().toString();
                    desc = etDesc.getText().toString();
                    price = etPrice.getText().toString();
                    type = spinner.getSelectedItem().toString();
                    if (type.equals("Fast food"))
                        cateogery=1;
                    else if (type.equals("Meat"))
                        cateogery=2;
                    else if (type.equals("Drink"))
                        cateogery=3;
                    else
                        cateogery=4;
                    uploadImages(bytes);
                }
                break;
        }
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
                                        pusMenu(String.valueOf(uri));
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
    private void pusMenu(String url){
        Menu_Categories menu = new Menu_Categories(cateogery,desc,0,url,Integer.valueOf(price),title);
            firestore.collection("Menu")
                    .add(menu)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progress.dismiss();
                            img.setImageResource(R.color.white);
                            etTitle.setText("");
                            etDesc.setText("");
                            etPrice.setText("");
                            Toast.makeText(getBaseContext(), "Success PUSH", Toast.LENGTH_SHORT).show();
                            onStart();
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
}