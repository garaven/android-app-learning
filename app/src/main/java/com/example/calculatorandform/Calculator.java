package com.example.calculatorandform;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {
    Button number1, number2, number3, number4, number5, number6, number7, number8, number9, number0, clear, equal, add, sustraction, backspace, product, division;
    TextView screen;

    String input;
    String operation;
    Double result, first_number, second_number;

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

        input = "";
        operation = "";
        first_number = 0.0;
        second_number = 0.0;

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

        add.setOnClickListener(x -> identifyOperation("+"));
        sustraction.setOnClickListener(x -> identifyOperation("-"));
        product.setOnClickListener(x -> identifyOperation("*"));
        division.setOnClickListener(x -> identifyOperation("/"));

        clear.setOnClickListener(x -> clear());
        equal.setOnClickListener(x -> calculate());

        backspace.setOnClickListener(x -> {
            if (!input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
                screen.setText(input.isEmpty() ? "0" : input);
            }

            if (input.isEmpty()) {
                screen.setText("0");
                first_number = 0.0;
                second_number = 0.0;
            } else {
                if (operation.isEmpty()) {
                    first_number = Double.parseDouble(input);
                } else {
                    second_number = Double.parseDouble(input);
                }
            }
        });
    }

    public void identifyOperation(String op) {
        if (!input.isEmpty()) {
            first_number = Double.parseDouble(input);
        }
        operation = op;
        input = "";
        screen.setText("0");
    }

    public void clear() {
        input = "";
        first_number = 0.0;
        second_number = 0.0;
        operation = "";
        screen.setText("0");
    }

    public void calculate() {
        result = 0.0;

        if (!input.isEmpty()) {
            second_number = Double.parseDouble(input);
        }

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
                if (second_number != 0) {
                    result = first_number / second_number;
                } else {
                    screen.setText("Error");
                    return;
                }
                break;
        }

        screen.setText(String.valueOf(result));
        first_number = result;
        second_number = 0.0;
        operation = "";
        input = "";
    }

    public void addToScreen(String number) {
        input += number;
        screen.setText(input);

        if (operation.isEmpty()) {
            first_number = Double.parseDouble(input);
        } else {
            second_number = Double.parseDouble(input);
        }
    }
}
