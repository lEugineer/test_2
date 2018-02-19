package calc.operations;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Operator {
    private final String operatorSymbol;
    private final int precedence;
    private final Associativity associativity;

    Operator ( String operatorSymbol, int precedence, Associativity associativity ) {
        this.operatorSymbol = operatorSymbol;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    public String getOperatorSymbol () {
        return operatorSymbol;
    }

    public int getPrecedence () {
        return precedence;
    }

    public Associativity getAssociativity () {
        return associativity;
    }

    public abstract BigDecimal with ( BigDecimal arg1, BigDecimal arg2 );

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (!(o instanceof Operator)) return false;
        Operator operator = (Operator) o;
        return Objects.equals( getOperatorSymbol(), operator.getOperatorSymbol() );
    }

    @Override
    public int hashCode () {
        return getOperatorSymbol().hashCode();
    }
}
