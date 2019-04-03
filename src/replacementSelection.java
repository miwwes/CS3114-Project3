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
    replacementSelection(RandomAccessFile raf){
        inFile = raf;
        inputBuffer = new byte[BUFFER_SIZE];
        outputBuffer = new byte[BUFFER_SIZE];
    }
    
    
    /**
     * Executes the reading of the input file
     */
    public void execute() {
        try {
            while (inFile.read(inputBuffer) != -1) {
                //read in the data into an array and heapify it!
                
            }
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    /**
     * Executes the reading of the input file
     */
    public void selection() {
        // first place the root into the output buffer
        // set first 16 bytes to min of heap
        outputBuffer[0] = records.getMin();
        while (records.heapSize() > 0) {
            
        }
    }
    
    /**
     * Called to find the start of each run within the output file
     * @return the number of bytes in the output buffer
     */
    public int addToOutputBuf(byte[] record) {
        // return the number of bytes in the output buffer
        return outputBuffer.length;
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
