package com.example.calculatorandform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity {
    // Declaracion de variables
    TextView idToSearch, idUser, nameUser, secondNameUser, ageUser, emailUser, docNumberUser;
    RadioButton radioMaleUpdate, radioFemaleUpdate;
    Button getId, updateUser;
    User userSearched;
    Spinner spinnerDocTypes;
    String docTypesOptions[] = {"Tarjeta de identidad", "Cédula de ciudadanía", "Pasaporte", "Tarjeta de extranjería", "Permiso especial de permanencia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Inicializacion de variables

        idToSearch = findViewById(R.id.idSearchInput);
        idUser = findViewById(R.id.idInputUpdate);
        nameUser = findViewById(R.id.nameInputUpdate);
        secondNameUser = findViewById(R.id.secondNameInputUpdate);
        ageUser = findViewById(R.id.ageInputUpdate);
        emailUser = findViewById(R.id.emailInputUpdate);
        docNumberUser = findViewById(R.id.docNumberInputUpdate);
        spinnerDocTypes = findViewById(R.id.spinnerDocTypeUpdate);
        radioMaleUpdate = findViewById(R.id.radioMaleUpdate);
        radioFemaleUpdate = findViewById(R.id.radioFemaleUpdate);
        ArrayAdapter<String> adapterDocTypes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, docTypesOptions);
        spinnerDocTypes.setAdapter(adapterDocTypes);

        getId = findViewById(R.id.searchUserButton);
        updateUser = findViewById(R.id.updateUserButton);

        ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("usersList");


        // Eventos

        getId.setOnClickListener(x -> {
            int searchId = Integer.parseInt(idToSearch.getText().toString().trim());
            for (User user : users) {
                if (user.id == searchId) {
                    userSearched = user;
                    break;
                }
            }

            // Aca se ponen los datos en los inputs/elementos para actualizarlos
            if (userSearched.gender.equals("Masculino")) { // Se evalua con equals, no con ==
                radioMaleUpdate.setChecked(true);
            } else {
                radioFemaleUpdate.setChecked(true);
            }
            idUser.setText(String.valueOf(userSearched.id));
            nameUser.setText(userSearched.name);
            secondNameUser.setText(userSearched.secondName);
            ageUser.setText(String.valueOf(userSearched.age));
            emailUser.setText(userSearched.email);
            docNumberUser.setText(String.valueOf(userSearched.docNumber));
            switch (userSearched.docType) {
                case("Tarjeta de identidad"): spinnerDocTypes.setSelection(0);
                    break;
                case("Cédula de ciudadanía"): spinnerDocTypes.setSelection(1);
                    break;
                case("Pasaporte"): spinnerDocTypes.setSelection(2);
                    break;
                case("Tarjeta de extranjería"): spinnerDocTypes.setSelection(3);
                    break;
                case("Permiso especial de permanencia"): spinnerDocTypes.setSelection(4);
                    break;
            }

            // Se habilitan los campos para actualizarse, excepto el campo del id
            radioMaleUpdate.setEnabled(true);
            radioFemaleUpdate.setEnabled(true);
            nameUser.setEnabled(true);
            secondNameUser.setEnabled(true);
            ageUser.setEnabled(true);
            emailUser.setEnabled(true);
            updateUser.setEnabled(true);
            docNumberUser.setEnabled(true);
            spinnerDocTypes.setEnabled(true);
        });

        updateUser.setOnClickListener(x -> {
            for (User user : users) {
                if (user.id == Integer.parseInt(idToSearch.getText().toString())) {
                    user.setName(nameUser.getText().toString());
                    user.setSecondName(secondNameUser.getText().toString());
                    user.setAge(Integer.parseInt(ageUser.getText().toString()));
                    user.setEmail(emailUser.getText().toString());
                    user.setDocNumber(Long.parseLong(docNumberUser.getText().toString()));
                    user.setDocType(spinnerDocTypes.getSelectedItem().toString());
                    if (radioMaleUpdate.isChecked()) {
                        user.setGender("Masculino");
                    }
                    if (radioFemaleUpdate.isChecked()) {
                        user.setGender("Feminino");
                    }
                    break;
                }
            }

            // Aca se pasa el array con los datos actualizados al MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedUsersList", users);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
