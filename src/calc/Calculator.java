package calc;

import calc.operations.ArithmeticSigns;
import calc.operations.Associativity;
import calc.operations.Operator;
import calc.operations.Operators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static calc.operations.ArithmeticSigns.LBR;
import static calc.operations.ArithmeticSigns.RBR;

public class Calculator {

    public BigDecimal evaluateExpression ( String expr ) throws IllegalArgumentException {
        List<String> tokens = parseExpression( expr );
        List<String> rpnExpression = convertToRPN( tokens );
        return evaluateRPN( rpnExpression );
    }

    private List<String> parseExpression ( String expr ) {
        List<String> tokens = new LinkedList<>();

        expr = expr.replaceAll( ",", "." );

        int currentCharIdx = 0;
        while (currentCharIdx < expr.length()) {

            currentCharIdx = processIfOperand( expr, tokens, currentCharIdx );

            currentCharIdx = processIfOperator( expr, tokens, currentCharIdx );
        }

        return tokens;
    }

    private int processIfOperand ( String expr, List<String> tokens, int charIdx) {
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

        if (!ArithmeticSigns.isArithmeticSign( c )) {
            if (!Character.isSpaceChar( c ))
                throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
            else
               return ++charIdx;
        }

        if (c == ArithmeticSigns.ADD.getChar()
        && isUnarOperator( tokens )) {
            return ++charIdx;
        }

        if (c == ArithmeticSigns.SUB.getChar()) {
            if (isUnarOperator( tokens )) {
                tokens.add( ArithmeticSigns.NEG.toString() );
            } else {
                tokens.add( ArithmeticSigns.SUB.toString() );
            }
        } else {
            tokens.add( String.valueOf( c ));
        }

        return ++charIdx;
    }

    private boolean isUnarOperator ( List<String> tokens) {
        // первый токен - значит UNAR
        // последний токен - '(', значит UNAR
        // последний токен - оператор, значит UNAR
        return tokens.size() == 0
                || tokens.get( tokens.size() - 1 ).equals( ArithmeticSigns.LBR.toString() )
                || Operators.isOperatorSign( tokens.get( tokens.size() - 1 ) );
    }

    private boolean isNumberSign ( char c ) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private boolean isBracketSign ( char c ) {
        return c == LBR.getChar() || c == RBR.getChar();
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

        for (String token : inputTokens) {
            if (Operators.isOperatorSign( token )) {

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
            } else if (token.equals( LBR.toString() )) {
                stack.push( token );
            } else if (token.equals( RBR.toString() )) {
                while (!stack.empty() && !stack.peek().equals( LBR.toString() )) {
                    out.add( stack.pop() );
                }
                stack.pop();
            }

            // token is a number
            else {
                out.add( token );
            }
        }

        while (!stack.empty()) {
            out.add( stack.pop() );
        }
        return out;
    }

    private BigDecimal evaluateRPN ( List<String> tokens ) {
        Stack<BigDecimal> stack = new Stack<>();

        for (String token : tokens) {
            Operator o = Operators.getOperator( token );
            if ( o == null ) {
                stack.push( new BigDecimal( token ) );
            } else {
                if (stack.size() < o.getRequiredOperandsCount()) {
                    throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
                }

                BigDecimal[] operands = new BigDecimal[ o.getRequiredOperandsCount() ];
                for(int i = o.getRequiredOperandsCount()-1; i >= 0 ; --i ) {
                    operands[i] = stack.pop();
                }

                BigDecimal result = Operators.doMath( token, operands );

                stack.push( result );
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

        return stack.pop();
    }
}
