import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import student.TestCase;


import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author amalone46
 *
 */
public class ExternalsortTest extends TestCase {

    //@Test
    public final void test() throws IOException {
        String[] args = {"sampleInput16.bin"};
        File in = new File("sampleInput16.bin");
        long startLength = in.length();
        Externalsort.main(args);
        sortContainer sc = Externalsort.getSortContainer();
        long endLength = sc.in.length();
        assertEquals(startLength, endLength);
        assertEquals(sc.in.getFilePointer(), sc.in.length());
    }

}
