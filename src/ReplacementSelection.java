import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;


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
        runStartPos = 0;
        runEndPos = 0;
    }
   
    /**
     * @return true if the inFile can be read from, false if not
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
     * @throws IOException 
     */
    public void execute() throws IOException {
        // initialize helper variables
        runStartPos = outFile.getFilePointer();
        int nextRunCount = 0;
    
        while (canRead()) { 
            
            inBuffer.loadBlock(inFile);
            
            while (!inBuffer.doneReading()) {
            
                if (recordHeap.empty()) {
                    writeBufferToFile();           
                    createRun();
                    nextRunCount = 0;
                    recordHeap.buildHeap(MAX_REC_HEAP);
                    
                }
                else if (outBuffer.full()) {
                    writeBufferToFile();
                }
                
                byte[] minVal = recordHeap.getRecord(0);
                outBuffer.write(minVal);
                byte[] buf = inBuffer.read();
                if (comparerecordHeap(buf, minVal) > 0 ) {
                    recordHeap.modify(0, buf);
                }
                else {
                    recordHeap.removemin(buf);
                    nextRunCount++;
                } 
            
            } // inBuffer is empty
            
        } // inFile is empty
        
        writeBufferToFile();
        
        for (int i = 0; i <= nextRunCount; i += nextRunCount) {
            while (!recordHeap.empty()) {
                if (outBuffer.full()) {
                    writeBufferToFile();
                }
                outBuffer.write(recordHeap.removemin());
            }
            
            writeBufferToFile();
            createRun();
            
            if (nextRunCount == 0) {
                i++;
            }
            else {
                recordHeap.buildHeap(nextRunCount);
            }
        }
        inBuffer.clear();
    }
    
    private void writeBufferToFile() throws IOException {
        if (!outBuffer.empty()) {
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                            0, outBuffer.pos()));
            outBuffer.clear();
        }
    }
    
    private void createRun() throws IOException {
        
        runEndPos = outFile.getFilePointer();
        
        if (runStartPos != runEndPos) {
            RunNode n = new RunNode(numRuns, runStartPos, runEndPos, false);
            runs.add(n);
            numRuns++;
            runStartPos = runEndPos;
        }
    }
    
    /**
     * Compares two records based on key value
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
     * Private variable holding the runs going into the output
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

    /**
     * Starting position of current run
     */
    private long runStartPos;
    
    /**
     * Ending position of current run
     */
    private long runEndPos;
    
    /**
     * The total number of runs created
     */
    private int numRuns;
}
