package com.example.calculatorandform;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class UsersTable extends AppCompatActivity {

    TableLayout usersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usersTable = findViewById(R.id.usersTable);

        ArrayList<User> users = (ArrayList<User>) getIntent().getSerializableExtra("usersList");
        for (User user : users) {
            addRow(user);
        }
    }

    public void addRow(User user) {
        TableRow row = new TableRow(this);
        TextView idCell = new TextView(this);
        TextView nameCell = new TextView(this);
        TextView secondNameCell = new TextView(this);
        TextView ageCell = new TextView(this);
        TextView emailCell = new TextView(this);

        // Making shorter the code jeje
        TextView cells[] = {idCell, nameCell, secondNameCell, ageCell, emailCell};
        for (TextView cell : cells) {
            cell.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }

        idCell.setText(String.valueOf(user.id));
        nameCell.setText(user.name);
        secondNameCell.setText(user.secondName);
        ageCell.setText(String.valueOf(user.age));
        emailCell.setText(user.email);

        // Code shorter again
        for (TextView cell : cells) {
            row.addView(cell);
        }

        usersTable.addView(row);
    }
}