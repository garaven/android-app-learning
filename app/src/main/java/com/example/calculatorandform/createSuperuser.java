package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class createSuperuser extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    Button create;

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

        usernameInput = findViewById(R.id.deleteUsuarioInput);
        passwordInput = findViewById(R.id.contrasenaInput);
        create = findViewById(R.id.crearButton);

        create.setOnClickListener(x -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            Intent intentSendInfo = new Intent();
            intentSendInfo.putExtra("username", username);
            intentSendInfo.putExtra("password", password);
            setResult(Activity.RESULT_OK, intentSendInfo);
            finish();
        });
    }
}