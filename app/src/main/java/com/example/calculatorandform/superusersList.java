package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class superusersList extends AppCompatActivity {

    ListView listaUsuarios;
    AdminSQLiteOpenHelper admin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_superusers_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaUsuarios = findViewById(R.id.listaUsuarios);

        admin = new AdminSQLiteOpenHelper(this, "login", null, 1);

        SQLiteDatabase dbRead = admin.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery("SELECT username FROM superusers", null);
        List<String> superusersList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(0);
                superusersList.add(username);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbRead.close();

        if (!superusersList.isEmpty()) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    superusersList
            );

            listaUsuarios.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay superusuarios", Toast.LENGTH_SHORT).show();
        }
    }
}
