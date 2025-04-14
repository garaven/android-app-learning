package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class adminPanel extends AppCompatActivity {

    Button create, list, update, delete;
    ArrayList<Superuser> superusers = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);

        if (getIntent().hasExtra("superusers")) {
            superusers = (ArrayList<Superuser>) getIntent().getSerializableExtra("superusers");
        }

        create = findViewById(R.id.adminCreateButton);
        list = findViewById(R.id.adminListButton);
        update = findViewById(R.id.adminUpdateButton);
        delete = findViewById(R.id.adminDeleteButton);

        create.setOnClickListener(x -> {
            Intent intentCreate = new Intent(this, createSuperuser.class);
            createUserActivityResultLauncher.launch(intentCreate);
        });

        list.setOnClickListener(x -> {
            Intent intentList = new Intent(this, superusersList.class);
            intentList.putExtra("superusers", superusers);
            startActivity(intentList);
        });

        update.setOnClickListener(x -> {
            Intent intentUpdate = new Intent(this, updateSuperuser.class);
            intentUpdate.putExtra("superusers", superusers);
            updateUserActivityResultLauncher.launch(intentUpdate);
        });

        delete.setOnClickListener(x -> {
            Intent intentDelete = new Intent(this, deleteSuperuser.class);
            intentDelete.putExtra("superusers", superusers);
            deleteUserActivityResultLauncher.launch(intentDelete);
        });
    }

    private final ActivityResultLauncher<Intent> createUserActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        String password = data.getStringExtra("password");

                        Superuser newSuperuser = new Superuser(username, password);
                        superusers.add(newSuperuser);

                        for (Superuser item : superusers) {
                            Log.d("Mi app", String.valueOf(item));
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> updateUserActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ArrayList<Superuser> updatedUsers = (ArrayList<Superuser>) data.getSerializableExtra("updatedSuperusersList");
                        if (updatedUsers != null) {
                            superusers.clear();
                            superusers.addAll(updatedUsers);

                            for (Superuser item : superusers) {
                                Log.d("Mi app", String.valueOf(item));
                            }
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> deleteUserActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ArrayList<Superuser> updatedUsers = (ArrayList<Superuser>) data.getSerializableExtra("updatedSuperusersList");
                        if (updatedUsers != null) {
                            superusers.clear();
                            superusers.addAll(updatedUsers);

                            for (Superuser item : superusers) {
                                Log.d("Mi app", String.valueOf(item));
                            }
                        }
                    }
                }
            }
    );

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedSuperusers", superusers);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
