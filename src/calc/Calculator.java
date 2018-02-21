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

import static calc.operations.ArithmeticSigns.*;

public class Calculator {

    public BigDecimal evaluateExpression ( String expr ) throws IllegalArgumentException {
        List<String> tokens = parseExpression( expr );
        List<String> rpnExpression = convertToRPN( tokens );
        return evaluateRPN( rpnExpression );
    }

    private List<String> parseExpression ( String expr ) {
        List<String> tokens = new LinkedList<>();

        expr = expr.replaceAll( ",", "." );

        char c;
        ArithmeticSigns sign;
        int bracketBalance = 0;

        for (int currentCharIdx = 0; currentCharIdx < expr.length(); currentCharIdx++) {
            c = expr.charAt( currentCharIdx );

            if (isNumberSign( c )) {
                currentCharIdx = saveOperand( expr, tokens, currentCharIdx );
            } else if ( (sign = getArithmeticSign( c )) != null ) {
                if (sign == LBR) {
                    ++bracketBalance;
                } else if (sign == RBR) {
                    --bracketBalance;
                }
                saveOperator( sign, tokens );
            } else if ( !Character.isSpaceChar( c ) ) {
                throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
            }
        }

        if (bracketBalance != 0) {
            throw new IllegalArgumentException( ExceptionMessages.BRACKET_MISSED.getMessage() );
        }

        return tokens;
    }

    private int saveOperand ( String expr, List<String> tokens, int charIdx) {

        char c = expr.charAt( charIdx );
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

        tokens.add( numBuilder.toString() );

        return --charIdx;
    }

    private void saveOperator ( ArithmeticSigns sign, List<String> tokens) {
        if (sign == ADD  && isUnarOperator( tokens )) {
            return;
        }

        if (sign == SUB && isUnarOperator( tokens )) {
            sign = NEG;
        }

        tokens.add( sign.toString() );
    }

    private boolean isUnarOperator ( List<String> tokens) {
        // первый токен - значит UNAR
        // последний токен - '(', значит UNAR
        // последний токен - оператор, значит UNAR
        return tokens.size() == 0
                || tokens.get( tokens.size() - 1 ).equals( LBR.toString() )
                || Operators.isOperatorSign( tokens.get( tokens.size() - 1 ) );
    }

    private boolean isNumberSign ( char c ) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    private List<String> convertToRPN ( List<String> inputTokens ) {

        if (inputTokens.size() < 1) {
            throw new IllegalArgumentException( ExceptionMessages.INCORRECT_EXPRESSION.getMessage() );
        }

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
            Operator operator = Operators.getOperator( token );
            if ( operator == null ) {
                stack.push( new BigDecimal( token ) );
            } else {
                if (stack.size() < operator.getRequiredOperandsCount()) {
                    throw new IllegalArgumentException( ExceptionMessages.OPERAND_MISSED.getMessage() );
                }

                BigDecimal[] operands = new BigDecimal[ operator.getRequiredOperandsCount() ];
                for(int i = operator.getRequiredOperandsCount()-1; i >= 0 ; --i ) {
                    operands[i] = stack.pop();
                }

                BigDecimal result = operator.with( operands );

                stack.push( result );
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException( ExceptionMessages.OPERATOR_MISSED.getMessage() );
        }

        return stack.pop();
    }
}
