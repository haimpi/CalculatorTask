package com.example.calculatortask;

import android.os.Bundle;
import android.system.StructUtsname;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView result;
    double currentResult;
    char operator = '\0';

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

        result = findViewById(R.id.textViewResult);
        result.setText("");
    }

    public void numFunc(View view) {
        if(result.getText().toString().equals("ERROR")) return;

        Button button = (Button) view;
        result.append(button.getText().toString());
    }

    public void charFunc(View view) {
        try {
            Button button = (Button) view;
            char currentChar = button.getText().toString().charAt(0);

            if(isErrorState() && currentChar != 'C') return;

            switch (currentChar) {
                case '*':
                case '+':
                case '-':
                case '/':
                    handleOperator(currentChar);
                    break;

                case '=':
                    handleEquals();
                    break;

                case 'C':
                    resetCalculator();
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            setErrorState();
        }
    }

    private double calculateResult(double num1, double num2, char operator) {
        double result = 0;

        switch (operator) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/': result = (num2 == 0) ? Double.NaN : num1 / num2; break;
            default: result = num2; break;
        }

        return result;
    }

    private String formatResult(double result){
        return (result == (int)result) ? String.valueOf((int) result) : String.valueOf(result);
    }

    private void handleOperator(char currentChar){
        if (!result.getText().toString().isEmpty()) {
            double number = Double.parseDouble(result.getText().toString());
            currentResult = (operator == '\0') ? number : calculateResult(currentResult, number, operator);
            operator = currentChar;
            result.setText("");
        }
    }

    private void handleEquals(){
        if (!result.getText().toString().isEmpty()) {
            double number = Double.parseDouble(result.getText().toString());
            currentResult = calculateResult(currentResult, number, operator);

            if (Double.isNaN(currentResult)) {
                setErrorState();
            } else {
                result.setText(formatResult(currentResult));
                resetState();
            }
        }
    }

    private void resetCalculator(){
        result.setText("");
        resetState();
    }

    private void setErrorState(){
        Toast.makeText(this, "Error: Division by zero! Please rest", Toast.LENGTH_SHORT).show();
        result.setText("ERROR");
        resetState();
    }

    private void resetState(){
        currentResult = 0;
        operator = '\0';
    }

    private boolean isErrorState(){
        return result.getText().toString().equals("ERROR");
    }
}