package calc;

import calc.operations.Associativity;
import calc.operations.Operator;
import calc.operations.Operators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static BigDecimal evaluateExpression(String expr) {
        List<String> tokens = parseExpression(expr);
        List<String> rpnExpression = infixToRPN(tokens);
        return evaluateRPN(rpnExpression);
    }

    private static List<String> parseExpression(String expr) {

        List<String> tokens = new LinkedList<>();
        StringBuilder numberConstructor = new StringBuilder();

        expr = expr.replaceAll(",", ".");

        List<Integer> negNumIdxsList = getNegativeNumbersIndexes(expr);

        for (int i = 0; i < expr.length(); i++) {

            if (negNumIdxsList.size() > 0 && negNumIdxsList.get(0) == i) {
                // If last token isn't an operator
                if (!Operators.isOperatorSign(tokens.get(tokens.size() - 1))) {
                    // consider '-' as an operator, not a number sign
                    continue;
                }

                StringBuilder buf = buildNegativeOperand(expr, i);
                i += buf.length()-1;
                saveOperandTo(buf, tokens);

                negNumIdxsList.remove(0);
                continue;
            }

            Character c = expr.charAt(i);

            if (isNumberRelatedSign(c)) {
                numberConstructor.append(c);
                continue;
            }
            //save previously constructed number
            saveOperandTo(numberConstructor, tokens);

            if (Operators.isOperatorSign(c.toString())) {
                tokens.add(c.toString());
                continue;
            }

            if (isBracketSign(c)) {
                tokens.add(c.toString());
                continue;
            }


            if (!c.equals(' ')) {
                throw new IllegalArgumentException("Incorrect input");
            }
        }
        saveOperandTo(numberConstructor, tokens);

        return tokens;
    }

    private static boolean isNumberRelatedSign(Character c) {
        return (c >= '0' && c <= '9') || c.equals('.');
    }

    private static boolean isBracketSign(Character c) {
        return c.equals('(') || c.equals(')');
    }

    private static List<Integer> getNegativeNumbersIndexes(String expr) {
        List<Integer> negativeNumbersIndexes = new LinkedList<>();
        Pattern p = Pattern.compile("-\\s?\\d");
        Matcher m = p.matcher(expr);
        while (m.find()) {
            negativeNumbersIndexes.add(m.start());
        }
        return negativeNumbersIndexes;
    }

    private static StringBuilder buildNegativeOperand(String expr, Integer currentCharIndex) {
        StringBuilder buf = new StringBuilder();

        for (int i = currentCharIndex; i < expr.length(); i++) {
            Character c = expr.charAt(i);
            if (c.equals('-') || isNumberRelatedSign(c)) {
                buf.append(c);
            } else {
                break;
            }
        }
        return buf;
    }

    private static void saveOperandTo(StringBuilder strBuf, List<String> tokens) {
        if (strBuf.length() == 0) {
            return;
        }

        tokens.add(strBuf.toString());
        strBuf.setLength(0);
        strBuf.delete(0, strBuf.length());
    }

    private static List<String> infixToRPN(List<String> inputTokens) {
        List<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        int operatorsCount = 0;
        int operandsCount = 0;


        for (String token : inputTokens) {
            if (Operators.isOperatorSign(token)) {
                ++operatorsCount;

                Operator currentOperator = Operators.getOperator(token);
                while (!stack.empty() && Operators.isOperatorSign(stack.peek())) {
                    Operator stackOperator = Operators.getOperator(stack.peek());

                    if (currentOperator.getPrecedence() <= stackOperator.getPrecedence() &&
                        (currentOperator.getAssociativity() == Associativity.LEFT)
                    ) {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            }

            // token is a number
            else {
                ++operandsCount;
                out.add(token);
            }
        }

        if (operandsCount - operatorsCount > 1) {
            throw new IllegalArgumentException("Too much operands");
        }

        while (!stack.empty()) {
            out.add(stack.pop());
        }
        return out;
    }

    private static BigDecimal evaluateRPN(List<String> tokens) throws IllegalArgumentException {
        Stack<BigDecimal> stack = new Stack<>();

        for (String token : tokens) {
            if (!Operators.isOperatorSign(token)) {
                stack.push(new BigDecimal(token));
            } else {
                BigDecimal d2 = stack.pop();
                BigDecimal d1 = stack.pop();

                BigDecimal result = Operators.doMath(token, d1, d2);

                stack.push(result);
            }
        }

        return stack.pop();
    }
}
