package com.example.calculatorandform;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class History extends AppCompatActivity {

    private static final String HISTORY_FILE = "calculator_history.txt";
    private TextView historyTextView;
    public static final int HISTORY_CLEARED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTextView = findViewById(R.id.historyTextView);
        Button clearHistoryButton = findViewById(R.id.clearHistoryButton);

        mostrarHistorial();

        clearHistoryButton.setOnClickListener(v -> {
            limpiarHistorial();
            mostrarHistorial();

            setResult(HISTORY_CLEARED);
        });
    }

    private void mostrarHistorial() {
        StringBuilder historyBuilder = new StringBuilder();

        try (FileInputStream fis = openFileInput(HISTORY_FILE);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                historyBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            historyBuilder.append("No hay historial disponible.");
        }

        historyTextView.setText(historyBuilder.toString());
    }

    private void limpiarHistorial() {
        try (FileOutputStream fos = openFileOutput(HISTORY_FILE, MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("");
            Toast.makeText(this, "Historial borrado", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, "Error al borrar historial", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
}