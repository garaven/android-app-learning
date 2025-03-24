package com.example.calculatorandform;

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

public class Form extends AppCompatActivity {

    EditText name, secondName, age, email;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.nameInput);
        secondName = findViewById(R.id.secondNameInput);
        age = findViewById(R.id.ageInput);
        email = findViewById(R.id.emailInput);

        register = findViewById(R.id.registerButton);

        register.setOnClickListener(x -> {
            String nombre = name.getText().toString();
            String apellido = secondName.getText().toString();
            String edad = age.getText().toString();
            String correo = email.getText().toString();

            Intent intentSendInfo = new Intent();
            intentSendInfo.putExtra("nombre", nombre);
            intentSendInfo.putExtra("apellido", apellido);
            intentSendInfo.putExtra("edad", edad);
            intentSendInfo.putExtra("correo", correo);
            setResult(Activity.RESULT_OK, intentSendInfo);
            finish();
        });
    }
}