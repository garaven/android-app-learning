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

public class deleteSuperuser extends AppCompatActivity {

    EditText usernameInput;
    Button deleteButton;

    ArrayList<Superuser> superusers;

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

        usernameInput = findViewById(R.id.deleteUsuarioInput);
        deleteButton = findViewById(R.id.deleteUsuarioButton);

        // Recibe la lista desde adminPanel
        superusers = (ArrayList<Superuser>) getIntent().getSerializableExtra("superusers");

        deleteButton.setOnClickListener(v -> handleDeleteUser());
    }

    private void handleDeleteUser() {
        String usernameToDelete = usernameInput.getText().toString().trim();

        if (usernameToDelete.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean found = false;

        for (int i = 0; i < superusers.size(); i++) {
            if (superusers.get(i).getUsername().equalsIgnoreCase(usernameToDelete)) {
                superusers.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
            returnResult();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedSuperusersList", superusers);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
