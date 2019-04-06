import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
     * @throws IOException 
     */
    replacementSelection(sortContainer c) throws IOException{
        runs = c.l;
        recordHeap = c.h;
        inFile = c.in;
        outFile = c.runs;
        inBuffer = c.ib;
        outBuffer = c.ob;
        try {
            out = new FileOutputStream("afterRepSel.bin");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                inBuffer.clear();
                inFile.read(inBuffer.array());
                inBuffer.update();
                
                
                while( !inBuffer.doneReading() ) {
                
                    if( recordHeap.empty() ) {
                        outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                        out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                        outBuffer.clear();
                        
                        long end = outFile.getFilePointer();
                        runNode n = new runNode(numRuns, runStart, end);
                        runs.add(n);
                        
                        numRuns++;
                        runStart = end;
                        addCount = 0;
                        recordHeap.buildHeap(MAX_REC_HEAP);
                        
                    }
                    else if ( outBuffer.full() ) {
                        outFile.write(outBuffer.array());
                        out.write(outBuffer.array());
                        outBuffer.clear();
                    }
                    
                    byte[] minVal = recordHeap.getRecord(0);    //get the minimum
                    outBuffer.insert(minVal);
                    //System.out.println(Arrays.toString(outBuffer.array()));
                    byte[] buf = inBuffer.read();
                    //breaks here
                    if (comparerecordHeap(buf, minVal) > 0 ) {
                        
                        /**byte[] idBytes = Arrays.copyOfRange(buf, 0, 8);
                        byte[] keyBytes = Arrays.copyOfRange(buf, 8, 16);
                        long id = ByteBuffer.wrap(idBytes).getLong();
                        double key = ByteBuffer.wrap(keyBytes).getDouble();
                        System.out.println("id1: " + id);
                        System.out.println("key1: " + key);
                        byte[] idB = Arrays.copyOfRange(minVal, 0, 8);
                        byte[] keyB = Arrays.copyOfRange(minVal, 8, 16);
                        long i = ByteBuffer.wrap(idB).getLong();
                        double k = ByteBuffer.wrap(keyB).getDouble();
                        System.out.println("id2: " + i);
                        System.out.println("key2: " + k);**/
                        
                        recordHeap.modify(0, buf);
                    }
                    else {
                        recordHeap.removemin(buf);
                        addCount++;
                    } 
                
                } // inBuffer is empty
                
            } // inFile is empty
            // could still be stuff in the heap and outBuffer
            if( !outBuffer.empty() ) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                
                out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
                outBuffer.clear();
            }
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    out.write(outBuffer.array());
                    outBuffer.clear();
                    
                }
                //byte[] rm = new byte[16];
                //rm = recordHeap.removemin();
                //toNumber(rm);
                outBuffer.insert(recordHeap.removemin());
            }
            
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            outBuffer.clear();
            
            long end = outFile.getFilePointer();
            runNode n = new runNode(numRuns, runStart, end);
            runs.add(n);
            
            numRuns++;
            runStart = end;
            recordHeap.buildHeap(addCount);
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    
                    out.write(outBuffer.array());
                    outBuffer.clear();
                }
                outBuffer.insert(recordHeap.removemin());
            }
            
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.array().length));
            outBuffer.clear();
            end = outFile.getFilePointer();
            runNode n2 = new runNode(numRuns, runStart, end);
            runs.add(n2);
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        return key;
    }
    
    /**
     * @param rec1
     * @param rec2
     * @return
     */
    int comparerecordHeap(byte[] rec1, byte[] rec2) {
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
    private FileOutputStream out;
    /**
     * 
     */
    private RandomAccessFile inFile;
    private RandomAccessFile wow;
    /**
     * 
     */
    private RandomAccessFile outFile;

}
