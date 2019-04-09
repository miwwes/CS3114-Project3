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
       // try {
            //out = new FileOutputStream("afterRepSel.bin");
            //out = new FileOutputStream("afterc.bin");
        //} catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        //    e.printStackTrace();
        //}
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
                long before = inFile.getFilePointer();
                inFile.read(inBuffer.array());
                long after = inFile.getFilePointer();
                inBuffer.loadBlock((int)(after - before));
                
                
                while( !inBuffer.doneReading() ) {
                
                    if( recordHeap.empty() ) {
                        outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                        //out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                        outBuffer.clear();
                        
                        long end = outFile.getFilePointer();
                        runNode n = new runNode(numRuns, runStart, end, false);
                        runs.add(n);
                        
                        numRuns++;
                        runStart = end;
                        addCount = 0;
                        recordHeap.buildHeap(MAX_REC_HEAP);
                        
                    }
                    else if ( outBuffer.full() ) {
                        outFile.write(outBuffer.array());
                        //out.write(outBuffer.array());
                        outBuffer.clear();
                    }
                    
                    byte[] minVal = recordHeap.getRecord(0);    //get the minimum
                    outBuffer.write(minVal);
                    byte[] buf = inBuffer.read();
                    if (comparerecordHeap(buf, minVal) > 0 ) {
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
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                //out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    //out.write(outBuffer.array());
                    outBuffer.clear();
                    
                }
                outBuffer.write(recordHeap.removemin());
            }
            
            if( !outBuffer.empty() ) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                //out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            long end = outFile.getFilePointer();
            
            if(runStart != end) {
                runNode n = new runNode(numRuns, runStart, end, false);
                runs.add(n);
                numRuns++;
                runStart = end;
            }
            
            if(addCount > 0) {
                recordHeap.buildHeap(addCount);
            }
            
            while( !recordHeap.empty() ) {
                if ( outBuffer.full() ) {
                    outFile.write(outBuffer.array());
                    //out.write(outBuffer.array());
                    outBuffer.clear();
                }
                outBuffer.write(recordHeap.removemin());
            }
            
            if( !outBuffer.empty() ) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                //out.write(Arrays.copyOfRange(outBuffer.array(), 0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            end = outFile.getFilePointer();
            if(runStart != end) {
                runNode n2 = new runNode(numRuns, runStart, end, false);
                runs.add(n2);     
            }
            inBuffer.clear();
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
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 8, 16));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 8, 16));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }
    
    /**
     * 
     */
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
    // private FileOutputStream out;
    /**
     * 
     */
    private RandomAccessFile inFile;
    /**
     * 
     */
    private RandomAccessFile outFile;

}
