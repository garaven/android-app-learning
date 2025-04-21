package com.example.calculatorandform;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {
    Button number1, number2, number3, number4, number5, number6, number7, number8, number9, number0;
    Button clear, equal, add, sustraction, backspace, product, division, decimal;
    TextView screen;

    String input;
    String operation;
    Double result, first_number, second_number;
    boolean isNewOperation;
    boolean operationJustPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.calculator);

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
        }
    }

    public void calculate() {
        if (operation.isEmpty()) {
            return;
        }

        if (input.isEmpty()) {
            second_number = first_number;
        } else {
            second_number = Double.parseDouble(input);
        }

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
                        screen.setText("Error");
                        resetCalculator();
                        return;
                    }
                    result = first_number / second_number;
                    break;
            }

            String resultText;
            if (result == Math.floor(result)) {
                resultText = String.format("%.0f", result);
            } else {
                resultText = String.valueOf(result);
            }

            screen.setText(resultText);
            first_number = result;
            input = "";
            operation = "";
            isNewOperation = true;
            operationJustPressed = false;

        } catch (Exception e) {
            screen.setText("Error");
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
        }
    }
}