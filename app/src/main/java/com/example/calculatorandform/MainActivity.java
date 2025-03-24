package com.example.calculatorandform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String name, secondName, email;
    Integer age, id;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button calculator = findViewById(R.id.calculatorButton);

        calculator.setOnClickListener(x -> {
            Intent intentCalculator = new Intent(this, Calculator.class);
            startActivity(intentCalculator);
        });

        Button form = findViewById(R.id.formButton);

        form.setOnClickListener(x -> {
            Intent intentForm = new Intent(this, Form.class);
            formActivityResultLauncher.launch(intentForm);
        });

        Button list = findViewById(R.id.listButton);

        list.setOnClickListener(x -> {
            Intent intentList = new Intent(this, UsersTable.class);
            intentList.putExtra("usersList", users);
            startActivity(intentList);
        });

        Button update = findViewById(R.id.updateOption);

        update.setOnClickListener(x -> {
            Intent intentUpdate = new Intent(this, UpdateUser.class);
            intentUpdate.putExtra("usersList", users);
            updateActivityResultLauncher.launch(intentUpdate);
        });
    }

    private final ActivityResultLauncher<Intent> formActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), x -> {
                if (x.getResultCode() == Activity.RESULT_OK) {
                    Intent data = x.getData();
                    name = data.getStringExtra("nombre");
                    secondName = data.getStringExtra("apellido");
                    email = data.getStringExtra("correo");
                    age = Integer.parseInt(data.getStringExtra("edad"));
                    id = 1 + users.size();

                    User newUser = new User(name, secondName, email, age, id);
                    users.add(newUser);

                    for (User item : users) {
                        Log.d("Mi app", String.valueOf(item));
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), x -> {
                if (x.getResultCode() == Activity.RESULT_OK) {
                    // Update the array with the array passed in UpdateUser activity
                    Intent data = x.getData();
                    ArrayList<User> updatedUsers = (ArrayList<User>) data.getSerializableExtra("updatedUsersList");
                    if (updatedUsers != null) {
                        users.clear();
                        users.addAll(updatedUsers);

                        for (User item : users) {
                            Log.d("Mi app", String.valueOf(item));
                        }
                    }
                }
            }
    );
}