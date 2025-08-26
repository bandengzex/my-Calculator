package com.scientific.calculator;

import android.text.SpannableStringBuilder;
import android.text.style.SuperscriptSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

public class MathExpressionRenderer {
    
    public static SpannableStringBuilder renderExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            return new SpannableStringBuilder("");
        }
        
        SpannableStringBuilder rendered = new SpannableStringBuilder();
        
        // 替换符号为更美观的数学符号
        expression = expression.replace("*", "×");
        expression = expression.replace("/", "÷");
        expression = expression.replace("-", "−");
        expression = expression.replace("sqrt", "√");
        
        // 处理指数显示
        int powerIndex = expression.indexOf("^");
        while (powerIndex != -1) {
            String before = expression.substring(0, powerIndex);
            String after = expression.substring(powerIndex + 1);
            
            // 找到指数部分
            StringBuilder exponent = new StringBuilder();
            int i = 0;
            
            // 处理括号内的指数
            if (i < after.length() && after.charAt(i) == '(') {
                int bracketCount = 1;
                exponent.append('(');
                i++;
                while (i < after.length() && bracketCount > 0) {
                    char c = after.charAt(i);
                    exponent.append(c);
                    if (c == '(') bracketCount++;
                    else if (c == ')') bracketCount--;
                    i++;
                }
            } else {
                // 处理单个字符或数字的指数
                while (i < after.length() && (Character.isDigit(after.charAt(i)) || 
                       Character.isLetter(after.charAt(i)))) {
                    exponent.append(after.charAt(i));
                    i++;
                }
            }
            
            if (exponent.length() > 0) {
                rendered.append(before);
                
                // 添加底数
                int baseStart = rendered.length();
                rendered.append(before.substring(Math.max(0, before.length() - 1)));
                
                // 添加上标的指数
                int expStart = rendered.length();
                rendered.append(exponent.toString());
                rendered.setSpan(new SuperscriptSpan(), expStart, expStart + exponent.length(), 0);
                rendered.setSpan(new RelativeSizeSpan(0.7f), expStart, expStart + exponent.length(), 0);
                
                expression = after.substring(i);
            } else {
                rendered.append(before);
                rendered.append("^");
                expression = after;
            }
            
            powerIndex = expression.indexOf("^");
        }
        
        rendered.append(expression);
        
        // 为不同元素添加颜色
        colorizeExpression(rendered);
        
        return rendered;
    }
    
    private static void colorizeExpression(SpannableStringBuilder text) {
        String str = text.toString();
        
        // 为运算符添加颜色
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '+' || c == '−' || c == '×' || c == '÷' || c == '^') {
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#ff8800")), i, i + 1, 0);
            }
            // 为函数名添加颜色
            else if (i < str.length() - 2 && 
                     str.substring(i, i + 3).matches("sin|cos|tan|log|ln")) {
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), i, i + 3, 0);
                i += 2;
            }
            // 为根号添加颜色
            else if (c == '√') {
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), i, i + 1, 0);
            }
        }
    }
    
    public static String formatForDisplay(String expression) {
        if (expression == null || expression.isEmpty()) {
            return "";
        }
        
        // 替换基本符号
        expression = expression.replace("*", "×");
        expression = expression.replace("/", "÷");
        expression = expression.replace("-", "−");
        expression = expression.replace("sqrt", "√");
        
        // 处理括号
        expression = expression.replace("(", "(");
        expression = expression.replace(",", ")");
        
        // 处理常数
        expression = expression.replace("pi", "π");
        
        return expression;
    }
}