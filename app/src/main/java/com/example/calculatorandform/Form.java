package com.example.calculatorandform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Form extends AppCompatActivity {
    // Declaracion de variables
    EditText name, secondName, age, email, docNumber;
    Button register;
    RadioButton radioMale, radioFemale;
    Spinner spinnerDocTypes;
    String docTypesOptions[] = {"Tarjeta de identidad", "Cédula de ciudadanía", "Pasaporte", "Tarjeta de extranjería", "Permiso especial de permanencia"};

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


        // Inicializacion de variables

        name = findViewById(R.id.nameInput);
        secondName = findViewById(R.id.secondNameInput);
        age = findViewById(R.id.ageInput);
        email = findViewById(R.id.emailInput);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        docNumber = findViewById(R.id.inputDocNumber);
        spinnerDocTypes = findViewById(R.id.spinnerDocType);
        ArrayAdapter<String> adapterDocTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, docTypesOptions);
        spinnerDocTypes.setAdapter(adapterDocTypes);

        register = findViewById(R.id.registerButton);


        // Eventos

        register.setOnClickListener(x -> {
            // Validar que todos los campos hayan sido completados o seleccionados ⬇️

            String nombre = name.getText().toString();
            String apellido = secondName.getText().toString();
            String edad = age.getText().toString();
            String correo = email.getText().toString();
            String tipoDocumento = spinnerDocTypes.getSelectedItem().toString();
            String numeroDocumento = docNumber.getText().toString();
            // Operador ternario para acortar codigo 😎
            String genero = radioMale.isChecked() ? "Masculino" : "Femenino";

            Intent intentSendInfo = new Intent();
            intentSendInfo.putExtra("nombre", nombre);
            intentSendInfo.putExtra("apellido", apellido);
            intentSendInfo.putExtra("edad", edad);
            intentSendInfo.putExtra("correo", correo);
            intentSendInfo.putExtra("tipo-documento", tipoDocumento);
            intentSendInfo.putExtra("numero-documento", numeroDocumento);
            intentSendInfo.putExtra("genero", genero);
            setResult(Activity.RESULT_OK, intentSendInfo);
            finish();
        });
    }
}