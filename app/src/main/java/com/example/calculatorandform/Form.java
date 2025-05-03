package com.example.calculatorandform;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class Form extends AppCompatActivity {
    private EditText name, secondName, age, email, docNumber;
    private Button register;
    private RadioButton radioMale, radioFemale;
    private Spinner spinnerDocTypes, spinnerEducationLevel;
    private CheckBox checkboxRock, checkboxPop, checkboxRap, checkboxJazz, checkboxClassical, checkboxOther;
    private CheckBox checkboxFootball, checkboxBasketball, checkboxTennis, checkboxRunning, checkboxSwimming, checkboxCycling;

    private AdminSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private String superusername;

    private static final String TEMP_FILE = "form_temp.txt";

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

        dbHelper = AdminSQLiteOpenHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets s = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(s.left, s.top, s.right, s.bottom);
            return insets;
        });

        name                = findViewById(R.id.nameInput);
        secondName          = findViewById(R.id.secondNameInput);
        age                 = findViewById(R.id.ageInput);
        email               = findViewById(R.id.emailInput);
        docNumber           = findViewById(R.id.inputDocNumber);
        radioMale           = findViewById(R.id.radioMale);
        radioFemale         = findViewById(R.id.radioFemale);
        register            = findViewById(R.id.registerButton);

        spinnerDocTypes     = findViewById(R.id.spinnerDocType);
        spinnerEducationLevel = findViewById(R.id.spinnerEducationLevel);

        checkboxRock        = findViewById(R.id.checkboxRock);
        checkboxPop         = findViewById(R.id.checkboxPop);
        checkboxRap         = findViewById(R.id.checkboxRap);
        checkboxJazz        = findViewById(R.id.checkboxJazz);
        checkboxClassical   = findViewById(R.id.checkboxClassical);
        checkboxOther       = findViewById(R.id.checkboxOther);

        checkboxFootball    = findViewById(R.id.checkboxFootball);
        checkboxBasketball  = findViewById(R.id.checkboxBasketball);
        checkboxTennis      = findViewById(R.id.checkboxTennis);
        checkboxRunning     = findViewById(R.id.checkboxRunning);
        checkboxSwimming    = findViewById(R.id.checkboxSwimming);
        checkboxCycling     = findViewById(R.id.checkboxCycling);

        spinnerDocTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DOC_TYPES));
        spinnerDocTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { guardarTemp(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerEducationLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EDUCATION_LEVELS));
        spinnerEducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { guardarTemp(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        restaurarTemp();

        TextWatcher saver = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { guardarTemp(); }
        };
        name.addTextChangedListener(saver);
        secondName.addTextChangedListener(saver);
        age.addTextChangedListener(saver);
        email.addTextChangedListener(saver);
        docNumber.addTextChangedListener(saver);

        radioMale.setOnCheckedChangeListener((btn, checked) -> { if (checked) guardarTemp(); });
        radioFemale.setOnCheckedChangeListener((btn, checked) -> { if (checked) guardarTemp(); });

        CompoundButton.OnCheckedChangeListener cbSaver = (btn, checked) -> guardarTemp();
        checkboxRock.setOnCheckedChangeListener(cbSaver);
        checkboxPop.setOnCheckedChangeListener(cbSaver);
        checkboxRap.setOnCheckedChangeListener(cbSaver);
        checkboxJazz.setOnCheckedChangeListener(cbSaver);
        checkboxClassical.setOnCheckedChangeListener(cbSaver);
        checkboxOther.setOnCheckedChangeListener(cbSaver);
        checkboxFootball.setOnCheckedChangeListener(cbSaver);
        checkboxBasketball.setOnCheckedChangeListener(cbSaver);
        checkboxTennis.setOnCheckedChangeListener(cbSaver);
        checkboxRunning.setOnCheckedChangeListener(cbSaver);
        checkboxSwimming.setOnCheckedChangeListener(cbSaver);
        checkboxCycling.setOnCheckedChangeListener(cbSaver);

        register.setOnClickListener(v -> {
            // checkpoint
            guardarTemp();
            if (!validarFormulario()) return;

            String nombre = name.getText().toString().trim();
            String apellido = secondName.getText().toString().trim();
            int edadNum = Integer.parseInt(age.getText().toString().trim());
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
            cvUser.put("age", edadNum);
            cvUser.put("docNumber", numeroDoc);
            cvUser.put("educationLevel", nivelEducacion);
            cvUser.put("createdBy", superusername);

            long userId = db.insert("users", null, cvUser);
            if (userId == -1) {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_LONG).show();
                return;
            }

            // preferencias
            StringBuilder gustos = new StringBuilder();
            if (checkboxRock.isChecked())      gustos.append("Rock, ");
            if (checkboxPop.isChecked())       gustos.append("Pop, ");
            if (checkboxRap.isChecked())       gustos.append("Rap, ");
            if (checkboxJazz.isChecked())      gustos.append("Jazz, ");
            if (checkboxClassical.isChecked()) gustos.append("Clásica, ");
            if (checkboxOther.isChecked())     gustos.append("Reggaeton, ");
            if (gustos.length()>0) gustos.setLength(gustos.length()-2);

            StringBuilder deps = new StringBuilder();
            if (checkboxFootball.isChecked())   deps.append("Fútbol, ");
            if (checkboxBasketball.isChecked()) deps.append("Baloncesto, ");
            if (checkboxTennis.isChecked())     deps.append("Tenis, ");
            if (checkboxRunning.isChecked())    deps.append("Correr, ");
            if (checkboxSwimming.isChecked())   deps.append("Natación, ");
            if (checkboxCycling.isChecked())    deps.append("Ciclismo, ");
            if (deps.length()>0) deps.setLength(deps.length()-2);

            ContentValues cvPref = new ContentValues();
            cvPref.put("userId", userId);
            cvPref.put("musicTaste", gustos.toString());
            cvPref.put("sports", deps.toString());
            db.insert("preferences", null, cvPref);

            // hecho: limpia y retorna
            deleteFile(TEMP_FILE);
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

            Intent out = new Intent();
            out.putExtra("nombre", nombre);
            out.putExtra("apellido", apellido);
            out.putExtra("edad", String.valueOf(edadNum));
            out.putExtra("correo", correo);
            out.putExtra("tipo-documento", tipoDocumento);
            out.putExtra("numero-documento", String.valueOf(numeroDoc));
            out.putExtra("genero", genero);
            out.putExtra("nivel-educacion", nivelEducacion);
            out.putExtra("gustos-musicales", gustos.toString());
            out.putExtra("deportes", deps.toString());
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

    private void guardarTemp() {
        try (FileOutputStream fos = openFileOutput(TEMP_FILE, MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("name=" + name.getText().toString().trim());             writer.newLine();
            writer.write("secondName=" + secondName.getText().toString().trim()); writer.newLine();
            writer.write("age=" + age.getText().toString().trim());               writer.newLine();
            writer.write("email=" + email.getText().toString().trim());           writer.newLine();
            writer.write("docNumber=" + docNumber.getText().toString().trim());   writer.newLine();
            writer.write("gender=" + (radioMale.isChecked() ? "Masculino" : radioFemale.isChecked() ? "Femenino" : "")); writer.newLine();
            writer.write("docType=" + spinnerDocTypes.getSelectedItem().toString()); writer.newLine();
            writer.write("educationLevel=" + spinnerEducationLevel.getSelectedItem().toString()); writer.newLine();

            StringBuilder mus = new StringBuilder();
            if (checkboxRock.isChecked())      mus.append("Rock,");
            if (checkboxPop.isChecked())       mus.append("Pop,");
            if (checkboxRap.isChecked())       mus.append("Rap,");
            if (checkboxJazz.isChecked())      mus.append("Jazz,");
            if (checkboxClassical.isChecked()) mus.append("Clásica,");
            if (checkboxOther.isChecked())     mus.append("Reggaeton,");
            writer.write("music=" + mus.toString()); writer.newLine();

            StringBuilder deps = new StringBuilder();
            if (checkboxFootball.isChecked())   deps.append("Fútbol,");
            if (checkboxBasketball.isChecked()) deps.append("Baloncesto,");
            if (checkboxTennis.isChecked())     deps.append("Tenis,");
            if (checkboxRunning.isChecked())    deps.append("Correr,");
            if (checkboxSwimming.isChecked())   deps.append("Natación,");
            if (checkboxCycling.isChecked())    deps.append("Ciclismo,");
            writer.write("sports=" + deps.toString());

        } catch (IOException ex) {
            Log.e("Form", "Error saving temp file", ex);
        }
    }

    private void restaurarTemp() {
        File file = new File(getFilesDir(), TEMP_FILE);
        if (!file.exists()) return;

        try (FileInputStream fis = openFileInput(TEMP_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;
                String key = parts[0], val = parts[1];

                switch (key) {
                    case "name":
                        name.setText(val);
                        break;
                    case "secondName":
                        secondName.setText(val);
                        break;
                    case "age":
                        age.setText(val);
                        break;
                    case "email":
                        email.setText(val);
                        break;
                    case "docNumber":
                        docNumber.setText(val);
                        break;
                    case "gender":
                        radioMale.setChecked("Masculino".equals(val));
                        radioFemale.setChecked("Femenino".equals(val));
                        break;
                    case "docType":
                        int docIdx = Arrays.asList(DOC_TYPES).indexOf(val);
                        if (docIdx >= 0) spinnerDocTypes.setSelection(docIdx);
                        break;
                    case "educationLevel":
                        int eduIdx = Arrays.asList(EDUCATION_LEVELS).indexOf(val);
                        if (eduIdx >= 0) spinnerEducationLevel.setSelection(eduIdx);
                        break;
                    case "music":
                        List<String> musList = Arrays.asList(val.split(","));
                        checkboxRock.setChecked(musList.contains("Rock"));
                        checkboxPop.setChecked(musList.contains("Pop"));
                        checkboxRap.setChecked(musList.contains("Rap"));
                        checkboxJazz.setChecked(musList.contains("Jazz"));
                        checkboxClassical.setChecked(musList.contains("Clásica"));
                        checkboxOther.setChecked(musList.contains("Reggaeton"));
                        break;
                    case "sports":
                        List<String> spList = Arrays.asList(val.split(","));
                        checkboxFootball.setChecked(spList.contains("Fútbol"));
                        checkboxBasketball.setChecked(spList.contains("Baloncesto"));
                        checkboxTennis.setChecked(spList.contains("Tenis"));
                        checkboxRunning.setChecked(spList.contains("Correr"));
                        checkboxSwimming.setChecked(spList.contains("Natación"));
                        checkboxCycling.setChecked(spList.contains("Ciclismo"));
                        break;
                }
            }
        } catch (IOException ex) {
            Log.e("Form", "Error restoring temp file", ex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
