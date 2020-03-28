package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/*
 * This is a calculator that works with main arithmetic operations, such as + - / *
 * It provides a method evaluate(String expression), that first checks if the given expression valid or not.
 * It checks if the expression isn't null, empty, if the brackets (if they are in the given expression) are balanced, then checks the expression with regex.
 * This calculator uses uses two stacks: one stack is for operators (including brackets) and another is for numbers.
 * Every single arithmetic operation has its own priority, which is required to determine whether the operation in stack should be performed or not.
 * Brackets have no priority and to be processed with particular methods.
 *
 */

public class Calculator {

    private static final Map<Character, Integer> operationsPriorities = new HashMap<>();
    private final Stack<Character> operators = new Stack<>();
    private final Stack<Double> numbers = new Stack<>();

    {
        operationsPriorities.put('+', 1);
        operationsPriorities.put('-', 1);
        operationsPriorities.put('/', 2);
        operationsPriorities.put('*', 2);
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    public String evaluate (String statement){
        if (!isExpressionValid(statement))
            return null;

        char[] tokens = statement.toCharArray();
        String number = "";

        try {
            for (char token : tokens) {
                if (Character.isDigit(token) || token == '.') {
                    number += Character.toString(token);
                } else {
                    if (!number.isEmpty()) {
                        numbers.push(Double.parseDouble(number));
                        number = "";
                    }

                    if (token == '(' || token == ')') {
                        processBracket(token);
                    } else {
                        processOperator(token);
                    }
                }
            }
            if (!number.isEmpty()) {
                numbers.push(Double.parseDouble(number));
            }
            performLastOperationsInStack();
        } catch (ArithmeticException e) {
            return null;
        }

        Double result = numbers.pop();
        return formatResult(result);
    }

    /*
     * Expression validation
     */
    private boolean isExpressionValid(String expression) {
        if (expression == null)
            return false;
        else if (expression.isEmpty())
            return false;

        if (!areBracketsBalanced(expression))
            return false;

        String expressionWithoutBrackets = deleteBracketsFromExpression(expression);
        String regex = "([0-9]+)?(([-+/*]([0-9]))?([.][0-9]+)?)*$";
        return expressionWithoutBrackets.matches(regex);
    }

    private boolean areBracketsBalanced(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Character> brackets = new Stack<>();

        for (Character token : tokens) {
            if (token.equals('('))
                brackets.push(token);

            if (token.equals(')')) {
                if (brackets.empty())
                    return false;
                else
                    brackets.pop();
            }
        }
        return brackets.empty();
    }

    private String deleteBracketsFromExpression(String expression) {
        String expressionWithoutBrackets = "";
        char[] tokens = expression.toCharArray();
        for (Character token : tokens) {
            if (token == '(' || token == ')') {
                continue;
            } else {
                expressionWithoutBrackets += Character.toString(token);
            }
        }
        return expressionWithoutBrackets;
    }

    /*
     * Processing
     */
    private void processOperator(Character operator) throws ArithmeticException {
        Integer operationPriority = operationsPriorities.get(operator);

        if (operator == '(' || operator == ')') {
            processBracket(operator);
            return;
        }

        if (operators.empty()) {
            operators.push(operator);
        } else {
            Character lastOperationInStack = operators.peek();

            if (lastOperationInStack == '(' || operationsPriorities.get(lastOperationInStack) < operationPriority) {
                operators.push(operator);
            } else {
                performLastOperationsInStack();
                processOperator(operator);
            }
        }
    }

    private void processBracket(Character bracket) throws ArithmeticException {
        if (bracket == '(') {
            operators.push(bracket);
        } else if (bracket == ')') {
            performLastOperationsInStack();
            if (operators.peek() == '(') {
                operators.pop();
            }
        }
    }

    private void performLastOperationsInStack() throws ArithmeticException {
        while (!operators.empty()) {
            if (operators.peek() == '(') {
                break;
            }

            Character lastOperationInStack = operators.peek();
            Double y = numbers.pop();
            Double x = numbers.pop();
            operators.pop();

            Double calculated = performCalculation(lastOperationInStack, x, y);
            numbers.push(calculated);
        }
    }

    private Double performCalculation(Character operation, Double x, Double y) throws ArithmeticException {
        Double result = null;

        switch(operation) {
            case ('+'):
                result = x + y;
                break;

            case ('-'):
                result = x - y;
                break;

            case ('*'):
                result = x * y;
                break;

            case ('/'):
                if (y == 0) {
                    throw new ArithmeticException("dividing by zero");
                }
                result = x / y;
                break;
        }
        return result;
    }

    /*
     * Formatting result
     */
    private String formatResult(Double result) {
        DecimalFormat df = new DecimalFormat("#.####", DecimalFormatSymbols.getInstance(Locale.US) );
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(result);
    }

}
