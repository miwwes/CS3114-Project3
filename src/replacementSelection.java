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
     * @param c
     */
    replacementSelection(sortContainer c){
        runs = c.l;
        recordHeap = c.h;
        inFile = c.in;
        outFile = c.runs;
        inBuffer = c.ib;
        outBuffer = c.ob;
    }
   
    public boolean canRead() {
        try {
            boolean test = inFile.getFilePointer() != inFile.length();
            return test;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Executes the reading of the input file
     */
    public void execute() {
        try {
            
            // initialize helper variables
            long runStart = outFile.getFilePointer();
            int numRuns = 0;
            int addCount = 0;
        
            while ( canRead() ) { 
                
                inFile.read(inBuffer.array());
                inBuffer.update();
                
                
                while( !inBuffer.empty() ) {
                
                    if( recordHeap.empty() ) {
                        outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                        outBuffer.clear();
                        
                        long end = outFile.getFilePointer();
                        runNode n = new runNode(numRuns, runStart, end);
                        runs.addLast(n);
                        
                        numRuns++;
                        runStart = end;
                        addCount = 0;
                        recordHeap.buildHeap(MAX_REC_HEAP);
                        
                    }
                    else if ( outBuffer.full() ) {
                        outFile.write(outBuffer.array());
                        outBuffer.clear();
                    }
                    
                    byte[] minVal = recordHeap.getRecord(0);    //get the minimum
                    outBuffer.insert(minVal);
                    byte[] buf = inBuffer.read();
                    //breaks here
                    if (comparerecordHeap(buf, minVal) > 0 ) {
                        recordHeap.modify(0, inBuffer.remove());
                    }
                    else {
                        recordHeap.removemin(inBuffer.remove());
                        addCount++;
                    } 
                
                } // inBuffer is empty
                
            } // inFile is empty
            // could still be stuff in the heap and outBuffer
            if( !outBuffer.empty() ) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                outBuffer.clear();
            }
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    outBuffer.clear();
                }
                outBuffer.insert(recordHeap.removemin());
            }
            
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            outBuffer.clear();
            
            long end = outFile.getFilePointer();
            runNode n = new runNode(numRuns, runStart, end);
            runs.push(n);
            
            numRuns++;
            runStart = end;
            recordHeap.buildHeap(addCount);
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    outBuffer.clear();
                }
                outBuffer.insert(recordHeap.removemin());
            }
            
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            outBuffer.clear();
            end = outFile.getFilePointer();
            runNode n2 = new runNode(numRuns, runStart, end);
            runs.push(n2);
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
    int comparerecordHeap(byte[] rec1, byte[] rec2) {
        if (rec1 == null) {
            System.out.println("ger");
        }
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 8, 16));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 8, 16));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }
    
    /**
     * 
     */
    //private static final int HEAP_SIZE = 8*8192;
    private static final int MAX_REC_HEAP = 4096;

    
    
    /**
     * 
     */
    private minHeap recordHeap;
    
    /**
     * 
     */
    private LinkedList<runNode> runs;
    /**
     * 
     */
    private buffer inBuffer;
    /**
     * 
     */
    private buffer outBuffer;
    
    /**
     * 
     */
    private RandomAccessFile inFile;
    /**
     * 
     */
    private RandomAccessFile outFile;

}
