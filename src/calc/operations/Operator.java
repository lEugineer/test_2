package calc.operations;

import java.math.BigDecimal;

public abstract class Operator {
    protected final String operatorSymbol;
    protected final int precedence;
    protected final Associativity associativity;

    public Operator(String operatorSymbol, int precedence, Associativity associativity) {
        this.operatorSymbol = operatorSymbol;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    public String getOperatorSymbol() {
        return operatorSymbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public abstract BigDecimal with (BigDecimal arg1, BigDecimal arg2);
}
