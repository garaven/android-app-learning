package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class deleteSuperuser extends AppCompatActivity {

    EditText usernameInput;
    Button deleteButton;

    AdminSQLiteOpenHelper admin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_superuser);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.createUsuarioInput);
        deleteButton = findViewById(R.id.deleteUsuarioButton);

        admin = new AdminSQLiteOpenHelper(this, "login", null, 1);

        deleteButton.setOnClickListener(v -> handleDeleteUser());
    }

    private void handleDeleteUser() {
        String usernameToDelete = usernameInput.getText().toString().trim();

        if (usernameToDelete.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean found = false;

        SQLiteDatabase dbDelete = admin.getWritableDatabase();
        int rowsDeleted = dbDelete.delete("superusers", "username = ?", new String[] { usernameToDelete });
        if (rowsDeleted > 0) {
            found = true;
        }

        dbDelete.close();

        if (found) {
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        usernameInput.setText("");
    }
}
