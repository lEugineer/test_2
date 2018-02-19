import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestsMain {
    public static void main ( String[] args ) {
        Result result = JUnitCore.runClasses(MyTestsSuit.class);

        for(Failure f : result.getFailures()) {
            System.out.println(f.getTestHeader() + " : " + f.getMessage());
        }

        System.out.println("Tests : " + result.getRunCount());
        System.out.println("Failed : " + result.getFailureCount());
        System.out.println("Time(ms) : " + result.getRunTime());
        System.out.println(result.wasSuccessful());
    }
}
