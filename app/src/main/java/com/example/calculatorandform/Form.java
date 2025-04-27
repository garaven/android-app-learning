package com.example.calculatorandform;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    private EditText name, secondName, age, email, docNumber;
    private Button register;
    private RadioButton radioMale, radioFemale;
    private Spinner spinnerDocTypes, spinnerEducationLevel;
    private CheckBox checkboxRock, checkboxPop, checkboxRap, checkboxJazz, checkboxClassical, checkboxOther;
    private CheckBox checkboxFootball, checkboxBasketball, checkboxTennis, checkboxRunning, checkboxSwimming, checkboxCycling;

    private AdminSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private int currentSuperuserId = 1;
    private String superusername;

    private static final String[] DOC_TYPES = {
            "Tarjeta de identidad",
            "Cédula de ciudadanía",
            "Pasaporte",
            "Tarjeta de extranjería",
            "Permiso especial de permanencia"
    };

    private static final String[] EDUCATION_LEVELS = {
            "Ninguno",
            "Primaria",
            "Secundaria",
            "Universidad",
            "Maestría"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        superusername = getIntent().getStringExtra("superuser");
        Log.d("Form", "Nombre del superusuario recibido: " + superusername);

        if (superusername != null) {
            currentSuperuserId = 1;
        }

        dbHelper = AdminSQLiteOpenHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets s = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(s.left, s.top, s.right, s.bottom);
            return insets;
        });

        name = findViewById(R.id.nameInput);
        secondName = findViewById(R.id.secondNameInput);
        age = findViewById(R.id.ageInput);
        email = findViewById(R.id.emailInput);
        docNumber = findViewById(R.id.inputDocNumber);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        register = findViewById(R.id.registerButton);

        spinnerDocTypes = findViewById(R.id.spinnerDocType);
        spinnerDocTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DOC_TYPES));

        spinnerEducationLevel = findViewById(R.id.spinnerEducationLevel);
        spinnerEducationLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EDUCATION_LEVELS));

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

        register.setOnClickListener(v -> {
            if (!validarFormulario()) {
                return;
            }

            String nombre = name.getText().toString().trim();
            String apellido = secondName.getText().toString().trim();
            int edad = Integer.parseInt(age.getText().toString().trim());
            String correo = email.getText().toString().trim();
            int numeroDoc = Integer.parseInt(docNumber.getText().toString().trim());
            String tipoDocumento = spinnerDocTypes.getSelectedItem().toString();
            String genero = radioMale.isChecked() ? "Masculino" : "Femenino";
            String nivelEducacion = spinnerEducationLevel.getSelectedItem().toString();

            ContentValues cvUser = new ContentValues();
            cvUser.put("name", nombre);
            cvUser.put("secondName", apellido);
            cvUser.put("email", correo);
            cvUser.put("docType", tipoDocumento);
            cvUser.put("gender", genero);
            cvUser.put("age", edad);
            cvUser.put("docNumber", numeroDoc);
            cvUser.put("educationLevel", nivelEducacion);
            cvUser.put("createdBy", superusername);

            long userId = db.insert("users", null, cvUser);
            if (userId == -1) {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_LONG).show();
                return;
            }

            StringBuilder gustosMusicales = new StringBuilder();
            if (checkboxRock.isChecked()) gustosMusicales.append("Rock, ");
            if (checkboxPop.isChecked()) gustosMusicales.append("Pop, ");
            if (checkboxRap.isChecked()) gustosMusicales.append("Rap, ");
            if (checkboxJazz.isChecked()) gustosMusicales.append("Jazz, ");
            if (checkboxClassical.isChecked()) gustosMusicales.append("Clásica, ");
            if (checkboxOther.isChecked()) gustosMusicales.append("Reggaeton, ");
            if (gustosMusicales.length() > 0) {
                gustosMusicales.delete(gustosMusicales.length() - 2, gustosMusicales.length());
            }

            StringBuilder deportes = new StringBuilder();
            if (checkboxFootball.isChecked()) deportes.append("Fútbol, ");
            if (checkboxBasketball.isChecked()) deportes.append("Baloncesto, ");
            if (checkboxTennis.isChecked()) deportes.append("Tenis, ");
            if (checkboxRunning.isChecked()) deportes.append("Correr, ");
            if (checkboxSwimming.isChecked()) deportes.append("Natación, ");
            if (checkboxCycling.isChecked()) deportes.append("Ciclismo, ");
            if (deportes.length() > 0) {
                deportes.delete(deportes.length() - 2, deportes.length());
            }

            ContentValues cvPreferences = new ContentValues();
            cvPreferences.put("userId", userId);
            cvPreferences.put("musicTaste", gustosMusicales.toString());
            cvPreferences.put("sports", deportes.toString());
            db.insert("preferences", null, cvPreferences);

            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

            Intent out = new Intent();
            out.putExtra("nombre", nombre);
            out.putExtra("apellido", apellido);
            out.putExtra("edad", String.valueOf(edad));
            out.putExtra("correo", correo);
            out.putExtra("tipo-documento", tipoDocumento);
            out.putExtra("numero-documento", String.valueOf(numeroDoc));
            out.putExtra("genero", genero);
            out.putExtra("nivel-educacion", nivelEducacion);
            out.putExtra("gustos-musicales", gustosMusicales.toString());
            out.putExtra("deportes", deportes.toString());

            setResult(Activity.RESULT_OK, out);
            finish();
        });
    }

    private boolean validarFormulario() {
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("El nombre es obligatorio");
            name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(secondName.getText())) {
            secondName.setError("El apellido es obligatorio");
            secondName.requestFocus();
            return false;
        }
        String e = age.getText().toString().trim();
        if (TextUtils.isEmpty(e)) {
            age.setError("La edad es obligatoria");
            age.requestFocus();
            return false;
        }
        try {
            int val = Integer.parseInt(e);
            if (val <= 0 || val > 120) {
                age.setError("La edad debe estar entre 1 y 120 años");
                age.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            age.setError("La edad debe ser un número válido");
            age.requestFocus();
            return false;
        }
        String mail = email.getText().toString().trim();
        if (TextUtils.isEmpty(mail) || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Ingrese un correo válido");
            email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(docNumber.getText())) {
            docNumber.setError("El número de documento es obligatorio");
            docNumber.requestFocus();
            return false;
        }
        if (!radioMale.isChecked() && !radioFemale.isChecked()) {
            Toast.makeText(this, "Debe seleccionar un género", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(checkboxRock.isChecked() || checkboxPop.isChecked() || checkboxRap.isChecked() ||
                checkboxJazz.isChecked() || checkboxClassical.isChecked() || checkboxOther.isChecked())) {
            Toast.makeText(this, "Seleccione al menos un gusto musical", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(checkboxFootball.isChecked() || checkboxBasketball.isChecked() || checkboxTennis.isChecked() ||
                checkboxRunning.isChecked() || checkboxSwimming.isChecked() || checkboxCycling.isChecked())) {
            Toast.makeText(this, "Seleccione al menos un deporte", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
