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
 * This is the test file for the overarching Externalsort class
 *
 */
public class ExternalsortTest extends TestCase {
    
    public void test() throws IOException {
        String[] args = {"src//sampleInput16.bin"};
        Externalsort.main(args);
        sortContainer sc = Externalsort.getSortContainer();
        assertTrue(sc.h.empty());
        assertTrue(sc.ib.empty());
        assertTrue(sc.ob.empty());
        assertTrue(sc.l.size() == 1);
    }

}

