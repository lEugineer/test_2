package calc.operations;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Operator {
    private final Character operatorSymbol;
    private final int precedence;
    private final Associativity associativity;
    private final int requiredOperandsCount;

    Operator ( Character operatorSymbol, int precedence, Associativity associativity, int requiredOperandsCount ) {
        this.operatorSymbol = operatorSymbol;
        this.precedence = precedence;
        this.associativity = associativity;
        this.requiredOperandsCount = requiredOperandsCount;
    }

    public abstract BigDecimal with ( BigDecimal... args );

    public Character getOperatorSign () {
        return operatorSymbol;
    }

    public int getPrecedence () {
        return precedence;
    }

    public Associativity getAssociativity () {
        return associativity;
    }

    public int getRequiredOperandsCount () {
        return requiredOperandsCount;
    }

    @Override
    public boolean equals ( Object o ) {
        if (this == o) return true;
        if (!(o instanceof Operator)) return false;
        Operator operator = (Operator) o;
        return Objects.equals( getOperatorSign(), operator.getOperatorSign() );
    }

    @Override
    public int hashCode () {
        return getOperatorSign().hashCode();
    }

}
