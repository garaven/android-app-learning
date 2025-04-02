package com.example.calculatorandform;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity {

    // Componentes de UI
    private TextView idToSearch, idUser, nameUser, secondNameUser, ageUser, emailUser, docNumberUser;
    private RadioButton radioMaleUpdate, radioFemaleUpdate;
    private Button getId, updateUser;
    private Spinner spinnerDocTypes, spinnerEducationLevel;
    private CheckBox checkboxRock, checkboxPop, checkboxRap, checkboxJazz, checkboxClassical, checkboxOther;
    private CheckBox checkboxFootball, checkboxBasketball, checkboxTennis, checkboxRunning, checkboxSwimming, checkboxCycling;

    // Datos
    private User userSearched;
    private final String[] docTypesOptions = {"Tarjeta de identidad", "Cédula de ciudadanía", "Pasaporte", "Tarjeta de extranjería", "Permiso especial de permanencia"};
    private final String[] educationLevelsOptions = {"Ninguno", "Primaria", "Secundaria", "Universidad", "Maestria"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user);
        setupWindowInsets();

        initializeComponents();
        setupSpinners();
        setupButtons();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeComponents() {
        // TextViews
        idToSearch = findViewById(R.id.idSearchInput);
        idUser = findViewById(R.id.idInputUpdate);
        nameUser = findViewById(R.id.nameInputUpdate);
        secondNameUser = findViewById(R.id.secondNameInputUpdate);
        ageUser = findViewById(R.id.ageInputUpdate);
        emailUser = findViewById(R.id.emailInputUpdate);
        docNumberUser = findViewById(R.id.docNumberInputUpdate);

        // RadioButtons
        radioMaleUpdate = findViewById(R.id.radioMaleUpdate);
        radioFemaleUpdate = findViewById(R.id.radioFemaleUpdate);

        // Spinners
        spinnerDocTypes = findViewById(R.id.spinnerDocTypeUpdate);
        spinnerEducationLevel = findViewById(R.id.spinnerEducationLevel);

        // CheckBoxes Música
        checkboxRock = findViewById(R.id.checkboxRock);
        checkboxPop = findViewById(R.id.checkboxPop);
        checkboxRap = findViewById(R.id.checkboxRap);
        checkboxJazz = findViewById(R.id.checkboxJazz);
        checkboxClassical = findViewById(R.id.checkboxClassical);
        checkboxOther = findViewById(R.id.checkboxOther);

        // CheckBoxes Deportes
        checkboxFootball = findViewById(R.id.checkboxFootball);
        checkboxBasketball = findViewById(R.id.checkboxBasketball);
        checkboxTennis = findViewById(R.id.checkboxTennis);
        checkboxRunning = findViewById(R.id.checkboxRunning);
        checkboxSwimming = findViewById(R.id.checkboxSwimming);
        checkboxCycling = findViewById(R.id.checkboxCycling);

        // Botones
        getId = findViewById(R.id.searchUserButton);
        updateUser = findViewById(R.id.updateUserButton);
    }

    private void setupSpinners() {
        ArrayAdapter<String> docAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, docTypesOptions);
        docAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDocTypes.setAdapter(docAdapter);

        ArrayAdapter<String> eduAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, educationLevelsOptions);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducationLevel.setAdapter(eduAdapter);
    }

    private void setupButtons() {
        ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("usersList");

        getId.setOnClickListener(v -> handleSearchUser(users));
        updateUser.setOnClickListener(v -> handleUpdateUser(users));
    }

    private void handleSearchUser(ArrayList<User> users) {
        try {
            int searchId = Integer.parseInt(idToSearch.getText().toString().trim());
            userSearched = findUserById(users, searchId);

            if (userSearched != null) {
                displayUserData();
                enableFields(true);
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID debe ser numérico", Toast.LENGTH_SHORT).show();
        }
    }

    private User findUserById(ArrayList<User> users, int id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    private void displayUserData() {
        // Datos básicos
        radioMaleUpdate.setChecked(userSearched.getGender().equals("Masculino"));
        radioFemaleUpdate.setChecked(userSearched.getGender().equals("Femenino"));
        idUser.setText(String.valueOf(userSearched.getId()));
        nameUser.setText(userSearched.getName());
        secondNameUser.setText(userSearched.getSecondName());
        ageUser.setText(String.valueOf(userSearched.getAge()));
        emailUser.setText(userSearched.getEmail());
        docNumberUser.setText(String.valueOf(userSearched.getDocNumber()));

        // Spinners
        setSpinnerSelection(spinnerDocTypes, docTypesOptions, userSearched.getDocType());
        setSpinnerSelection(spinnerEducationLevel, educationLevelsOptions, userSearched.getEducationLevel());

        // CheckBoxes
        setCheckboxSelections(userSearched.getMusicalTastes(), userSearched.getSports());
    }

    private void setSpinnerSelection(Spinner spinner, String[] options, String value) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setCheckboxSelections(String musicalTastes, String sports) {
        resetCheckboxes();
        setMusicalTastes(musicalTastes);
        setSports(sports);
    }

    private void resetCheckboxes() {
        // Música
        checkboxRock.setChecked(false);
        checkboxPop.setChecked(false);
        checkboxRap.setChecked(false);
        checkboxJazz.setChecked(false);
        checkboxClassical.setChecked(false);
        checkboxOther.setChecked(false);

        // Deportes
        checkboxFootball.setChecked(false);
        checkboxBasketball.setChecked(false);
        checkboxTennis.setChecked(false);
        checkboxRunning.setChecked(false);
        checkboxSwimming.setChecked(false);
        checkboxCycling.setChecked(false);
    }

    private void setMusicalTastes(String tastes) {
        if (tastes != null && !tastes.isEmpty()) {
            for (String taste : tastes.split(",")) {
                switch (taste.trim()) {
                    case "Rock": checkboxRock.setChecked(true); break;
                    case "Pop": checkboxPop.setChecked(true); break;
                    case "Rap": checkboxRap.setChecked(true); break;
                    case "Jazz": checkboxJazz.setChecked(true); break;
                    case "Clásica": checkboxClassical.setChecked(true); break;
                    case "Otra": checkboxOther.setChecked(true); break;
                }
            }
        }
    }

    private void setSports(String sports) {
        if (sports != null && !sports.isEmpty()) {
            for (String sport : sports.split(",")) {
                switch (sport.trim()) {
                    case "Fútbol": checkboxFootball.setChecked(true); break;
                    case "Baloncesto": checkboxBasketball.setChecked(true); break;
                    case "Tenis": checkboxTennis.setChecked(true); break;
                    case "Correr": checkboxRunning.setChecked(true); break;
                    case "Natación": checkboxSwimming.setChecked(true); break;
                    case "Ciclismo": checkboxCycling.setChecked(true); break;
                }
            }
        }
    }

    private void enableFields(boolean enable) {
        // RadioButtons
        radioMaleUpdate.setEnabled(enable);
        radioFemaleUpdate.setEnabled(enable);

        // EditTexts
        nameUser.setEnabled(enable);
        secondNameUser.setEnabled(enable);
        ageUser.setEnabled(enable);
        emailUser.setEnabled(enable);
        docNumberUser.setEnabled(enable);

        // Spinners
        spinnerDocTypes.setEnabled(enable);
        spinnerEducationLevel.setEnabled(enable);

        // CheckBoxes
        checkboxRock.setEnabled(enable);
        checkboxPop.setEnabled(enable);
        checkboxRap.setEnabled(enable);
        checkboxJazz.setEnabled(enable);
        checkboxClassical.setEnabled(enable);
        checkboxOther.setEnabled(enable);
        checkboxFootball.setEnabled(enable);
        checkboxBasketball.setEnabled(enable);
        checkboxTennis.setEnabled(enable);
        checkboxRunning.setEnabled(enable);
        checkboxSwimming.setEnabled(enable);
        checkboxCycling.setEnabled(enable);

        // Botón
        updateUser.setEnabled(enable);
    }

    private void handleUpdateUser(ArrayList<User> users) {
        try {
            int targetId = Integer.parseInt(idToSearch.getText().toString());
            for (User user : users) {
                if (user.getId() == targetId) {
                    updateUserData(user);
                    break;
                }
            }
            returnResult(users);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserData(User user) {
        user.setName(nameUser.getText().toString());
        user.setSecondName(secondNameUser.getText().toString());
        user.setAge(Integer.parseInt(ageUser.getText().toString()));
        user.setEmail(emailUser.getText().toString());
        user.setDocNumber(Long.parseLong(docNumberUser.getText().toString()));
        user.setDocType(spinnerDocTypes.getSelectedItem().toString());
        user.setGender(radioMaleUpdate.isChecked() ? "Masculino" : "Femenino");
        user.setEducationLevel(spinnerEducationLevel.getSelectedItem().toString());
        user.setMusicalTastes(getSelectedMusicalTastes());
        user.setSports(getSelectedSports());
    }

    private String getSelectedMusicalTastes() {
        StringBuilder sb = new StringBuilder();
        if (checkboxRock.isChecked()) sb.append("Rock,");
        if (checkboxPop.isChecked()) sb.append("Pop,");
        if (checkboxRap.isChecked()) sb.append("Rap,");
        if (checkboxJazz.isChecked()) sb.append("Jazz,");
        if (checkboxClassical.isChecked()) sb.append("Clásica,");
        if (checkboxOther.isChecked()) sb.append("Otra,");
        return sb.length() > 0 ? sb.substring(0, sb.length()-1) : "";
    }

    private String getSelectedSports() {
        StringBuilder sb = new StringBuilder();
        if (checkboxFootball.isChecked()) sb.append("Fútbol,");
        if (checkboxBasketball.isChecked()) sb.append("Baloncesto,");
        if (checkboxTennis.isChecked()) sb.append("Tenis,");
        if (checkboxRunning.isChecked()) sb.append("Correr,");
        if (checkboxSwimming.isChecked()) sb.append("Natación,");
        if (checkboxCycling.isChecked()) sb.append("Ciclismo,");
        return sb.length() > 0 ? sb.substring(0, sb.length()-1) : "";

        
    }

    private void returnResult(ArrayList<User> users) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedUsersList", users);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}