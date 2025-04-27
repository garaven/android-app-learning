package com.example.calculatorandform;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UsersTable extends AppCompatActivity {

    private TableLayout usersTable;
    private AdminSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

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

        dbHelper = AdminSQLiteOpenHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "users",
                new String[]{ "id", "name", "age", "email", "createdBy" },
                null, null, null, null, "id ASC"
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id        = cursor.getInt(0);
                    String name   = cursor.getString(1);
                    int age       = cursor.getInt(2);
                    String email  = cursor.getString(3);
                    String by     = cursor.getString(4);
                    addRow(id, name, age, email, by);
                }
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(this, "No hay usuarios para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRow(int id, String name, int age, String email, String createdBy) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
        );

        TextView idCell       = new TextView(this);
        TextView nameCell     = new TextView(this);
        TextView ageCell      = new TextView(this);
        TextView emailCell    = new TextView(this);
        TextView createdByCell= new TextView(this);

        idCell.setLayoutParams(lp);
        nameCell.setLayoutParams(lp);
        ageCell.setLayoutParams(lp);
        emailCell.setLayoutParams(lp);
        createdByCell.setLayoutParams(lp);

        idCell.setText(String.valueOf(id));
        nameCell.setText(name);
        ageCell.setText(String.valueOf(age));
        emailCell.setText(email);
        createdByCell.setText(createdBy);

        row.addView(idCell);
        row.addView(nameCell);
        row.addView(ageCell);
        row.addView(emailCell);
        row.addView(createdByCell);

        usersTable.addView(row);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
