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

public final class Calculator {

    private Calculator(){}

    public static BigDecimal evaluateExpression ( String expr ) throws IllegalArgumentException {
        List<String> tokens = parseExpression( expr );
        List<String> rpnExpression = infixToRPN( tokens );
        return evaluateRPN( rpnExpression );
    }

    private static List<String> parseExpression ( String expr ) {

        List<String> tokens = new LinkedList<>();
        StringBuilder numberConstructor = new StringBuilder();

        expr = expr.replaceAll( ",", "." );

        List<Integer> negNumIdxsList = getNegativeNumbersIndexes( expr );

        for (int i = 0; i < expr.length(); i++) {

            if (negNumIdxsList.size() > 0 && negNumIdxsList.get( 0 ) == i) {
                negNumIdxsList.remove( 0 );


                if (tokens.size() == 0
                || Operators.isOperatorSign( tokens.get( tokens.size() - 1 ) )) {
                    // If last token is an operator
                    // consider '-' as a number sign

                    StringBuilder buf = buildNegativeOperand( expr, i );
                    i += buf.length() - 1;

                    saveOperandClearBuilder( buf, tokens );

                    continue;
                }
            }

            char c = expr.charAt( i );

            if (isNumberSign( c )) {
                numberConstructor.append( c );
                continue;
            }
            //save previously constructed number
            saveOperandClearBuilder( numberConstructor, tokens );

            if (Operators.isOperatorSign( c )) {
                tokens.add( String.valueOf( c ));
                continue;
            }

            if (isBracketSign( c )) {
                tokens.add( String.valueOf( c ) );
                continue;
            }

            if (c != ' ') {
                throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
            }
        }
        saveOperandClearBuilder( numberConstructor, tokens );

        return tokens;
    }

    private static boolean isNumberSign ( char c ) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private static boolean isBracketSign ( char c ) {
        return c == '(' || c == ')';
    }

    private static List<Integer> getNegativeNumbersIndexes ( String expr ) {
        List<Integer> negativeNumbersIndexes = new LinkedList<>();
        Pattern p = Pattern.compile( "-\\s?\\d" );
        Matcher m = p.matcher( expr );
        while (m.find()) {
            negativeNumbersIndexes.add( m.start() );
        }
        return negativeNumbersIndexes;
    }

    private static StringBuilder buildNegativeOperand ( String expr, Integer currentCharIndex ) {
        StringBuilder buf = new StringBuilder();
        buf.append( "-" );
        ++currentCharIndex;

//        while (expr.charAt( currentCharIndex ) == ' '
//            && currentCharIndex < expr.length()) {
//            ++currentCharIndex;
//        }

        for (; currentCharIndex < expr.length(); ++currentCharIndex) {
            char c = expr.charAt( currentCharIndex );
            if (isNumberSign( c )) {
                buf.append( c );
            } else {
                break;
            }
        }

        if (buf.length() < 2) {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

        return buf;
    }

    private static void saveOperandClearBuilder ( StringBuilder operand, List<String> tokens ) {
        if (operand.length() == 0) {
            return;
        }

        tokens.add( operand.toString() );

        operand.setLength( 0 );
        operand.delete( 0, operand.length() );
    }

    private static List<String> infixToRPN ( List<String> inputTokens ) {
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

                    if (currentOperator.getPrecedence() <= stackOperator.getPrecedence()
                    && (currentOperator.getAssociativity() == Associativity.LEFT)) {
                        out.add( stack.pop() );
                        continue;
                    }
                    break;
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

    private static BigDecimal evaluateRPN ( List<String> tokens ) {
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
