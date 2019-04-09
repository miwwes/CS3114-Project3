import java.io.IOException;
import student.TestCase;


/**
 * 
 */

/**
 * @author abbym1
 * @author juliam8
 * @version 4-8-19
 * 
 * This is the test file for the over-arching Externalsort class
 *
 */
public class ExternalsortTest extends TestCase {
    
    /**
     * @throws IOException
     */
    public void test() throws IOException {
        String[] args = {"src//b.bin"};
        Externalsort.main(args);
        SortContainer sc = Externalsort.getSortContainer();
        assertTrue(sc.heap().empty());
        assertTrue(sc.inputBuffer().empty());
        assertTrue(sc.outputBuffer().empty());
    }

}

