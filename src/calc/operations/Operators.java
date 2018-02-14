package calc.operations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class Operators {

    private static final Map<String, Operator> operators = new HashMap<>();

    static {
        Operator o = new Operator("+", 2, Associativity.LEFT) {
            @Override
            public BigDecimal with(BigDecimal arg1, BigDecimal arg2) {
                return arg1.add(arg2);
            }
        };
        operators.put(o.getOperatorSymbol(), o);

        o = new Operator("-", 2, Associativity.LEFT) {
            @Override
            public BigDecimal with(BigDecimal arg1, BigDecimal arg2) {
                return arg1.subtract(arg2);
            }
        };
        operators.put(o.getOperatorSymbol(), o);

        o = new Operator("*", 3, Associativity.LEFT) {
            @Override
            public BigDecimal with(BigDecimal arg1, BigDecimal arg2) {
                return arg1.multiply(arg2);
            }
        };
        operators.put(o.getOperatorSymbol(), o);

        o = new Operator("/", 3, Associativity.LEFT) {
            @Override
            public BigDecimal with(BigDecimal arg1, BigDecimal arg2) {
                return arg1.divide(arg2);
            }
        };
        operators.put(o.getOperatorSymbol(), o);

        o = new Operator("^", 4, Associativity.RIGHT) {
            @Override
            public BigDecimal with(BigDecimal arg1, BigDecimal arg2) {
                return arg1.pow (arg2.intValue(), MathContext.DECIMAL128);
            }
        };
        operators.put(o.getOperatorSymbol(), o);
    }

    public static boolean isOperatorSign(String key) {
        return operators.containsKey(key);
    }

    public static Operator getOperator(String key) {
        return operators.get(key);
    }

    public static BigDecimal doMath(String key, BigDecimal arg1, BigDecimal arg2) throws IllegalArgumentException {
        Operator operation = operators.get(key);

        if (operation == null) {
            throw new IllegalArgumentException("Operator '" + key + "' not supported");
        }
        return operation.with(arg1, arg2);
    }
}