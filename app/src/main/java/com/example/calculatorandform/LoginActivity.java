package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
    ArrayList<Superuser> superusers = new ArrayList<>();

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

        login.setOnClickListener(x -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            boolean validUser = false;

            for (Superuser superuser : superusers) {
                if (superuser.getUsername().equals(username) && superuser.getPassword().equals(password)) {
                    validUser = true;
                    break;
                }
            }

            if (validUser) {
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.putExtra("superuser", username);
                startActivity(intentHome);
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas, intentalo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        adminPanel.setOnClickListener(x -> {
            Intent intentAdmin = new Intent(this, adminPanel.class);
            intentAdmin.putExtra("superusers", superusers);
            adminActivityResultLauncher.launch(intentAdmin);
        });
    }

    private final ActivityResultLauncher<Intent> adminActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Obtener la lista de superusuarios devuelta
                    Intent data = result.getData();
                    if (data != null) {
                        ArrayList<Superuser> updatedSuperusers = (ArrayList<Superuser>) data.getSerializableExtra("updatedSuperusers");
                        if (updatedSuperusers != null) {
                            superusers.clear();
                            superusers.addAll(updatedSuperusers);

                            for (Superuser item : superusers) {
                                Log.d("Mi app", "Superuser: " + item);
                            }
                        }
                    }
                }
            }
    );
}
