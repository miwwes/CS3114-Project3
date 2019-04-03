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
            byte[] heapArray = new byte[HEAP_SIZE];
            inFile.read(heapArray);
            records = new minHeap(heapArray, 4096);
        
            while(inFile.read(inputBuffer) != -1) {
                
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
        System.arraycopy(records.getMin(), 0, outputBuffer, 0, 16);
        //outputBuffer[0] = records.getMin();
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
    private static final int HEAP_SIZE = 8*8192;

    
    
    /**
     * 
     */
    private minHeap records;
    /**
     * 
     */
    private byte[] inputBuffer;
    /**
     * 
     */
    private byte[] outputBuffer;
    
    /**
     * 
     */
    private RandomAccessFile inFile;
    /**
     * 
     */
    private RandomAccessFile outFile;
    /**
     * 
     */
    private byte numWords = 0;
    /**
     * 
     */
    private short wordOffset = 0;
    /**
     * 
     */
    private byte wordLength = 0;
}
