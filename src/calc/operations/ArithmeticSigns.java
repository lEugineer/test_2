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

    private final char sign;

    ArithmeticSigns (char sign) {
        this.sign = sign;
    }

    public char getChar () {
        return sign;
    }

    @Override
    public String toString () {
        return String.valueOf( sign );
    }

    public static boolean isArithmeticSign (char c) {
        for(ArithmeticSigns sign : ArithmeticSigns.values()) {
            if (sign.getChar() == c)
                return true;
        }
        return false;
    }
}
