package com.scientific.calculator;

import java.util.Stack;
import java.util.regex.Pattern;

public class MathExpressionEvaluator {
    
    public double evaluate(String expression) throws Exception {
        // 预处理表达式
        expression = preprocessExpression(expression);
        
        // 检查括号匹配
        if (!isBalanced(expression)) {
            throw new Exception("括号不匹配");
        }
        
        // 计算表达式
        return evaluateExpression(expression);
    }
    
    private String preprocessExpression(String expression) {
        expression = expression.replace("π", String.valueOf(Math.PI));
        expression = expression.replace("e", String.valueOf(Math.E));
        expression = expression.replace("×", "*");
        expression = expression.replace("÷", "/");
        expression = expression.replace("−", "-");
        expression = expression.replace("√", "sqrt");
        expression = expression.replace("^", "**");
        expression = expression.replace(" ", "");
        
        return expression;
    }
    
    private boolean isBalanced(String expression) {
        Stack<Character> stack = new Stack<>();
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }
    
    private double evaluateExpression(String expression) throws Exception {
        return new ExpressionParser(expression).parse();
    }
    
    private class ExpressionParser {
        private String expression;
        private int pos = 0;
        
        public ExpressionParser(String expression) {
            this.expression = expression;
        }
        
        public double parse() throws Exception {
            double result = parseAddition();
            if (pos < expression.length()) {
                throw new Exception("无效的表达式");
            }
            return result;
        }
        
        private double parseAddition() throws Exception {
            double result = parseMultiplication();
            
            while (pos < expression.length()) {
                char operator = expression.charAt(pos);
                if (operator == '+' || operator == '-') {
                    pos++;
                    double operand = parseMultiplication();
                    if (operator == '+') {
                        result += operand;
                    } else {
                        result -= operand;
                    }
                } else {
                    break;
                }
            }
            
            return result;
        }
        
        private double parseMultiplication() throws Exception {
            double result = parsePower();
            
            while (pos < expression.length()) {
                char operator = expression.charAt(pos);
                if (operator == '*' || operator == '/') {
                    pos++;
                    double operand = parsePower();
                    if (operator == '*') {
                        result *= operand;
                    } else {
                        if (operand == 0) {
                            throw new Exception("除数不能为零");
                        }
                        result /= operand;
                    }
                } else {
                    break;
                }
            }
            
            return result;
        }
        
        private double parsePower() throws Exception {
            double result = parseUnary();
            
            if (pos < expression.length() && expression.charAt(pos) == '*' && pos + 1 < expression.length() && expression.charAt(pos + 1) == '*') {
                pos += 2;
                double exponent = parsePower();
                result = Math.pow(result, exponent);
            }
            
            return result;
        }
        
        private double parseUnary() throws Exception {
            if (pos < expression.length() && expression.charAt(pos) == '-') {
                pos++;
                return -parseUnary();
            }
            
            return parsePrimary();
        }
        
        private double parsePrimary() throws Exception {
            skipWhitespace();
            
            if (pos >= expression.length()) {
                throw new Exception("意外的表达式结束");
            }
            
            char c = expression.charAt(pos);
            
            if (c == '(') {
                pos++;
                double result = parseAddition();
                if (pos >= expression.length() || expression.charAt(pos) != ')') {
                    throw new Exception("缺少右括号");
                }
                pos++;
                return result;
            }
            
            if (Character.isDigit(c) || c == '.') {
                return parseNumber();
            }
            
            if (Character.isLetter(c)) {
                return parseFunction();
            }
            
            throw new Exception("无效的字符: " + c);
        }
        
        private double parseNumber() throws Exception {
            int start = pos;
            boolean hasDecimal = false;
            
            while (pos < expression.length() && (Character.isDigit(expression.charAt(pos)) || expression.charAt(pos) == '.')) {
                if (expression.charAt(pos) == '.') {
                    if (hasDecimal) {
                        throw new Exception("数字格式错误");
                    }
                    hasDecimal = true;
                }
                pos++;
            }
            
            String numberStr = expression.substring(start, pos);
            try {
                return Double.parseDouble(numberStr);
            } catch (NumberFormatException e) {
                throw new Exception("无效的数字: " + numberStr);
            }
        }
        
        private double parseFunction() throws Exception {
            int start = pos;
            while (pos < expression.length() && Character.isLetter(expression.charAt(pos))) {
                pos++;
            }
            
            String function = expression.substring(start, pos);
            
            if (pos >= expression.length() || expression.charAt(pos) != '(') {
                throw new Exception("函数后缺少括号");
            }
            pos++;
            
            double argument = parseAddition();
            
            if (pos >= expression.length() || expression.charAt(pos) != ')') {
                throw new Exception("缺少右括号");
            }
            pos++;
            
            return evaluateFunction(function, argument);
        }
        
        private double evaluateFunction(String function, double argument) throws Exception {
            switch (function.toLowerCase()) {
                case "sin":
                    return Math.sin(Math.toRadians(argument));
                case "cos":
                    return Math.cos(Math.toRadians(argument));
                case "tan":
                    return Math.tan(Math.toRadians(argument));
                case "log":
                    return Math.log10(argument);
                case "ln":
                    return Math.log(argument);
                case "sqrt":
                    if (argument < 0) {
                        throw new Exception("不能对负数开平方");
                    }
                    return Math.sqrt(argument);
                default:
                    throw new Exception("未知的函数: " + function);
            }
        }
        
        private void skipWhitespace() {
            while (pos < expression.length() && Character.isWhitespace(expression.charAt(pos))) {
                pos++;
            }
        }
    }
}