package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

        if (getIntent().hasExtra("superusers")) {
            existingUsers = (ArrayList<Superuser>) getIntent().getSerializableExtra("superusers");
        }

        create.setOnClickListener(x -> {
            String username = usernameInput.getText().toString().trim().toLowerCase();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Superuser user : existingUsers) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    Toast.makeText(this, "El nombre de usuario ya está registrado.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intentSendInfo = new Intent();
            intentSendInfo.putExtra("username", username);
            intentSendInfo.putExtra("password", password);
            Toast.makeText(this, "Usuario creado:\n" +
                    "Username: " + username + "\n" +
                    "Contraseña: " + password, Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK, intentSendInfo);
            finish();
        });
    }
}
