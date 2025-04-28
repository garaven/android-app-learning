package com.example.calculatorandform;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

public class UpdateUser extends AppCompatActivity {

    private TextView idToSearch, idUser, nameUser, secondNameUser, ageUser, emailUser, docNumberUser;
    private RadioButton radioMaleUpdate, radioFemaleUpdate;
    private Button getId, updateUser;
    private Spinner spinnerDocTypes, spinnerEducationLevel;
    private CheckBox checkboxRock, checkboxPop, checkboxRap, checkboxJazz, checkboxClassical, checkboxOther;
    private CheckBox checkboxFootball, checkboxBasketball, checkboxTennis, checkboxRunning, checkboxSwimming, checkboxCycling;

    private AdminSQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private int currentUserId = -1;
    private String currentSuperuser;

    private final String[] docTypesOptions = {
            "Tarjeta de identidad", "Cédula de ciudadanía",
            "Pasaporte", "Tarjeta de extranjería",
            "Permiso especial de permanencia"
    };
    private final String[] educationLevelsOptions = {
            "Ninguno", "Primaria", "Secundaria",
            "Universidad", "Maestría"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user);
        setupWindowInsets();

        dbHelper = AdminSQLiteOpenHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();

        currentSuperuser = getIntent().getStringExtra("superuser");
        if (currentSuperuser == null) {
            currentSuperuser = "";
        }

