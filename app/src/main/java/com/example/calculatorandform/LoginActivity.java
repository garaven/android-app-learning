package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button login, adminPanel;
    SQLiteOpenHelper adminDB;
    ArrayList<Superuser> superusersL = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.superUsernameInput);
        passwordInput = findViewById(R.id.superPasswordInput);
        login = findViewById(R.id.superLoginButton);
        adminPanel = findViewById(R.id.adminPanelButton);

        adminDB = new AdminSQLiteOpenHelper(this, "login", null, 1);

        login.setOnClickListener(x -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            SQLiteDatabase dbRead = adminDB.getReadableDatabase();
            Cursor cursor = dbRead.rawQuery("SELECT password FROM superusers WHERE username = ?", new String[] { username });

            // Validations
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "El usuario " + username + " no está registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            String passwordObtained = "";
            if (cursor != null && cursor.moveToFirst()) {
                passwordObtained = cursor.getString(0);
            }

            if (!password.equals(passwordObtained)) {
                Toast.makeText(this, "Credenciales incorrectas, intentalo de nuevo.", Toast.LENGTH_SHORT).show();
                return;
            }
            //

            cursor.close();
            dbRead.close();

            Intent intentHome = new Intent(this, MainActivity.class);
            intentHome.putExtra("superuser", username);
            startActivity(intentHome);
            finish();
        });

        adminPanel.setOnClickListener(x -> {
            Intent intentAdmin = new Intent(this, adminPanel.class);
            startActivity(intentAdmin);
        });
    }
}
