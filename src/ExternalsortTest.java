

import java.io.File;
import java.io.IOException;
import student.TestCase;


/**
 * 
 */

/**
 * @author amalone46
 *
 */

public class ExternalsortTest extends TestCase {
    
    public final void test() throws IOException {
        String[] args = {"src//sampleInput16.bin"};
        File in = new File("src//sampleInput16.bin");
        long startLength = in.length();
        Externalsort.main(args);
        sortContainer sc = Externalsort.getSortContainer();
        long endLength = 0;
        try {
            endLength = sc.in.length();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(startLength, endLength);
    }

}
