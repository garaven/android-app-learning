package com.example.calculatorandform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Form extends AppCompatActivity {
    EditText name, secondName, age, email, docNumber;
    Button register;
    RadioButton radioMale, radioFemale;
    Spinner spinnerDocTypes;
    String docTypesOptions[] = {"Tarjeta de identidad", "Cédula de ciudadanía", "Pasaporte", "Tarjeta de extranjería", "Permiso especial de permanencia"};

    Spinner spinnerEducationLevel;
    String educationLevelsOptions[] = {"Ninguno", "Primaria", "Secundaria", "Universidad", "Maestria"};

    CheckBox checkboxRock, checkboxPop, checkboxRap, checkboxJazz, checkboxClassical, checkboxOther;
    CheckBox checkboxFootball, checkboxBasketball, checkboxTennis, checkboxRunning, checkboxSwimming, checkboxCycling;

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
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        docNumber = findViewById(R.id.inputDocNumber);
        spinnerDocTypes = findViewById(R.id.spinnerDocType);
        ArrayAdapter<String> adapterDocTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, docTypesOptions);
        spinnerDocTypes.setAdapter(adapterDocTypes);

        register = findViewById(R.id.registerButton);

        spinnerEducationLevel = findViewById(R.id.spinnerEducationLevel);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, educationLevelsOptions);
        spinnerEducationLevel.setAdapter(adapterEducation);

        checkboxRock = findViewById(R.id.checkboxRock);
        checkboxPop = findViewById(R.id.checkboxPop);
        checkboxRap = findViewById(R.id.checkboxRap);
        checkboxJazz = findViewById(R.id.checkboxJazz);
        checkboxClassical = findViewById(R.id.checkboxClassical);
        checkboxOther = findViewById(R.id.checkboxOther);

        checkboxFootball = findViewById(R.id.checkboxFootball);
        checkboxBasketball = findViewById(R.id.checkboxBasketball);
        checkboxTennis = findViewById(R.id.checkboxTennis);
        checkboxRunning = findViewById(R.id.checkboxRunning);
        checkboxSwimming = findViewById(R.id.checkboxSwimming);
        checkboxCycling = findViewById(R.id.checkboxCycling);

        register.setOnClickListener(x -> {
            if (validarFormulario()) {
                String nombre = name.getText().toString();
                String apellido = secondName.getText().toString();
                String edad = age.getText().toString();
                String correo = email.getText().toString();
                String tipoDocumento = spinnerDocTypes.getSelectedItem().toString();
                String numeroDocumento = docNumber.getText().toString();
                String genero = radioMale.isChecked() ? "Masculino" : "Femenino";

                Intent intentSendInfo = new Intent();
                intentSendInfo.putExtra("nombre", nombre);
                intentSendInfo.putExtra("apellido", apellido);
                intentSendInfo.putExtra("edad", edad);
                intentSendInfo.putExtra("correo", correo);
                intentSendInfo.putExtra("tipo-documento", tipoDocumento);
                intentSendInfo.putExtra("numero-documento", numeroDocumento);
                intentSendInfo.putExtra("genero", genero);

                String nivelEducacion = spinnerEducationLevel.getSelectedItem().toString();
                intentSendInfo.putExtra("nivel-educacion", nivelEducacion);

                StringBuilder gustosMusicales = new StringBuilder();
                if (checkboxRock.isChecked()) gustosMusicales.append("Rock,");
                if (checkboxPop.isChecked()) gustosMusicales.append("Pop,");
                if (checkboxRap.isChecked()) gustosMusicales.append("Rap,");
                if (checkboxJazz.isChecked()) gustosMusicales.append("Jazz,");
                if (checkboxClassical.isChecked()) gustosMusicales.append("Clásica,");
                if (checkboxOther.isChecked()) gustosMusicales.append("Otra,");
                if (gustosMusicales.length() > 0)
                    gustosMusicales.setLength(gustosMusicales.length() - 1);
                intentSendInfo.putExtra("gustos-musicales", gustosMusicales.toString());

                StringBuilder deportes = new StringBuilder();
                if (checkboxFootball.isChecked()) deportes.append("Fútbol,");
                if (checkboxBasketball.isChecked()) deportes.append("Baloncesto,");
                if (checkboxTennis.isChecked()) deportes.append("Tenis,");
                if (checkboxRunning.isChecked()) deportes.append("Correr,");
                if (checkboxSwimming.isChecked()) deportes.append("Natación,");
                if (checkboxCycling.isChecked()) deportes.append("Ciclismo,");
                if (deportes.length() > 0)
                    deportes.setLength(deportes.length() - 1);
                intentSendInfo.putExtra("deportes", deportes.toString());

                setResult(Activity.RESULT_OK, intentSendInfo);
                finish();
            }
        });
    }

    private boolean validarFormulario() {
        // Validar nombre
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("El nombre es obligatorio");
            name.requestFocus();
            return false;
        }

        // Validar apellido
        if (TextUtils.isEmpty(secondName.getText())) {
            secondName.setError("El apellido es obligatorio");
            secondName.requestFocus();
            return false;
        }

        // Validar edad
        String edadStr = age.getText().toString();
        if (TextUtils.isEmpty(edadStr)) {
            age.setError("La edad es obligatoria");
            age.requestFocus();
            return false;
        }

        try {
            int edadNum = Integer.parseInt(edadStr);
            if (edadNum <= 0 || edadNum > 120) {
                age.setError("La edad debe estar entre 1 y 120 años");
                age.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            age.setError("La edad debe ser un número válido");
            age.requestFocus();
            return false;
        }

        // Validar email
        String emailStr = email.getText().toString();
        if (TextUtils.isEmpty(emailStr)) {
            email.setError("El correo electrónico es obligatorio");
            email.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("Ingrese un correo electrónico válido");
            email.requestFocus();
            return false;
        }

        // Validar número de documento
        if (TextUtils.isEmpty(docNumber.getText())) {
            docNumber.setError("El número de documento es obligatorio");
            docNumber.requestFocus();
            return false;
        }

        // Validar selección de género
        if (!radioMale.isChecked() && !radioFemale.isChecked()) {
            Toast.makeText(this, "Debe seleccionar un género", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar que al menos se haya seleccionado un gusto musical
        if (!checkboxRock.isChecked() && !checkboxPop.isChecked() && !checkboxRap.isChecked() &&
                !checkboxJazz.isChecked() && !checkboxClassical.isChecked() && !checkboxOther.isChecked()) {
            Toast.makeText(this, "Debe seleccionar al menos un gusto musical", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar que al menos se haya seleccionado un deporte
        if (!checkboxFootball.isChecked() && !checkboxBasketball.isChecked() && !checkboxTennis.isChecked() &&
                !checkboxRunning.isChecked() && !checkboxSwimming.isChecked() && !checkboxCycling.isChecked()) {
            Toast.makeText(this, "Debe seleccionar al menos un deporte", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}