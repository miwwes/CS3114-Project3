import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;


/**
 * @author amalone46
 *
 */
public class ExternalsortTest {

    //@Test
    final void test() throws IOException {
        String[] args = {"sampleInput16.bin"};
        final PrintStream standard = System.out;
        File results = new File("results.bin");
        final PrintStream output = new PrintStream(results);
        System.setOut(output);
        Externalsort.main(args);
        System.setOut(standard);
        assertTrue(results.exists());
        assertTrue(results.canRead());
    }

}
