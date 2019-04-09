// WARNING: This program uses the Assertion class. When it is run,
// assertions must be turned on. For example, under Linux, use:
// java -ea Genfile



import java.io.*;
import java.util.*;
import java.math.*;

/**
 * Generate a data file. The size is a multiple of 8192 bytes.
    Each record is one long and one double.
 * @author CS 3114 Professors Spring 2019
 * @version 4-4-19
 * 
 * This is a provided file to generate bin files for testing
 *
 */
public class Genfile {


    /**
     * Number of records in a block
     */
    static final int NUM_RECS = 512; // Because they are short ints
    
    /**
     * Initialize the random variable
     */
    static private Random value = new Random(); // Hold the Random class object
    
    
    /**
     * @return a random long value
     */
    static long randLong() {
        return value.nextLong();
    }
    
    /**
     * @return a random double value
     */
    static double randDouble() {
        return value.nextDouble();
    }
    
    /**
     * @param args arguments for the filename to generate and its size
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        long val;
        double val2;
        assert (args.length == 2) :
             "\nUsage: Genfile <filename> <size>" +
             "\nOptions \nSize is measured in blocks of 8192 bytes";
    
        int filesize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(args[0])));
    
        for (int i = 0; i < filesize; i++) {
            for (int j = 0; j < NUM_RECS; j++) {
                val = (long)(randLong());
                file.writeLong(val);
                val2 = (double)(randDouble());
                file.writeDouble(val2);
            }
        }

        file.flush();
        file.close();
    }
}
