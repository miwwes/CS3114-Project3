import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author juliam8
 * @author abbym1
 * @version 4-8-19
 * 
 * This class is a test suite for all project test cases.
 * Running the suite runs all the tests at once.
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({ ExternalsortTest.class, BlockNodeTest.class, 
                minHeapTest.class })

public class AllTests {

}
