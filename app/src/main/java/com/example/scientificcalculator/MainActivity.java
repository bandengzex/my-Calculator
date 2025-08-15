package com.example.scientificcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.github.kexanie.library.MathView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import java.text.DecimalFormat;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MathView expressionDisplay;
    private TextView resultDisplay;
    private StringBuilder currentExpression;
    private String currentResult;
    private DecimalFormat decimalFormat;
    
    // JavaScript engine for calculations
    private Context jsContext;
    private Scriptable jsScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        expressionDisplay = findViewById(R.id.expression_display);
        resultDisplay = findViewById(R.id.result_display);
        
        // Initialize expression and result
        currentExpression = new StringBuilder();
        currentResult = "0";
        decimalFormat = new DecimalFormat("#.##########");
        
        // Initialize JavaScript engine
        jsContext = Context.enter();
        jsScope = jsContext.initStandardObjects();
        
        // Add mathematical functions to JavaScript scope
        addMathFunctions();
        
        // Set up MathView for LaTeX rendering
        expressionDisplay.setText("$$0$$");
        
        // Set up button click listeners
        setupButtonClickListeners();
    }

    private void addMathFunctions() {
        // Add common mathematical constants and functions
        jsContext.evaluateString(jsScope, "var Math = Java.type('java.lang.Math');", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "var PI = Math.PI;", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "var E = Math.E;", "<cmd>", 1, null);
        
        // Add trigonometric functions
        jsContext.evaluateString(jsScope, "function sin(x) { return Math.sin(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function cos(x) { return Math.cos(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function tan(x) { return Math.tan(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function asin(x) { return Math.asin(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function acos(x) { return Math.acos(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function atan(x) { return Math.atan(x); }", "<cmd>", 1, null);
        
        // Add logarithmic functions
        jsContext.evaluateString(jsScope, "function log(x) { return Math.log10(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function ln(x) { return Math.log(x); }", "<cmd>", 1, null);
        
        // Add other mathematical functions
        jsContext.evaluateString(jsScope, "function sqrt(x) { return Math.sqrt(x); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function pow(x, y) { return Math.pow(x, y); }", "<cmd>", 1, null);
        jsContext.evaluateString(jsScope, "function factorial(n) { if (n <= 1) return 1; var result = 1; for (var i = 2; i <= n; i++) result *= i; return result; }", "<cmd>", 1, null);
    }

    private void setupButtonClickListeners() {
        // Number buttons
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        
        // Operator buttons
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_equals).setOnClickListener(this);
        
        // Function buttons
        findViewById(R.id.btn_sin).setOnClickListener(this);
        findViewById(R.id.btn_cos).setOnClickListener(this);
        findViewById(R.id.btn_tan).setOnClickListener(this);
        findViewById(R.id.btn_asin).setOnClickListener(this);
        findViewById(R.id.btn_acos).setOnClickListener(this);
        findViewById(R.id.btn_atan).setOnClickListener(this);
        findViewById(R.id.btn_log).setOnClickListener(this);
        findViewById(R.id.btn_ln).setOnClickListener(this);
        findViewById(R.id.btn_sqrt).setOnClickListener(this);
        findViewById(R.id.btn_power).setOnClickListener(this);
        findViewById(R.id.btn_square).setOnClickListener(this);
        findViewById(R.id.btn_cube).setOnClickListener(this);
        findViewById(R.id.btn_factorial).setOnClickListener(this);
        findViewById(R.id.btn_percent).setOnClickListener(this);
        findViewById(R.id.btn_reciprocal).setOnClickListener(this);
        findViewById(R.id.btn_plus_minus).setOnClickListener(this);
        findViewById(R.id.btn_pi).setOnClickListener(this);
        findViewById(R.id.btn_e).setOnClickListener(this);
        findViewById(R.id.btn_left_paren).setOnClickListener(this);
        findViewById(R.id.btn_right_paren).setOnClickListener(this);
        findViewById(R.id.btn_fraction).setOnClickListener(this);
        
        // Clear buttons
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_clear_all).setOnClickListener(this);
        findViewById(R.id.btn_backspace).setOnClickListener(this);
        findViewById(R.id.btn_backspace2).setOnClickListener(this);
        
        // Angle buttons
        findViewById(R.id.btn_degree).setOnClickListener(this);
        findViewById(R.id.btn_minute).setOnClickListener(this);
        findViewById(R.id.btn_second).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (id) {
            case R.id.btn_0: case R.id.btn_1: case R.id.btn_2: case R.id.btn_3:
            case R.id.btn_4: case R.id.btn_5: case R.id.btn_6: case R.id.btn_7:
            case R.id.btn_8: case R.id.btn_9:
                appendToExpression(buttonText);
                break;
                
            case R.id.btn_dot:
                appendToExpression(".");
                break;
                
            case R.id.btn_plus:
                appendToExpression(" + ");
                break;
                
            case R.id.btn_minus:
                appendToExpression(" - ");
                break;
                
            case R.id.btn_multiply:
                appendToExpression(" × ");
                break;
                
            case R.id.btn_divide:
                appendToExpression(" ÷ ");
                break;
                
            case R.id.btn_left_paren:
                appendToExpression("(");
                break;
                
            case R.id.btn_right_paren:
                appendToExpression(")");
                break;
                
            case R.id.btn_sin:
                appendToExpression("sin(");
                break;
                
            case R.id.btn_cos:
                appendToExpression("cos(");
                break;
                
            case R.id.btn_tan:
                appendToExpression("tan(");
                break;
                
            case R.id.btn_asin:
                appendToExpression("asin(");
                break;
                
            case R.id.btn_acos:
                appendToExpression("acos(");
                break;
                
            case R.id.btn_atan:
                appendToExpression("atan(");
                break;
                
            case R.id.btn_log:
                appendToExpression("log(");
                break;
                
            case R.id.btn_ln:
                appendToExpression("ln(");
                break;
                
            case R.id.btn_sqrt:
                appendToExpression("√(");
                break;
                
            case R.id.btn_power:
                appendToExpression("^");
                break;
                
            case R.id.btn_square:
                appendToExpression("²");
                break;
                
            case R.id.btn_cube:
                appendToExpression("³");
                break;
                
            case R.id.btn_factorial:
                appendToExpression("!");
                break;
                
            case R.id.btn_percent:
                appendToExpression("%");
                break;
                
            case R.id.btn_reciprocal:
                appendToExpression("1/");
                break;
                
            case R.id.btn_plus_minus:
                toggleSign();
                break;
                
            case R.id.btn_pi:
                appendToExpression("π");
                break;
                
            case R.id.btn_e:
                appendToExpression("e");
                break;
                
            case R.id.btn_fraction:
                appendToExpression("/");
                break;
                
            case R.id.btn_degree:
                appendToExpression("°");
                break;
                
            case R.id.btn_minute:
                appendToExpression("′");
                break;
                
            case R.id.btn_second:
                appendToExpression("″");
                break;
                
            case R.id.btn_clear:
                clearLastEntry();
                break;
                
            case R.id.btn_clear_all:
                clearAll();
                break;
                
            case R.id.btn_backspace:
            case R.id.btn_backspace2:
                backspace();
                break;
                
            case R.id.btn_equals:
                calculateResult();
                break;
        }
    }

    private void appendToExpression(String text) {
        currentExpression.append(text);
        updateExpressionDisplay();
    }

    private void updateExpressionDisplay() {
        String expression = currentExpression.toString();
        if (expression.isEmpty()) {
            expressionDisplay.setText("$$0$$");
        } else {
            // Convert to LaTeX format for better rendering
            String latexExpression = convertToLatex(expression);
            expressionDisplay.setText("$$" + latexExpression + "$$");
        }
    }

    private String convertToLatex(String expression) {
        // Convert special symbols to LaTeX format
        String latex = expression
                .replace(" × ", " \times ")
                .replace(" ÷ ", " \div ")
                .replace("√(", "\\sqrt{")
                .replace("²", "^2")
                .replace("³", "^3")
                .replace("π", "\\pi")
                .replace("°", "^{\\circ}")
                .replace("′", "'")
                .replace("″", "''")
                .replace("%", "\\%");
        
        return latex;
    }

    private void toggleSign() {
        String expression = currentExpression.toString();
        if (!expression.isEmpty()) {
            // Simple sign toggle - in a real implementation, this would be more sophisticated
            if (expression.startsWith("-")) {
                currentExpression = new StringBuilder(expression.substring(1));
            } else {
                currentExpression = new StringBuilder("-" + expression);
            }
            updateExpressionDisplay();
        }
    }

    private void clearLastEntry() {
        // Clear the last entered number or function
        String expression = currentExpression.toString();
        if (!expression.isEmpty()) {
            // Find the last operator or function
            int lastSpace = expression.lastIndexOf(" ");
            if (lastSpace >= 0) {
                currentExpression = new StringBuilder(expression.substring(0, lastSpace));
            } else {
                currentExpression = new StringBuilder();
            }
            updateExpressionDisplay();
        }
    }

    private void clearAll() {
        currentExpression = new StringBuilder();
        currentResult = "0";
        expressionDisplay.setText("$$0$$");
        resultDisplay.setText(currentResult);
    }

    private void backspace() {
        if (currentExpression.length() > 0) {
            currentExpression.deleteCharAt(currentExpression.length() - 1);
            updateExpressionDisplay();
        }
    }

    private void calculateResult() {
        String expression = currentExpression.toString();
        if (expression.isEmpty()) return;

        try {
            // Convert expression to JavaScript format
            String jsExpression = convertToJavaScript(expression);
            
            // Evaluate the expression
            Object result = jsContext.evaluateString(jsScope, jsExpression, "<cmd>", 1, null);
            
            // Format the result
            if (result instanceof Number) {
                double number = ((Number) result).doubleValue();
                currentResult = decimalFormat.format(number);
                
                // Remove trailing .0 for integers
                if (currentResult.endsWith(".0")) {
                    currentResult = currentResult.substring(0, currentResult.length() - 2);
                }
            } else {
                currentResult = result.toString();
            }
            
            resultDisplay.setText(currentResult);
            
        } catch (Exception e) {
            resultDisplay.setText("Error");
        }
    }

    private String convertToJavaScript(String expression) {
        // Convert calculator expression to JavaScript format
        String js = expression
                .replace(" × ", " * ")
                .replace(" ÷ ", " / ")
                .replace("√(", "sqrt(")
                .replace("²", "**2")
                .replace("³", "**3")
                .replace("π", "PI")
                .replace("e", "E")
                .replace("%", "/100")
                .replace("!", "factorial");
        
        return js;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Context.exit();
    }
}