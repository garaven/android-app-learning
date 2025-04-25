package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class createSuperuser extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button create;
    AdminSQLiteOpenHelper admin;
    ArrayList<Superuser> existingUsers = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_superuser);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.createUsuarioInput);
        passwordInput = findViewById(R.id.contrasenaInput);
        create = findViewById(R.id.crearButton);

        admin = new AdminSQLiteOpenHelper(this, "login", null, 1);

        create.setOnClickListener(x -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                return;
            }
            //

            // Search if the username is taken
            SQLiteDatabase dbRead = admin.getReadableDatabase();
            Cursor cursor = dbRead.rawQuery("SELECT username FROM superusers WHERE username = ?", new String[] { username });

            if (cursor.getCount() > 0) {
                Toast.makeText(this, "El nombre de usuario ya está registrado.", Toast.LENGTH_SHORT).show();
                return;
            }

            cursor.close();
            dbRead.close();
            //

            // Create the superuser
            SQLiteDatabase dbInsert = admin.getWritableDatabase();
            ContentValues file = new ContentValues();
            file.put("username", username);
            file.put("password", password);
            dbInsert.insert("superusers", null, file);

            usernameInput.setText("");
            passwordInput.setText("");

            dbInsert.close();
            Toast.makeText(this, "Superusuario " + username + " agregado", Toast.LENGTH_SHORT).show();
            //
        });
    }
}
