package com.example.calculatorandform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda";
    private static final int DATABASE_VERSION = 2;
    private static AdminSQLiteOpenHelper instance;

    public static synchronized AdminSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AdminSQLiteOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table superusers(username text primary key, password text)");
        db.execSQL("CREATE table users(id integer primary key, name text, secondName text, email text, docType text, gender text, age integer, docNumber integer, educationLevel text, createdBy integer, foreign key(createdBy) references superusers(username))");
        db.execSQL("CREATE table sports(userId integer, soccer integer, basketball integer, tennis integer, running integer, swimming integer, cycling integer, foreign key(userId) references users(id))");
        db.execSQL("CREATE table musicTastes(userId integer, rock integer, pop integer, rap integer, jazz integer, classic integer, reggaeton integer, foreign key(userId) references users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}