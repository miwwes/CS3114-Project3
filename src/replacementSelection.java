import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;

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
        File newFile = new File("output.bin");
        try {
            newFile.createNewFile();
            outFile = new RandomAccessFile(newFile, "rw");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        inputBuffer = new buffer();
        outputBuffer = new buffer();
    }
    
    
    /**
     * Executes the reading of the input file
     */
    public void execute() {
        try {
            byte[] heapArray = new byte[HEAP_SIZE];
            inFile.read(heapArray);
            records = new minHeap(heapArray, 4096, 4096);
            records.buildHeap();
            
            long runStart = outFile.getFilePointer();
            int numRuns = 1;
        
            while ( inFile.getFilePointer() != inFile.length() ) {
                
                runStart = outFile.getFilePointer();
                
                if( inputBuffer.empty() ) {
                    inFile.read(inputBuffer.array());
                    inputBuffer.update();
                }
                
                while ( records.heapSize() > 0 ) {
                    if ( outputBuffer.full() ) {
                        outFile.write(outputBuffer.array());
                        outputBuffer.clear();
                    }
                    if ( inputBuffer.empty() ) {
                        inFile.read(inputBuffer.array());
                        inputBuffer.update();
                    }
                    
                    byte[] minVal = records.getMin();
                    outputBuffer.insert(minVal);
                    if (compareRecords(inputBuffer.read(), minVal) > 0 ) {
                        records.modify(0, inputBuffer.remove());
                    }
                    else {
                        records.removemin(inputBuffer.remove());
                    }
                    
                }
                // heap is empty 
                
                outFile.write(Arrays.copyOfRange(outputBuffer.array(), 0, outputBuffer.array().length));
                outputBuffer.clear();
                
                long end = outFile.getFilePointer();
                runNode n = new runNode(numRuns, runStart, end);
                runs.push(n);
                
                numRuns++;
                records.buildHeap(4096);
                
            }
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    /**
     * @param rec1
     * @param rec2
     * @return
     */
    int compareRecords(byte[] rec1, byte[] rec2) {
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 8, 16));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 8, 16));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }
    
    /**
     * Executes the reading of the input file
     */
    public void selection() {
        // first place the root into the output buffer
        // set first 16 bytes to min of heap

        //System.arraycopy(records.getMin(), 0, outputBuffer, 0, 16);
        //outputBuffer[0] = records.getMin();
        //while (!outputBuffer.full()) {
            
        //}
    }
    
    /**
     * Called to find the start of each run within the output file
     * @return the number of bytes in the output buffer
     */
    public int addToOutputBuf(byte[] record) {
        // return the number of bytes in the output buffer
        return outputBuffer.size();
    }
    
    /**
     * Called to find the start of each run within the output file
     * @return the number of bytes in the output buffer
     */
    public int getOutputBufLength() {
        // return the number of bytes in the output buffer
        return outputBuffer.size();
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
    private LinkedList<runNode> runs;
    /**
     * 
     */
    private buffer inputBuffer;
    /**
     * 
     */
    private buffer outputBuffer;
    
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
