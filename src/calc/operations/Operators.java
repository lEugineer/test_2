package calc.operations;

import calc.ExceptionMessages;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Operators {

    private static final Map<String, Operator> operators = new HashMap<>();

    static {
        operators.put( ArithmeticSigns.ADD.toString(),
                new Operator( ArithmeticSigns.ADD.getChar(), 2, Associativity.LEFT, 2 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 2)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return args[0].add( args[1] );
                    }
                });

        operators.put( ArithmeticSigns.SUB.toString(),
                new Operator( ArithmeticSigns.SUB.getChar(), 2, Associativity.LEFT, 2 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 2)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return args[0].subtract( args[1] );
                    }
                });

        operators.put( ArithmeticSigns.MUL.toString(),
                new Operator( ArithmeticSigns.MUL.getChar(), 3, Associativity.LEFT, 2 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 2)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return args[0].multiply( args[1] );
                    }
                });

        operators.put( ArithmeticSigns.DIV.toString(),
                new Operator( ArithmeticSigns.DIV.getChar(), 3, Associativity.LEFT, 2 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 2)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return args[0].divide( args[1] );
                    }
                });

        operators.put( ArithmeticSigns.POW.toString(),
                new Operator( ArithmeticSigns.POW.getChar(), 4, Associativity.RIGHT, 2 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 2)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return BigDecimal.valueOf( Math.pow( args[0].doubleValue(), args[1].doubleValue() ));
                    }
                });

        operators.put( ArithmeticSigns.NEG.toString(),
                new Operator( ArithmeticSigns.NEG.getChar(), 4, Associativity.RIGHT, 1 ) {
                    @Override
                    public BigDecimal with ( BigDecimal... args ) {
                        if (args.length != 1)
                            throw new IllegalArgumentException( ExceptionMessages.INVALID_ARGUMENTS.getMessage() );
                        return args[0].negate();
                    }
                });
    }


    public static boolean isOperatorSign ( String key ) {
        return operators.containsKey( key );
    }

    public static boolean isOperatorSign ( char key ) {
        return operators.containsKey( String.valueOf( key ) );
    }

    public static Operator getOperator ( String key ) {
        return operators.get( key );
    }

    public static BigDecimal doMath ( String key, BigDecimal... args) throws IllegalArgumentException {
        Operator operation = operators.get( key );

        if (operation == null) {
            throw new IllegalArgumentException( key + " : " + ExceptionMessages.OPERATOR_NOT_SUPPORTED.getMessage() );
        }

        return operation.with( args );
    }
}