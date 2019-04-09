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
public class ReplacementSelection {

    /**
     * @param c the variable containing data 
     *          corresponding to sorting
     * @throws IOException 
     */
    ReplacementSelection(SortContainer c) throws IOException {
        runs = c.getRunsList();
        recordHeap = c.getHeap();
        inFile = c.getInFile();
        outFile = c.getRunsFile();
        inBuffer = c.getInputBuffer();
        outBuffer = c.getOutputBuffer();
    }
   
    /**
     * @return true if the infile can be read from, false if not
     */
    public boolean canRead() {
        try {
            boolean test = inFile.getFilePointer() != inFile.length();
            return test;
        } 
        catch (IOException e) {
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
        
            while (canRead()) { 
                inBuffer.clear();
                long before = inFile.getFilePointer();
                inFile.read(inBuffer.array());
                long after = inFile.getFilePointer();
                inBuffer.loadBlock((int)(after - before));
                
                
                while (!inBuffer.doneReading()) {
                
                    if (recordHeap.empty()) {
                        outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                                        0, outBuffer.pos()));
                        outBuffer.clear();
                        
                        long end = outFile.getFilePointer();
                        RunNode n = new RunNode(numRuns, runStart, end, false);
                        runs.add(n);
                        
                        numRuns++;
                        runStart = end;
                        addCount = 0;
                        recordHeap.buildHeap(MAX_REC_HEAP);
                        
                    }
                    else if (outBuffer.full()) {
                        outFile.write(outBuffer.array());
                        outBuffer.clear();
                    }
                    
                    byte[] minVal = recordHeap.getRecord(0);
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
            if (!outBuffer.empty()) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                                0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            while (!recordHeap.empty()) {
                if (outBuffer.full()) {
                    outFile.write(outBuffer.array());
                    outBuffer.clear();
                    
                }
                outBuffer.write(recordHeap.removemin());
            }
            
            if (!outBuffer.empty()) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                                0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            long end = outFile.getFilePointer();
            
            if (runStart != end) {
                RunNode n = new RunNode(numRuns, runStart, end, false);
                runs.add(n);
                numRuns++;
                runStart = end;
            }
            
            if (addCount > 0) {
                recordHeap.buildHeap(addCount);
            }
            
            while (!recordHeap.empty()) {
                if (outBuffer.full()) {
                    outFile.write(outBuffer.array());
                    outBuffer.clear();
                }
                outBuffer.write(recordHeap.removemin());
            }
            
            if (!outBuffer.empty()) {
                outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                                0, outBuffer.pos()));
                outBuffer.clear();
            }
            
            end = outFile.getFilePointer();
            if (runStart != end) {
                RunNode n2 = new RunNode(numRuns, runStart, end, false);
                runs.add(n2);     
            }
            inBuffer.clear();
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    /**
     * Compares two records
     * @param rec1 the first record to compare
     * @param rec2 the second record to compare
     * @return the comparison value
     */
    int comparerecordHeap(byte[] rec1, byte[] rec2) {
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 8, 16));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 8, 16));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }
    
    /**
     * Named constant for maximum heap size
     */
    private static final int MAX_REC_HEAP = 4096;

    /**
     * Private variable for the 8 block heap
     */
    private MinHeap recordHeap;
    
    /**
     * Private variable holding the runs going into the outfile
     */
    private LinkedList<RunNode> runs;
    /**
     * Input buffer to aid in replacement selection
     */
    private Buffer inBuffer;
    /**
     * Output buffer to aid in replacement selection
     */
    private Buffer outBuffer;
    /**
     * File from which the raw data is being read from
     */
    private RandomAccessFile inFile;
    /**
     * File into which the original runs are put
     */
    private RandomAccessFile outFile;

}
