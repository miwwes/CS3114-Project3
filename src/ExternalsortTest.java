//import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;
//import org.junit.jupiter.api.Test;


/**
 * @author amalone46
 *
 */

public class ExternalsortTest extends TestCase {

    //@Test
    public final void test() {
        String[] args = {"src//sampleInput16.bin"};
        RandomAccessFile in = null;
        try {
            in = new RandomAccessFile("src//sampleInput16.bin", "rw");
        }
        catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        long startLength = 0;
        try {
            startLength = in.length();
        }
        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            Externalsort.main(args);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        try {
            assertEquals(sc.in.getFilePointer(), sc.in.length());
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
