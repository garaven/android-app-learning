package com.example.calculatorandform;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calculator extends AppCompatActivity {
    Button number1, number2, number3, number4, number5, number6, number7, number8, number9, number0, showHistory;
    Button clear, equal, add, sustraction, backspace, product, division, decimal;
    TextView screen;

    String input;
    String operation;
    Double result, first_number, second_number;
    boolean isNewOperation;
    boolean operationJustPressed;

    private static final String HISTORY_FILE = "calculator_history.txt";
    private static final int MAX_HISTORY_ENTRIES = 100;
    private List<String> calculationHistory;

    private final ActivityResultLauncher<Intent> historyLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == History.HISTORY_CLEARED) {
                    calculationHistory.clear();
                    Log.d("Calculator", "Historial borrado en memoria");
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calculator);

        calculationHistory = new ArrayList<>();
        cargarHistorial();

        screen = findViewById(R.id.screen);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        number0 = findViewById(R.id.number0);
        clear = findViewById(R.id.clear);
        equal = findViewById(R.id.equal);
        backspace = findViewById(R.id.backspace);
        add = findViewById(R.id.add);
        sustraction = findViewById(R.id.sustraction);
        product = findViewById(R.id.product);
        division = findViewById(R.id.division);
        decimal = findViewById(R.id.decimal);

        resetCalculator();

        number1.setOnClickListener(x -> addToScreen("1"));
        number2.setOnClickListener(x -> addToScreen("2"));
        number3.setOnClickListener(x -> addToScreen("3"));
        number4.setOnClickListener(x -> addToScreen("4"));
        number5.setOnClickListener(x -> addToScreen("5"));
        number6.setOnClickListener(x -> addToScreen("6"));
        number7.setOnClickListener(x -> addToScreen("7"));
        number8.setOnClickListener(x -> addToScreen("8"));
        number9.setOnClickListener(x -> addToScreen("9"));
        number0.setOnClickListener(x -> addToScreen("0"));
        decimal.setOnClickListener(x -> addDecimal());

        add.setOnClickListener(x -> identifyOperation("+"));
        sustraction.setOnClickListener(x -> identifyOperation("-"));
        product.setOnClickListener(x -> identifyOperation("*"));
        division.setOnClickListener(x -> identifyOperation("/"));

        clear.setOnClickListener(x -> resetCalculator());
        equal.setOnClickListener(x -> calculate());
        backspace.setOnClickListener(x -> handleBackspace());

        showHistory = findViewById(R.id.showHistory);
        showHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Calculator.this, History.class);
            historyLauncher.launch(intent);  // Usar el launcher en lugar de startActivity
        });
    }

    private void resetCalculator() {
        input = "";
        operation = "";
        first_number = 0.0;
        second_number = 0.0;
        isNewOperation = true;
        operationJustPressed = false;
        result = null;
        screen.setText("0");
    }

    public void identifyOperation(String op) {
        if (operationJustPressed) {
            operation = op;
            Toast.makeText(this, "Operación cambiada a " + op, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!operation.isEmpty() && !input.isEmpty()) {
            calculate();
        }

        if (!input.isEmpty()) {
            first_number = Double.parseDouble(input);
        } else if (result != null) {
            first_number = result;
        }

        operation = op;
        isNewOperation = true;
        operationJustPressed = true;
    }

    public void handleBackspace() {
        if (isNewOperation && result != null) {
            input = screen.getText().toString();
            isNewOperation = false;
        }

        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);

            if (input.isEmpty()) {
                screen.setText("0");
            } else {
                screen.setText(input);
            }
        } else {
            Toast.makeText(this, "Nada para borrar", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculate() {
        if (operation.isEmpty()) {
            Toast.makeText(this, "Primero selecciona una operación", Toast.LENGTH_SHORT).show();
            return;
        }

        if (input.isEmpty()) {
            second_number = first_number;
        } else {
            second_number = Double.parseDouble(input);
        }

        String operationStr = formatNumber(first_number) + " " + operation + " " + formatNumber(second_number);

        try {
            switch (operation) {
                case "+":
                    result = first_number + second_number;
                    break;
                case "*":
                    result = first_number * second_number;
                    break;
                case "-":
                    result = first_number - second_number;
                    break;
                case "/":
                    if (second_number == 0) {
                        Toast.makeText(this, "No se puede dividir por cero", Toast.LENGTH_SHORT).show();
                        resetCalculator();
                        return;
                    }
                    result = first_number / second_number;
                    break;
            }

            String resultText = formatNumber(result);
            screen.setText(resultText);

            String fullOperation = operationStr + " = " + resultText;
            agregarAlHistorial(fullOperation);

            first_number = result;
            input = "";
            operation = "";
            isNewOperation = true;
            operationJustPressed = false;

        } catch (Exception e) {
            Toast.makeText(this, "Error al calcular", Toast.LENGTH_SHORT).show();
            resetCalculator();
        }
    }

    public void addToScreen(String number) {
        if (isNewOperation) {
            input = "";
            isNewOperation = false;
        }

        operationJustPressed = false;

        if (input.equals("0") && number.equals("0")) {
            Toast.makeText(this, "Ya hay un cero inicial", Toast.LENGTH_SHORT).show();
            return;
        }

        if (input.equals("0") && !number.equals(".")) {
            input = number;
        } else {
            input += number;
        }

        screen.setText(input);
    }

    public void addDecimal() {
        operationJustPressed = false;

        if (isNewOperation) {
            input = "0";
            isNewOperation = false;
        }

        if (!input.contains(".")) {
            if (input.isEmpty()) {
                input = "0";
            }
            input += ".";
            screen.setText(input);
        } else {
            Toast.makeText(this, "Ya hay un punto decimal", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatNumber(Double number) {
        if (number == Math.floor(number)) {
            return String.format(Locale.getDefault(), "%.0f", number);
        } else {
            return String.valueOf(number);
        }
    }

    private void agregarAlHistorial(String operacion) {
        calculationHistory.add(0, operacion);

        if (calculationHistory.size() > MAX_HISTORY_ENTRIES) {
            calculationHistory = calculationHistory.subList(0, MAX_HISTORY_ENTRIES);
        }
        guardarHistorial();
    }

    private void guardarHistorial() {
        try (FileOutputStream fos = openFileOutput(HISTORY_FILE, MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            for (String operacion : calculationHistory) {
                writer.write(operacion);
                writer.newLine();
            }

        } catch (IOException ex) {
            Log.e("Calculator", "Error guardando historial", ex);
            Toast.makeText(this, "No se pudo guardar el historial", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarHistorial() {
        calculationHistory.clear();

        try (FileInputStream fis = openFileInput(HISTORY_FILE);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                calculationHistory.add(line);
            }

        } catch (IOException e) {
            Log.d("Calculator", "No se encontró historial o está vacío");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarHistorial();
    }

    @Override
    protected void onPause() {
        super.onPause();
        guardarHistorial();
    }
}