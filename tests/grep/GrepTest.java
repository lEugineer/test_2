package grep;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GrepTest {

    private static final InputStream sysIn = System.in;
    private static final OutputStream sysOut = System.out;

    private static final ByteArrayInputStream byteIn = new ByteArrayInputStream(
            "abcdf;\nabcd abc;\nbcd adbc abc;\nghij abcd * acdf;\nab ac ad;\n".getBytes()
    );
    private static final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();


    @BeforeClass // once in class creation
    public static void systemStreamInit() {
        System.setIn( byteIn );
        System.setOut( new PrintStream( byteOut ) );
    }

    @AfterClass
    public static void systemStreamReset() {
        System.setIn( sysIn );
        System.setOut( new PrintStream( sysOut ) );
    }

    //execute after each test
    @After
    public void afterTestCase () {
        byteIn.reset();
        byteOut.reset();
    }

    @Test
    public void testNoArguments () {
        try {
            GrepCmd.main( new String[0] );
            fail();
        } catch (Exception e) {
            if ( !(e instanceof IllegalArgumentException) ) {
                fail();
            }
        }
    }

    @Test
    public void testSingleArg () {
        try {
            GrepCmd.main( new String[]{"abcd"} );

            assertEquals( "abcd abc;\nghij abcd * acdf;\n", byteOut.toString() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }

    @Test
    public void testMultipleArgs () {
        try {
            GrepCmd.main( new String[] {"abcd", "abc"} );

            assertEquals( "abcd abc;\nbcd adbc abc;\nghij abcd * acdf;\n", byteOut.toString() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }

    @Test
    public void testRegularExpr () {
        try {
            GrepCmd.main( new String[] {"^ab.+"} );

            assertEquals( "abcdf;\nabcd abc;\nbcd adbc abc;\nghij abcd * acdf;\n", byteOut.toString() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }

    @Test
    public void testIncorrectRegularExpr () {
        try {
            GrepCmd.main( new String[]{"*"} );

            assertEquals( "ghij abcd * acdf;\n", byteOut.toString() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }

    @Test
    public void testNotExist () {
        try {
            GrepCmd.main( new String[] {"pqrst"} );

            assertEquals( "", byteOut.toString() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }
}
