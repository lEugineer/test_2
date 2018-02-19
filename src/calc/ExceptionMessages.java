package calc;

public enum ExceptionMessages {
    DIVISION_BY_ZERO ("Division by zero"),
    INCORRECT_EXPRESSION ("Incorrect expression"),
    OPERATOR_NOT_SUPPORTED ("Operator not supported");

    private String message;

    ExceptionMessages (String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }
}
