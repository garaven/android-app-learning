package com.example.calculatorandform;

import android.annotation.SuppressLint;
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

public class updateSuperuser extends AppCompatActivity {

    EditText searchUserInput, userInput, passwordInput;
    Button searchButton, updateButton;

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

        searchButton.setOnClickListener(v -> handleSearchSuperuser(superusers));
        updateButton.setOnClickListener(v -> handleUpdateSuperuser(superusers));
    }

    private void handleSearchSuperuser(ArrayList<Superuser> superusers) {
        String searchUsername = searchUserInput.getText().toString().trim();
        if (searchUsername.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        superuserSearched = findSuperuserByUsername(superusers, searchUsername);
        if (superuserSearched != null) {
            userInput.setText(superuserSearched.getUsername());
            passwordInput.setText(superuserSearched.getPassword());
            updateButton.setEnabled(true);
        } else {
            Toast.makeText(this, "Superusuario no encontrado", Toast.LENGTH_SHORT).show();
            updateButton.setEnabled(false);
        }
    }

    private Superuser findSuperuserByUsername(ArrayList<Superuser> superusers, String username) {
        for (Superuser su : superusers) {
            if (su.getUsername().equalsIgnoreCase(username)) {
                return su;
            }
        }
        return null;
    }

    private void handleUpdateSuperuser(ArrayList<Superuser> superusers) {
        if (superuserSearched != null) {
            superuserSearched.setUsername(userInput.getText().toString());
            superuserSearched.setPassword(passwordInput.getText().toString());

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedSuperusersList", superusers);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Primero busque un superusuario", Toast.LENGTH_SHORT).show();
        }
    }
}
