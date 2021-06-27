package com.example.project.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.Models.User;

import java.util.ArrayList;


public class database extends SQLiteOpenHelper {

    public static final String DB_NAME = "users";
    public static final int DB_VERSION = 1 ;

    public database(Context context) {
        super(context, DB_NAME, null,DB_VERSION );
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users "+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+" email TEXT UNIQUE NOT NULL ,"+" " +
                " passsword TEXT ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertEmployee (User user){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());

        long result = db.insert("users",null,values);

        return result !=-1 ;
    }

//    public ArrayList<User> getAllEmployee (){
//        ArrayList<User> users = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase() ;
//        Cursor c = db.rawQuery("SELECT * FROM users",null);
//        if(c.moveToFirst()){
//            do {
//                int id = c.getInt(0);
//                String email = c.getString(1);
//                String password = c.getString(2);
//                users.add(new User(id,email,password));
//            }while (c.moveToNext());
//        }
//        return users ;
//    }
}
