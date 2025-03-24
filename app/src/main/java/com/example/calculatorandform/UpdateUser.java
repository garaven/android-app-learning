package com.example.calculatorandform;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UpdateUser extends AppCompatActivity {

    TextView idToSearch, idUser, nameUser, secondNameUser, ageUser, emailUser;
    Button getId, updateUser;
    User userSearched;

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

        idToSearch = findViewById(R.id.idSearchInput);
        idUser = findViewById(R.id.idInputUpdate);
        nameUser = findViewById(R.id.nameInputUpdate);
        secondNameUser = findViewById(R.id.secondNameInputUpdate);
        ageUser = findViewById(R.id.ageInputUpdate);
        emailUser = findViewById(R.id.emailInputUpdate);

        getId = findViewById(R.id.searchUserButton);
        updateUser = findViewById(R.id.updateUserButton);

        ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("usersList");

        getId.setOnClickListener(x -> {
            int searchId = Integer.parseInt(idToSearch.getText().toString().trim());
            for (User user : users) {
                if (user.id == searchId) {
                    userSearched = user;
                    break;
                }
            }

            idUser.setText(String.valueOf(userSearched.id));
            nameUser.setText(userSearched.name);
            secondNameUser.setText(userSearched.secondName);
            ageUser.setText(String.valueOf(userSearched.age));
            emailUser.setText(userSearched.email);

            nameUser.setEnabled(true);
            secondNameUser.setEnabled(true);
            ageUser.setEnabled(true);
            emailUser.setEnabled(true);
            updateUser.setEnabled(true);

            idToSearch.setEnabled(false);
            getId.setEnabled(false);
        });

        updateUser.setOnClickListener(x -> {
            for (User user : users) {
                if (user.id == Integer.parseInt(idToSearch.getText().toString())) {
                    user.setName(nameUser.getText().toString());
                    user.setSecondName(secondNameUser.getText().toString());
                    user.setAge(Integer.parseInt(ageUser.getText().toString()));
                    user.setEmail(emailUser.getText().toString());
                    break;
                }
            }

            // It passes the updated array to Main activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedUsersList", users);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
