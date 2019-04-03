import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 */
public class replacementSelection {

    /**
     * @param raf
     */
    public replacementSelection(RandomAccessFile raf){
        inFile = raf;
        inputBuffer = new byte[BUFFER_SIZE];
        outputBuffer = new byte[BUFFER_SIZE];
    }
    
    
    /**
     * 
     */
    public void execute() {
        try {
            while (inFile.read(inputBuffer) != -1) {
                
            }
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    /**
     * Called to find the start of each run within the output file
     * @return the number of bytes in the output buffer
     */
    public int getOutputBufLength() {
        // return the number of bytes in the output buffer
        return outputBuffer.length;
    }
    
    /**
     * 
     */
    private static final int BUFFER_SIZE = 8192;
    
    /**
     * 
     */
    minHeap records;
    /**
     * 
     */
    byte[] inputBuffer;
    /**
     * 
     */
    byte[] outputBuffer;
    
    /**
     * 
     */
    RandomAccessFile inFile;
    /**
     * 
     */
    RandomAccessFile outFile;
    /**
     * 
     */
    byte numWords = 0;
    /**
     * 
     */
    short wordOffset = 0;
    /**
     * 
     */
    byte wordLength = 0;
}
