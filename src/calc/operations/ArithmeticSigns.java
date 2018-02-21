package calc.operations;

public enum ArithmeticSigns {
    ADD ('+'),
    SUB ('-'),
    NEG ('~'),
    MUL ('*'),
    DIV ('/'),
    POW ('^'),
    LBR ('('),
    RBR (')');

    private final char asChar;
    private final String asString;

    ArithmeticSigns (char sign) {
        this.asChar = sign;
        this.asString = String.valueOf( sign );
    }

    public char getChar () {
        return asChar;
    }

    @Override
    public String toString () {
        return asString;
    }

    public static boolean isArithmeticSign (char c) {
        for(ArithmeticSigns sign : ArithmeticSigns.values()) {
            if (sign.asChar == c)
                return true;
        }
        return false;
    }

    public static boolean isArithmeticSign (String c) {
        for(ArithmeticSigns sign : ArithmeticSigns.values()) {
            if (sign.asString.equals( c ))
                return true;
        }
        return false;
    }

    public static ArithmeticSigns getArithmeticSign (char c) {
        for(ArithmeticSigns sign : ArithmeticSigns.values()) {
            if (sign.asChar == c )
                return sign;
        }
        return null;
    }
}
