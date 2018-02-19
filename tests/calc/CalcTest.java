package calc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalcTest {

    private static final InputStream sysIn = System.in;
    private static final OutputStream sysOut = System.out;
    private static final ByteArrayOutputStream byteOut = new ByteArrayOutputStream(  );

    @Parameterized.Parameter
    public String expr;
    @Parameterized.Parameter(1)
    public String result;

    @Parameterized.Parameters
    public static Collection<Object[]> getExpressions() {
        return Arrays.asList(new Object[][] {
                {"2 + 2 * 2", "6"},
                {"2 * - 2", "-4"},
                {"-2 +-2", "-4"},
                {"2 + 2 -2", "2"},
                {"(4 + 3) * 2 ^ -2", "1.75"},
                {"(17 ^ 4 + 5 * 974 ^ 33 + 2.24 * 4,75)^0", "1"},
                {"123456789123456789 ^ 2", "1.524157878067367851562262075019052E+34"},
                {"123456789123456789 ^ -2", "6.561000106288201410811846848805654E-35"},
                {"", ExceptionMessages.INCORRECT_EXPRESSION.getMessage()},
                {"5 + 1/0", ExceptionMessages.DIVISION_BY_ZERO.getMessage()},
                {"4 2 * 3", ExceptionMessages.INCORRECT_EXPRESSION.getMessage()},
                {"4a * 5", ExceptionMessages.INCORRECT_EXPRESSION.getMessage()},
                {"5 + a", ExceptionMessages.INCORRECT_EXPRESSION.getMessage()}
        });
    }

    @BeforeClass
    public static void classBefore() {
        System.setOut( new PrintStream( byteOut ));
    }

    @AfterClass
    public static void classAfter() {
        System.setIn( sysIn );
        System.setOut( new PrintStream( sysOut ));
    }

    @After
    public void testAfter() {
        byteOut.reset();
    }

    @Test
    public void testAll () {
        System.setIn(new ByteArrayInputStream( expr.getBytes() ));

        try {
            BigDecimal result = Calculator.evaluateExpression( expr );
            assertEquals( this.result, result.toString() );
        } catch (Exception e) {
            assertEquals( this.result, e.getMessage() );
        }
    }
}
