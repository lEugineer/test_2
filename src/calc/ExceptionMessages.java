package calc;

public enum ExceptionMessages {
    DIVISION_BY_ZERO ("Division by zero"),
    INVALID_ARGUMENTS ("Invalid Arguments"),
    INCORRECT_EXPRESSION ("Incorrect expression"),
    OPERATOR_NOT_SUPPORTED ("Operator not supported"),
    BRACKET_MISSED( "Check brackets" ),
    OPERATOR_MISSED ( "Operator missed" ),
    OPERAND_MISSED( "Operand missed" );

    private String message;

    ExceptionMessages (String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }
}
