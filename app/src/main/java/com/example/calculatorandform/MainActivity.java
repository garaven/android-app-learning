package com.example.calculatorandform;

import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
            startActivityForResult(intentForm, 500);
        });

        Button list = findViewById(R.id.listButton);

        list.setOnClickListener(x -> {
            Intent intentList = new Intent(this, UsersTable.class);
            intentList.putExtra("usersList", users);
            startActivity(intentList);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);

        // It gets the form's data, create an object with that data and add the object to the arraylist
        // and it generates a new id for every user, as well

        if (requestCode == 500) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle datos = data.getExtras();
                name = datos.getString("nombre");
                secondName = datos.getString("apellido");
                email = datos.getString("correo");
                age = Integer.parseInt(datos.getString("edad"));
                id = 1 + users.size();
                User nuevoUsuario = new User(name, secondName, email, age, id);
                users.add(nuevoUsuario);

                // To see the Array content
                for (User item : users) {
                    Log.d("Mi app", String.valueOf(item));
                }
            }
        }
    }
}