        initializeComponents();
        setupSpinners();
        setupButtons();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });
    }

    private void initializeComponents() {
        idToSearch       = findViewById(R.id.idSearchInput);
        idUser           = findViewById(R.id.idInputUpdate);
        nameUser         = findViewById(R.id.nameInputUpdate);
        secondNameUser   = findViewById(R.id.secondNameInputUpdate);
        ageUser          = findViewById(R.id.ageInputUpdate);
        emailUser        = findViewById(R.id.emailInputUpdate);
        docNumberUser    = findViewById(R.id.docNumberInputUpdate);

        radioMaleUpdate   = findViewById(R.id.radioMaleUpdate);
        radioFemaleUpdate = findViewById(R.id.radioFemaleUpdate);

        spinnerDocTypes       = findViewById(R.id.spinnerDocTypeUpdate);
        spinnerEducationLevel = findViewById(R.id.spinnerEducationLevel);

        checkboxRock      = findViewById(R.id.checkboxRock);
        checkboxPop       = findViewById(R.id.checkboxPop);
        checkboxRap       = findViewById(R.id.checkboxRap);
        checkboxJazz      = findViewById(R.id.checkboxJazz);
        checkboxClassical = findViewById(R.id.checkboxClassical);
        checkboxOther     = findViewById(R.id.checkboxOther);

        checkboxFootball   = findViewById(R.id.checkboxFootball);
        checkboxBasketball = findViewById(R.id.checkboxBasketball);
        checkboxTennis     = findViewById(R.id.checkboxTennis);
        checkboxRunning    = findViewById(R.id.checkboxRunning);
        checkboxSwimming   = findViewById(R.id.checkboxSwimming);
        checkboxCycling    = findViewById(R.id.checkboxCycling);

        getId       = findViewById(R.id.searchUserButton);
        updateUser  = findViewById(R.id.updateUserButton);
    }

    private void setupSpinners() {
        ArrayAdapter<String> docAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, docTypesOptions);
        docAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDocTypes.setAdapter(docAdapter);

        ArrayAdapter<String> eduAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, educationLevelsOptions);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducationLevel.setAdapter(eduAdapter);
    }

    private void setupButtons() {
        getId.setOnClickListener(v -> handleSearchUserInDb());
        updateUser.setOnClickListener(v -> handleUpdateUserInDb());
        enableFields(false);
    }

    private void handleSearchUserInDb() {
        try {
            int uid = Integer.parseInt(idToSearch.getText().toString().trim());
            if (findUserById(uid)) {
                enableFields(true);
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                enableFields(false);
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "ID debe ser numérico", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean findUserById(int uid) {
        String[] cols = {
                "id","name","secondName","email","docType",
                "gender","age","docNumber","educationLevel"
        };
        String sel = "id = ?";
        String[] args = { String.valueOf(uid) };

        try (Cursor c = db.query("users", cols, sel, args, null, null, null)) {
            if (c.moveToFirst()) {
                currentUserId = uid;
                idUser.setText(String.valueOf(c.getInt(0)));
                nameUser.setText(c.getString(1));
                secondNameUser.setText(c.getString(2));
                emailUser.setText(c.getString(3));
                setSpinnerSelection(spinnerDocTypes, docTypesOptions, c.getString(4));
                String gender = c.getString(5);
                radioMaleUpdate.setChecked("Masculino".equals(gender));
                radioFemaleUpdate.setChecked("Femenino".equals(gender));
                ageUser.setText(String.valueOf(c.getInt(6)));
                docNumberUser.setText(String.valueOf(c.getInt(7)));
                setSpinnerSelection(spinnerEducationLevel, educationLevelsOptions, c.getString(8));

                loadSportsData(uid);
                loadMusicTastesData(uid);
                return true;
            }
        } catch (Exception e) {
            Log.e("UpdateUser", "buscar: " + e.getMessage(), e);
        }
        return false;
    }

    private void loadSportsData(int uid) {
        resetCheckboxesSports();
        String[] cols = {"soccer","basketball","tennis","running","swimming","cycling"};
        String sel = "userId = ?";
        String[] args = { String.valueOf(uid) };
        try (Cursor c = db.query("sports", cols, sel, args, null, null, null)) {
            if (c.moveToFirst()) {
                checkboxFootball.setChecked(c.getInt(0)==1);
                checkboxBasketball.setChecked(c.getInt(1)==1);
                checkboxTennis.setChecked(c.getInt(2)==1);
                checkboxRunning.setChecked(c.getInt(3)==1);
                checkboxSwimming.setChecked(c.getInt(4)==1);
                checkboxCycling.setChecked(c.getInt(5)==1);
            }
        } catch (Exception e) {
            Log.e("UpdateUser", "loadSports: " + e.getMessage(), e);
        }
    }

    private void loadMusicTastesData(int uid) {
        resetCheckboxesMusic();
        String[] cols = {"rock","pop","rap","jazz","classic","reggaeton"};
        String sel = "userId = ?";
        String[] args = { String.valueOf(uid) };
        try (Cursor c = db.query("musicTastes", cols, sel, args, null, null, null)) {
            if (c.moveToFirst()) {
                checkboxRock.setChecked(c.getInt(0)==1);
                checkboxPop.setChecked(c.getInt(1)==1);
                checkboxRap.setChecked(c.getInt(2)==1);
                checkboxJazz.setChecked(c.getInt(3)==1);
                checkboxClassical.setChecked(c.getInt(4)==1);
                checkboxOther.setChecked(c.getInt(5)==1);
            }
        } catch (Exception e) {
            Log.e("UpdateUser", "loadMusic: " + e.getMessage(), e);
        }
    }

    private void setSpinnerSelection(Spinner sp, String[] opts, String val) {
        for (int i = 0; i < opts.length; i++) {
            if (opts[i].equals(val)) {
                sp.setSelection(i);
                break;
            }
        }
    }

    private void handleUpdateUserInDb() {
        if (currentUserId < 0) {
            Toast.makeText(this, "Primero busca un usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put("name", nameUser.getText().toString());
            cv.put("secondName", secondNameUser.getText().toString());
            cv.put("email", emailUser.getText().toString());
            cv.put("docType", spinnerDocTypes.getSelectedItem().toString());
            cv.put("gender", radioMaleUpdate.isChecked() ? "Masculino" : "Femenino");
            cv.put("age", Integer.parseInt(ageUser.getText().toString()));
            cv.put("docNumber", Long.parseLong(docNumberUser.getText().toString()));
            cv.put("educationLevel", spinnerEducationLevel.getSelectedItem().toString());
            // NO ACTUALIZAR createdBy aquí

            String where = "id = ?";
            String[] args = { String.valueOf(currentUserId) };
            int rows = db.update("users", cv, where, args);
            if (rows <= 0) throw new Exception("No se actualizó users");

            ContentValues cs = new ContentValues();
            cs.put("soccer",      checkboxFootball.isChecked()   ? 1 : 0);
            cs.put("basketball",  checkboxBasketball.isChecked() ? 1 : 0);
            cs.put("tennis",      checkboxTennis.isChecked()     ? 1 : 0);
            cs.put("running",     checkboxRunning.isChecked()    ? 1 : 0);
            cs.put("swimming",    checkboxSwimming.isChecked()   ? 1 : 0);
            cs.put("cycling",     checkboxCycling.isChecked()    ? 1 : 0);
            String ws = "userId = ?";
            String[] as = { String.valueOf(currentUserId) };
            if (db.update("sports", cs, ws, as) == 0) {
                cs.put("userId", currentUserId);
                db.insert("sports", null, cs);
            }

            ContentValues cm = new ContentValues();
            cm.put("rock",      checkboxRock.isChecked()      ? 1 : 0);
            cm.put("pop",       checkboxPop.isChecked()       ? 1 : 0);
            cm.put("rap",       checkboxRap.isChecked()       ? 1 : 0);
            cm.put("jazz",      checkboxJazz.isChecked()      ? 1 : 0);
            cm.put("classic",   checkboxClassical.isChecked() ? 1 : 0);
            cm.put("reggaeton", checkboxOther.isChecked()     ? 1 : 0);
            if (db.update("musicTastes", cm, ws, as) == 0) {
                cm.put("userId", currentUserId);
                db.insert("musicTastes", null, cm);
            }

            db.setTransactionSuccessful();
            Toast.makeText(this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
            resetForm();
        } catch (Exception e) {
            Log.e("UpdateUser", "update: " + e.getMessage(), e);
            Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
    }

    private void enableFields(boolean e) {
        radioMaleUpdate.setEnabled(e);
        radioFemaleUpdate.setEnabled(e);
        nameUser.setEnabled(e);
        secondNameUser.setEnabled(e);
        emailUser.setEnabled(e);
        ageUser.setEnabled(e);
        docNumberUser.setEnabled(e);
        spinnerDocTypes.setEnabled(e);
        spinnerEducationLevel.setEnabled(e);
        checkboxRock.setEnabled(e);
        checkboxPop.setEnabled(e);
        checkboxRap.setEnabled(e);
        checkboxJazz.setEnabled(e);
        checkboxClassical.setEnabled(e);
        checkboxOther.setEnabled(e);
        checkboxFootball.setEnabled(e);
        checkboxBasketball.setEnabled(e);
        checkboxTennis.setEnabled(e);
        checkboxRunning.setEnabled(e);
        checkboxSwimming.setEnabled(e);
        checkboxCycling.setEnabled(e);
        updateUser.setEnabled(e);
    }

    private void resetForm() {
        idToSearch.setText("");
        idUser.setText("");
        nameUser.setText("");
        secondNameUser.setText("");
        emailUser.setText("");
        ageUser.setText("");
        docNumberUser.setText("");
        resetCheckboxesMusic();
        resetCheckboxesSports();
        enableFields(false);
        currentUserId = -1;
    }

    private void resetCheckboxesMusic() {
        checkboxRock.setChecked(false);
        checkboxPop.setChecked(false);
        checkboxRap.setChecked(false);
        checkboxJazz.setChecked(false);
        checkboxClassical.setChecked(false);
        checkboxOther.setChecked(false);
    }

    private void resetCheckboxesSports() {
        checkboxFootball.setChecked(false);
        checkboxBasketball.setChecked(false);
        checkboxTennis.setChecked(false);
        checkboxRunning.setChecked(false);
        checkboxSwimming.setChecked(false);
        checkboxCycling.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) db.close();
    }
}
