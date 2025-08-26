package com.scientific.calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathExpressionEvaluatorTest {
    
    private MathExpressionEvaluator evaluator = new MathExpressionEvaluator();
    
    @Test
    public void testBasicOperations() throws Exception {
        assertEquals(4.0, evaluator.evaluate("2+2"), 0.001);
        assertEquals(5.0, evaluator.evaluate("10-5"), 0.001);
        assertEquals(6.0, evaluator.evaluate("2*3"), 0.001);
        assertEquals(2.5, evaluator.evaluate("5/2"), 0.001);
    }
    
    @Test
    public void testScientificFunctions() throws Exception {
        assertEquals(1.0, evaluator.evaluate("sin(90)"), 0.001);
        assertEquals(0.0, evaluator.evaluate("cos(90)"), 0.001);
        assertEquals(1.0, evaluator.evaluate("tan(45)"), 0.001);
        assertEquals(2.0, evaluator.evaluate("log(100)"), 0.001);
        assertEquals(1.0, evaluator.evaluate("ln(2.718)"), 0.01);
        assertEquals(3.0, evaluator.evaluate("sqrt(9)"), 0.001);
    }
    
    @Test
    public void testComplexExpressions() throws Exception {
        assertEquals(7.0, evaluator.evaluate("(2+3)*2-3"), 0.001);
        assertEquals(16.0, evaluator.evaluate("2^4"), 0.001);
        assertEquals(8.0, evaluator.evaluate("2^(1+2)"), 0.001);
    }
    
    @Test
    public void testConstants() throws Exception {
        assertEquals(Math.PI, evaluator.evaluate("π"), 0.001);
        assertEquals(Math.E, evaluator.evaluate("e"), 0.001);
    }
    
    @Test(expected = Exception.class)
    public void testDivisionByZero() throws Exception {
        evaluator.evaluate("1/0");
    }
    
    @Test(expected = Exception.class)
    public void testInvalidExpression() throws Exception {
        evaluator.evaluate("2++3");
    }
}