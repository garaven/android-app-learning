package com.example.calculatorandform;

import android.annotation.SuppressLint;
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

public class updateSuperuser extends AppCompatActivity {

    EditText searchUserInput, userInput, passwordInput;
    Button searchButton, updateButton;
    AdminSQLiteOpenHelper admin;
    String userToUpdate;

    private Superuser superuserSearched;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_superuser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchUserInput = findViewById(R.id.searchUsuarioInput);
        userInput = findViewById(R.id.updateUsuarioInput);
        passwordInput = findViewById(R.id.updateContraInput);

        searchButton = findViewById(R.id.searchUsuarioButton);
        updateButton = findViewById(R.id.updateUsuarioButton);

        ArrayList<Superuser> superusers = (ArrayList<Superuser>) getIntent().getSerializableExtra("superusers");

        admin = AdminSQLiteOpenHelper.getInstance(this);

        searchButton.setOnClickListener(v -> handleSearchSuperuser());
        updateButton.setOnClickListener(v -> handleUpdateSuperuser());
    }

    private void handleSearchSuperuser() {
        String searchUsername = searchUserInput.getText().toString().trim();
        if (searchUsername.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase dbRead = admin.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery("SELECT username, password FROM superusers WHERE username = ?", new String[] { searchUsername });

        // Validations
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Superusuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameObtained = "";
        String passwordObtained = "";

        if (cursor != null && cursor.moveToFirst()) {
            usernameObtained = cursor.getString(0);
            passwordObtained = cursor.getString(1);

            userInput.setText(usernameObtained);
            passwordInput.setText(passwordObtained);
            updateButton.setEnabled(true);
            userInput.setEnabled(true);
            passwordInput.setEnabled(true);

            userToUpdate = usernameObtained;
        } else {
            Toast.makeText(this, "Superusuario no encontrado", Toast.LENGTH_SHORT).show();
            updateButton.setEnabled(false);
        }

        cursor.close();
        dbRead.close();
    }

    private void handleUpdateSuperuser() {
        String updatedUsername = userInput.getText().toString().toLowerCase();
        String updatedPassword = passwordInput.getText().toString();

        // Validation
        if (updatedUsername.isEmpty() || updatedPassword.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase dbInsert = admin.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", updatedUsername);
        values.put("password", updatedPassword);
        int rowsUpdated = dbInsert.update("superusers", values, "username = ?", new String[] { userToUpdate });
        if (rowsUpdated == 0) {
            Toast.makeText(this, "Superusuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        dbInsert.close();

        Toast.makeText(this, "Superusuario actualizado:\n" +
                "Username: " + updatedUsername + "\n" +
                "Contraseña: " + updatedPassword, Toast.LENGTH_LONG).show();

        userInput.setText("");
        passwordInput.setText("");
    }
}
