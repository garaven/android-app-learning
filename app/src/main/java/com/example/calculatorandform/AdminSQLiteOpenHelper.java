package com.example.calculatorandform;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table superusers(username text primary key, password text)");
        db.execSQL("CREATE table users(id integer primary key, name text, secondName text, email text, docType text, gender text, age integer, docNumber integer, educationLevel text, createdBy integer, foreign key(createdBy) references superusers(id))");
        db.execSQL("CREATE table sports(userId integer, soccer integer, basketball integer, tennis integer, running integer, swimming integer, cycling integer, foreign key(userId) references users(id))");
        db.execSQL("CREATE table musicTastes(userId integer, rock integer, pop integer, rap integer, jazz integer, classic integer, reggaeton integer, foreign key(userId) references users(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
