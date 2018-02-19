import calc.CalcTest;
import grep.GrepTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( Suite.class )

@Suite.SuiteClasses({
        CalcTest.class,
        GrepTest.class
})

public class MyTestsSuit {

}
