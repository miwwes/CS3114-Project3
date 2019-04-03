import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author amalone46
 *
 */
public class replacementSelection {

    public replacementSelection(RandomAccessFile raf){
        inFile = raf;
        inputBuffer = new byte[BUFFER_SIZE];
        outputBuffer = new byte[BUFFER_SIZE];
    }
    
    
    public void execute() {
        try {
            byte[] heapArray = new byte[HEAP_SIZE];
            inFile.read(heapArray);
            records = new MinHeap(heapArray, 4096);
        
            while(inFile.read(inputBuffer) != -1) {
                
            }
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    private static final int BUFFER_SIZE = 8192;
    private static final int HEAP_SIZE = 8*8192;

    
    
    MinHeap records;
    byte[] inputBuffer;
    byte[] outputBuffer;
    
    RandomAccessFile inFile;
    RandomAccessFile outFile;
    byte numWords = 0;
    short wordOffset = 0;
    byte wordLength = 0;
}
