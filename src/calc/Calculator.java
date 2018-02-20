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

    public BigDecimal evaluateExpression ( String expr ) throws IllegalArgumentException {
        List<String> tokens = parseExpression( expr );
        List<String> rpnExpression = convertToRPN( tokens );
        return evaluateRPN( rpnExpression );
    }

    private List<String> parseExpression ( String expr ) {
        List<String> tokens = new LinkedList<>();

        expr = expr.replaceAll( ",", "." );
        List<Integer> negNumIdxsList = getAllNegNumIdxs( expr );

        int currentCharIdx = 0;
        while (currentCharIdx < expr.length()) {

            currentCharIdx = processIfNegNum( expr, tokens, negNumIdxsList, currentCharIdx );

            currentCharIdx = processIfNum( expr, tokens, currentCharIdx );

            currentCharIdx = processIfOperator( expr, tokens, currentCharIdx );
        }

        return tokens;
    }

    private int processIfNum ( String expr, List<String> tokens, int charIdx) {
        if (charIdx >= expr.length()) {
            return charIdx;
        }

        char c = expr.charAt( charIdx );
        if (!isNumberSign( c )) return charIdx;

        StringBuilder numBuilder = new StringBuilder();
        numBuilder.append( c );
        ++charIdx;

        while (charIdx < expr.length()) {
            c = expr.charAt( charIdx );
            if (!isNumberSign( c ))
                break;

            numBuilder.append( c );
            ++charIdx;
        }

        saveOperand( numBuilder, tokens );

        return charIdx;
    }

    private int processIfOperator ( String expr, List<String> tokens, int charIdx) {
        if (charIdx >= expr.length()) {
            return charIdx;
        }

        char c = expr.charAt( charIdx );

        if (Operators.isOperatorSign( c )
        || isBracketSign( c )) {
            tokens.add( String.valueOf( c ));
        } else

        if (c != ' ') {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

        return ++charIdx;
    }

    private int processIfNegNum ( String expr, List<String> tokens, List<Integer> negNumIdxsList, int charIdx) {
        if (negNumIdxsList.size() == 0
        || negNumIdxsList.get( 0 ) != charIdx) {
            return charIdx;
        }
        negNumIdxsList.remove( 0 );

        if (tokens.size() == 0
        || Operators.isOperatorSign( tokens.get( tokens.size() - 1 ) )) {

            StringBuilder numBuilder = new StringBuilder();
            charIdx = buildNegativeOperand( expr, charIdx, numBuilder );

            saveOperand( numBuilder, tokens );
        }

        return charIdx;
    }

    private int buildNegativeOperand ( String expr, Integer charIdx, StringBuilder numBuilder ) {
        numBuilder.append( "-" );
        ++charIdx;

        while (charIdx < expr.length()
        && expr.charAt( charIdx ) == ' ') {
            ++charIdx;
        }

        while ( charIdx < expr.length()) {
            char c = expr.charAt( charIdx );

            if (isNumberSign( c )) {
                numBuilder.append( c );
            } else {
                break;
            }

            ++charIdx;
        }

        if (numBuilder.length() < 2) {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

        return charIdx;
    }

    private boolean isNumberSign ( char c ) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private boolean isBracketSign ( char c ) {
        return c == '(' || c == ')';
    }

    private List<Integer> getAllNegNumIdxs ( String expr ) {
        List<Integer> negNumIdxs = new LinkedList<>();

        Pattern p = Pattern.compile( "-\\s?\\d" );
        Matcher m = p.matcher( expr );

        while (m.find()) {
            negNumIdxs.add( m.start() );
        }

        return negNumIdxs;
    }

    private void saveOperand ( StringBuilder operand, List<String> tokens ) {
        if (operand == null || operand.length() == 0) {
            return;
        }

        tokens.add( operand.toString() );
    }

    private List<String> convertToRPN ( List<String> inputTokens ) {
        List<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        int operatorsCount = 0;
        int operandsCount = 0;

        for (String token : inputTokens) {
            if (Operators.isOperatorSign( token )) {
                ++operatorsCount;

                Operator currentOperator = Operators.getOperator( token );
                while (!stack.empty() && Operators.isOperatorSign( stack.peek() )) {

                    Operator stackOperator = Operators.getOperator( stack.peek() );

                    if (currentOperator.getPrecedence() > stackOperator.getPrecedence()
                    || (currentOperator.getAssociativity() != Associativity.LEFT)) {
                        break;
                    }

                    out.add( stack.pop() );
                }

                stack.push( token );
            } else if (token.equals( "(" )) {
                stack.push( token );
            } else if (token.equals( ")" )) {
                while (!stack.empty() && !stack.peek().equals( "(" )) {
                    out.add( stack.pop() );
                }
                stack.pop();
            }

            // token is a number
            else {
                ++operandsCount;
                out.add( token );
            }
        }

        if (operandsCount - operatorsCount != 1) {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

        while (!stack.empty()) {
            out.add( stack.pop() );
        }
        return out;
    }

    private BigDecimal evaluateRPN ( List<String> tokens ) {
        Stack<BigDecimal> stack = new Stack<>();

        for (String token : tokens) {
            if (!Operators.isOperatorSign( token )) {
                stack.push( new BigDecimal( token ) );
            } else {
                BigDecimal d2 = stack.pop();
                BigDecimal d1 = stack.pop();

                BigDecimal result = Operators.doMath( token, d1, d2 );

                stack.push( result );
            }
        }

        return stack.pop();
    }
}
