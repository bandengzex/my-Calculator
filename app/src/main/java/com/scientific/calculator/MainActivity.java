package com.scientific.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    
    private TextView displayExpression;
    private TextView displayResult;
    private StringBuilder currentExpression = new StringBuilder();
    private MathExpressionEvaluator evaluator = new MathExpressionEvaluator();
    private MathExpressionRenderer renderer = new MathExpressionRenderer();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        displayExpression = findViewById(R.id.display_expression);
        displayResult = findViewById(R.id.display_result);
        
        setupButtons();
    }
    
    private void setupButtons() {
        // 数字按钮
        findViewById(R.id.btn_0).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.btn_1).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.btn_2).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.btn_3).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.btn_4).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.btn_5).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.btn_6).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.btn_7).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.btn_8).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.btn_9).setOnClickListener(v -> appendNumber("9"));
        
        // 基本运算按钮
        findViewById(R.id.btn_add).setOnClickListener(v -> appendOperator("+"));
        findViewById(R.id.btn_subtract).setOnClickListener(v -> appendOperator("−"));
        findViewById(R.id.btn_multiply).setOnClickListener(v -> appendOperator("×"));
        findViewById(R.id.btn_divide).setOnClickListener(v -> appendOperator("÷"));
        
        // 科学计算按钮
        findViewById(R.id.btn_sin).setOnClickListener(v -> appendFunction("sin("));
        findViewById(R.id.btn_cos).setOnClickListener(v -> appendFunction("cos("));
        findViewById(R.id.btn_tan).setOnClickListener(v -> appendFunction("tan("));
        findViewById(R.id.btn_log).setOnClickListener(v -> appendFunction("log("));
        findViewById(R.id.btn_ln).setOnClickListener(v -> appendFunction("ln("));
        findViewById(R.id.btn_sqrt).setOnClickListener(v -> appendFunction("√("));
        findViewById(R.id.btn_power).setOnClickListener(v -> appendOperator("^"));
        
        // 特殊按钮
        findViewById(R.id.btn_left_paren).setOnClickListener(v -> appendText("("));
        findViewById(R.id.btn_right_paren).setOnClickListener(v -> appendText(")"));
        findViewById(R.id.btn_decimal).setOnClickListener(v -> appendText("."));
        findViewById(R.id.btn_pi).setOnClickListener(v -> appendText("π"));
        findViewById(R.id.btn_e).setOnClickListener(v -> appendText("e"));
        
        // 控制按钮
        findViewById(R.id.btn_clear).setOnClickListener(v -> clearAll());
        findViewById(R.id.btn_backspace).setOnClickListener(v -> backspace());
        findViewById(R.id.btn_equals).setOnClickListener(v -> calculate());
    }
    
    private void appendNumber(String number) {
        currentExpression.append(number);
        updateDisplay();
    }
    
    private void appendOperator(String operator) {
        if (currentExpression.length() > 0) {
            currentExpression.append(operator);
            updateDisplay();
        }
    }
    
    private void appendFunction(String function) {
        currentExpression.append(function);
        updateDisplay();
    }
    
    private void appendText(String text) {
        currentExpression.append(text);
        updateDisplay();
    }
    
    private void clearAll() {
        currentExpression.setLength(0);
        displayExpression.setText("");
        displayResult.setText("0");
    }
    
    private void backspace() {
        if (currentExpression.length() > 0) {
            currentExpression.deleteCharAt(currentExpression.length() - 1);
            updateDisplay();
        }
    }
    
    private void calculate() {
        if (currentExpression.length() > 0) {
            try {
                String expression = currentExpression.toString();
                double result = evaluator.evaluate(expression);
                displayResult.setText(formatResult(result));
            } catch (Exception e) {
                displayResult.setText("错误");
            }
        }
    }
    
    private void updateDisplay() {
        String formattedExpression = MathExpressionRenderer.formatForDisplay(currentExpression.toString());
        displayExpression.setText(formattedExpression);
    }
    
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            return String.format("%.8f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
        }
    }
}