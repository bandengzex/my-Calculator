package com.example.scientificcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.SubscriptSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.mariuszgromada.math.mxparser.*;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView inputTextView;
    private TextView resultTextView;
    private StringBuilder currentExpression;
    private StringBuilder displayExpression;
    private int numeratorStart = -1; // 分数分子的起始位置
    private int denominatorStart = -1; // 分数分母的起始位置
    private boolean isInFractionMode = false; // 是否处于分数输入模式
    private boolean isInSuperscriptMode = false; // 是否处于上标输入模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextView = findViewById(R.id.input_text_view);
        resultTextView = findViewById(R.id.result_text_view);
        currentExpression = new StringBuilder();
        displayExpression = new StringBuilder();

        // 设置数字按钮点击事件
        setButtonClickListener(R.id.button_0, "0");
        setButtonClickListener(R.id.button_1, "1");
        setButtonClickListener(R.id.button_2, "2");
        setButtonClickListener(R.id.button_3, "3");
        setButtonClickListener(R.id.button_4, "4");
        setButtonClickListener(R.id.button_5, "5");
        setButtonClickListener(R.id.button_6, "6");
        setButtonClickListener(R.id.button_7, "7");
        setButtonClickListener(R.id.button_8, "8");
        setButtonClickListener(R.id.button_9, "9");
        setButtonClickListener(R.id.button_dot, ".");

        // 设置基本运算符按钮点击事件
        setButtonClickListener(R.id.button_add, "+");
        setButtonClickListener(R.id.button_subtract, "-");
        setButtonClickListener(R.id.button_multiply, "*");
        setButtonClickListener(R.id.button_divide, "/");

        // 设置括号按钮点击事件
        setButtonClickListener(R.id.button_left_parenthesis, "(");
        setButtonClickListener(R.id.button_right_parenthesis, ")");

        // 设置特殊函数按钮点击事件
        findViewById(R.id.button_sin).setOnClickListener(v -> appendFunction("sin(", "sin("));
        findViewById(R.id.button_cos).setOnClickListener(v -> appendFunction("cos(", "cos("));
        findViewById(R.id.button_tan).setOnClickListener(v -> appendFunction("tan(", "tan("));
        findViewById(R.id.button_log).setOnClickListener(v -> appendFunction("log(", "log("));
        findViewById(R.id.button_ln).setOnClickListener(v -> appendFunction("ln(", "ln("));
        findViewById(R.id.button_sqrt).setOnClickListener(v -> appendSquareRoot());
        findViewById(R.id.button_square).setOnClickListener(v -> appendSquare());
        findViewById(R.id.button_power).setOnClickListener(v -> appendPower());
        findViewById(R.id.button_pi).setOnClickListener(v -> appendConstant("pi", "π"));
        findViewById(R.id.button_e).setOnClickListener(v -> appendConstant("e", "e"));

        // 设置分数按钮点击事件
        findViewById(R.id.button_fraction).setOnClickListener(v -> appendFraction());

        // 设置清除按钮点击事件
        findViewById(R.id.button_clear).setOnClickListener(v -> {
            clearAll();
        });

        // 设置删除按钮点击事件
        findViewById(R.id.button_delete).setOnClickListener(v -> {
            handleDelete();
        });

        // 设置等号按钮点击事件
        findViewById(R.id.button_equals).setOnClickListener(v -> calculateResult());
    }

    private void setButtonClickListener(int buttonId, String text) {
        findViewById(buttonId).setOnClickListener(v -> appendText(text));
    }

    private void appendText(String text) {
        if (isInFractionMode && denominatorStart > 0) {
            // 在分数模式下，我们需要特殊处理
            currentExpression.append(text);
            displayExpression.append(text);
        } else {
            currentExpression.append(text);
            displayExpression.append(text);
        }
        updateDisplay();
        calculateResult();
    }

    private void appendOperator(String operator) {
        // 退出特殊输入模式
        isInFractionMode = false;
        isInSuperscriptMode = false;
        numeratorStart = -1;
        denominatorStart = -1;

        currentExpression.append(operator);
        displayExpression.append(operator);
        updateDisplay();
        calculateResult();
    }

    private void appendFunction(String function, String displayFunction) {
        // 退出特殊输入模式
        isInFractionMode = false;
        isInSuperscriptMode = false;
        numeratorStart = -1;
        denominatorStart = -1;

        currentExpression.append(function);
        displayExpression.append(displayFunction);
        updateDisplay();
    }

    private void appendConstant(String constant, String displayConstant) {
        // 退出特殊输入模式
        isInFractionMode = false;
        isInSuperscriptMode = false;
        numeratorStart = -1;
        denominatorStart = -1;

        currentExpression.append(constant);
        displayExpression.append(displayConstant);
        updateDisplay();
        calculateResult();
    }

    private void appendSquareRoot() {
        // 退出特殊输入模式
        isInFractionMode = false;
        isInSuperscriptMode = false;
        numeratorStart = -1;
        denominatorStart = -1;

        currentExpression.append("sqrt(");
        displayExpression.append("√(");
        updateDisplay();
    }

    private void appendSquare() {
        // 检查是否有可以平方的数字或表达式
        int lastOperatorIndex = findLastOperatorIndex();
        if (lastOperatorIndex == -1 && currentExpression.length() == 0) {
            // 如果表达式为空，添加一个默认的x²
            currentExpression.append("x^(2)");
            displayExpression.append("x²");
        } else {
            // 为最后一个数字或表达式添加平方
            String subExpression = currentExpression.substring(lastOperatorIndex + 1);
            if (subExpression.isEmpty()) {
                return;
            }
            
            // 检查是否需要添加括号
            boolean needsParentheses = !subExpression.startsWith("(") && 
                                      (subExpression.contains("+") || 
                                       subExpression.contains("-") || 
                                       subExpression.contains("*") || 
                                       subExpression.contains("/"));
            
            currentExpression.delete(lastOperatorIndex + 1, currentExpression.length());
            displayExpression.delete(lastOperatorIndex + 1, displayExpression.length());
            
            if (needsParentheses) {
                currentExpression.append("(").append(subExpression).append(")^(2)");
                displayExpression.append("(").append(subExpression).append(")²");
            } else {
                currentExpression.append(subExpression).append("^(2)");
                displayExpression.append(subExpression).append("²");
            }
        }
        updateDisplay();
        calculateResult();
    }

    private void appendPower() {
        // 退出特殊输入模式
        isInFractionMode = false;
        isInSuperscriptMode = true;
        numeratorStart = -1;
        denominatorStart = -1;

        currentExpression.append("^");
        displayExpression.append("^");
        updateDisplay();
    }

    private void appendFraction() {
        // 退出特殊输入模式
        isInFractionMode = true;
        isInSuperscriptMode = false;
        
        if (numeratorStart == -1) {
            // 开始分数输入，添加分子的开始括号
            numeratorStart = currentExpression.length();
            currentExpression.append("(");
            displayExpression.append("(");
        } else if (denominatorStart == -1) {
            // 结束分子输入，开始分母输入
            denominatorStart = currentExpression.length();
            currentExpression.append(")/(");
            displayExpression.append(")/(");
        }
        updateDisplay();
    }

    private void clearAll() {
        currentExpression.setLength(0);
        displayExpression.setLength(0);
        numeratorStart = -1;
        denominatorStart = -1;
        isInFractionMode = false;
        isInSuperscriptMode = false;
        inputTextView.setText("");
        resultTextView.setText("");
    }

    private void handleDelete() {
        if (currentExpression.length() > 0) {
            currentExpression.deleteCharAt(currentExpression.length() - 1);
            displayExpression.deleteCharAt(displayExpression.length() - 1);
            
            // 更新特殊模式状态
            if (isInFractionMode) {
                if (denominatorStart > 0 && currentExpression.length() < denominatorStart) {
                    denominatorStart = -1;
                } else if (numeratorStart > 0 && currentExpression.length() < numeratorStart) {
                    numeratorStart = -1;
                    isInFractionMode = false;
                }
            }
            
            updateDisplay();
            if (currentExpression.length() > 0) {
                calculateResult();
            } else {
                resultTextView.setText("");
                clearSpecialModeFlags();
            }
        }
    }

    private void clearSpecialModeFlags() {
        numeratorStart = -1;
        denominatorStart = -1;
        isInFractionMode = false;
        isInSuperscriptMode = false;
    }

    private int findLastOperatorIndex() {
        // 查找最后一个运算符的位置
        int lastPlus = currentExpression.lastIndexOf("+");
        int lastMinus = currentExpression.lastIndexOf("-");
        int lastMultiply = currentExpression.lastIndexOf("*");
        int lastDivide = currentExpression.lastIndexOf("/");
        int lastPower = currentExpression.lastIndexOf("^");
        
        return Math.max(Math.max(Math.max(Math.max(lastPlus, lastMinus), lastMultiply), lastDivide), lastPower);
    }

    private void updateDisplay() {
        // 创建一个SpannableStringBuilder来显示格式化的表达式
        SpannableStringBuilder builder = new SpannableStringBuilder(displayExpression.toString());
        
        // 处理分数的特殊显示
        if (isInFractionMode && denominatorStart > 0) {
            // 简单实现：在分子和分母之间添加分隔线效果
            // 在实际应用中，可以使用更复杂的布局来显示分数
            int fractionStart = numeratorStart;
            int fractionEnd = displayExpression.length();
            // 这里只是简单示例，实际分数显示需要更复杂的布局或自定义视图
        }
        
        // 处理上标的特殊显示
        if (isInSuperscriptMode) {
            // 查找^符号的位置并设置上标效果
            int powerIndex = displayExpression.toString().lastIndexOf("^");
            if (powerIndex >= 0 && powerIndex < displayExpression.length() - 1) {
                // 设置从^符号后开始的文本为上标
                builder.setSpan(new SuperscriptSpan(), powerIndex + 1, displayExpression.length(), 0);
                builder.setSpan(new RelativeSizeSpan(0.7f), powerIndex + 1, displayExpression.length(), 0);
            }
        }
        
        // 处理平方根符号的显示
        int sqrtIndex = displayExpression.toString().indexOf("√");
        if (sqrtIndex >= 0) {
            // 查找平方根的结束位置
            int sqrtEndIndex = findMatchingParenthesis(displayExpression.toString(), sqrtIndex + 1);
            if (sqrtEndIndex > sqrtIndex) {
                // 这里可以添加特殊的平方根显示效果
            }
        }
        
        inputTextView.setText(builder);
    }

    private int findMatchingParenthesis(String text, int startIndex) {
        // 查找匹配的右括号
        int openCount = 0;
        for (int i = startIndex; i < text.length(); i++) {
            if (text.charAt(i) == '(') {
                openCount++;
            } else if (text.charAt(i) == ')') {
                if (openCount > 0) {
                    openCount--;
                } else {
                    return i;
                }
            }
        }
        return text.length();
    }

    private void calculateResult() {
        try {
            String expression = currentExpression.toString();
            if (!expression.isEmpty()) {
                // 处理特殊格式的表达式转换为mxparser可识别的格式
                String processedExpression = processExpressionForCalculation(expression);
                Expression exp = new Expression(processedExpression);
                double result = exp.calculate();
                if (!Double.isNaN(result) && !Double.isInfinite(result)) {
                    // 格式化结果，去除末尾的.0
                    String resultStr = formatResult(result);
                    resultTextView.setText(resultStr);
                } else {
                    resultTextView.setText("错误");
                }
            }
        } catch (Exception e) {
            resultTextView.setText("错误");
        }
    }

    private String formatResult(double result) {
        // 格式化结果，避免科学计数法，并去除末尾的.0
        if (Math.abs(result) >= 1e10 || Math.abs(result) < 1e-6 && result != 0) {
            // 使用科学计数法显示大数或极小的数
            return String.format("%.10g", result);
        } else {
            // 普通数字格式
            DecimalFormat df = new DecimalFormat("#.##########");
            return df.format(result);
        }
    }

    private String processExpressionForCalculation(String expression) {
        // 处理表达式，确保mxparser可以正确解析
        // 这里可以添加特殊格式表达式的转换逻辑
        // 例如，将π转换为pi，将分数格式转换为除法等
        String processed = expression;
        
        // 确保函数调用都有正确的括号
        // 这里可以添加更多的表达式处理逻辑
        
        return processed;
    }
}