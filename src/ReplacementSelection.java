import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-9
 * 
 * This file performs replacement selection upon the given
 * input RandomAccessFile. It uses an 8-block heap and two 
 * 1-block buffers, one for input and one for output. The 
 * input buffer records are compared to the minimum record
 * from the heap, and either added into the heap (current 
 * run) or stored in the back of the heap array to be processed 
 * in the next run. Data about each run such as start and 
 * end position within the output file is tracked in a 
 * linked list for further sorting in mutiway merge.
 *
 */
public class ReplacementSelection {

    /**
     * @param c the variable containing data 
     * corresponding to sorting
     * @throws IOException 
     */
    ReplacementSelection(SortContainer c) throws IOException {
        // set the private variables to the shared 
        // resources from the SortContainer
        runs = c.getRunsList();
        recordHeap = c.getHeap();
        inFile = c.getInFile();
        outFile = c.getRunsFile();
        inBuffer = c.getInputBuffer();
        outBuffer = c.getOutputBuffer();
        
        // initialize start and end run positions to zero
        runStartPos = 0;
        runEndPos = 0;
    }
   
    /**
     * @return true if the inFile can be read from, false if not
     */
    public boolean canRead() throws IOException {
        // if these are equal, we are at the end of the file
        return inFile.getFilePointer() != inFile.length();
    }
    
    /**
     * Executes the reading of the input file
     * and creates runs of partially sorted data
     * which are written to the outFile and tracked
     * with the linked list
     * @throws IOException 
     */
    public void execute() throws IOException {
        // set the start to the current pos
        runStartPos = outFile.getFilePointer();
        
        // this tracks how many values have been
        // stored in the heap for processing in
        // the next run (needed to heapify)
        int nextRunCount = 0;
    
        while (canRead()) { 
            
            // load the next block of records
            inBuffer.loadBlock(inFile);
            
            while (!inBuffer.doneReading()) {
                // there are still records to process
                
                if (recordHeap.empty()) {
                    // if the heap is empty, that is the end of a run
                    writeBufferToFile();           
                    createRun();
                    
                    // heap array is full of records for next run
                    recordHeap.buildHeap(MAX_REC_HEAP);
                    // reset to zero
                    nextRunCount = 0;
                }
                else if (outBuffer.full()) {
                    writeBufferToFile();
                }
                
                // compare the minimum heap value and the 
                // next value from inBuffer, and apply
                // the replacement selection algorithm
                byte[] minVal = recordHeap.getRecord(0);
                outBuffer.write(minVal);
                byte[] buf = inBuffer.read();
                if (comparerecordHeap(buf, minVal) > 0 ) {
                    // replace the minVal with the record
                    // from the inBuffer and siftdown
                    recordHeap.modify(0, buf);
                }
                else {
                    // remove the minVal you added to output
                    // and store the too small inBuffer value
                    // for processing in the next run
                    recordHeap.removemin(buf);
                    nextRunCount++;
                } 
            } // inBuffer is empty  
        } // inFile is empty
        
        writeBufferToFile();
        
        // clear out the heap to finish the current run, and 
        // if there are values that were too small stored, 
        // create another run for those
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
    
    /**
     * Writes all the records currently in the outBuffer to the
     * outFile
     * @throws IOException
     */
    private void writeBufferToFile() throws IOException {
        // if there are records in the buffer write to file
        // then clear the buffer
        if (!outBuffer.empty()) {
            outFile.write(Arrays.copyOfRange(outBuffer.array(), 
                            0, outBuffer.pos()));
            outBuffer.clear();
        }
    }
    
    /**
     * Creates a runNode and adds it to the 
     * linked list for later processing
     * @throws IOException
     */
    private void createRun() throws IOException {
        // get the current position in outFile
        runEndPos = outFile.getFilePointer();
        
        if (runStartPos != runEndPos) {
            // create a node and add to linked list
            RunNode n = new RunNode(numRuns, runStartPos, runEndPos, false);
            runs.add(n);
            numRuns++;
            // set the start pos for next run
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
        // convert both keys to Doubles and compare
        ByteBuffer buffer1 = ByteBuffer.wrap(
                Arrays.copyOfRange(rec1, RECORD_SIZE / 2, RECORD_SIZE));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(
                Arrays.copyOfRange(rec2, RECORD_SIZE / 2, RECORD_SIZE));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }
    
    /**
     * Named constant for maximum heap size
     */
    private static final int MAX_REC_HEAP = 4096;
    
    /**
     * Number of bytes in a record
     */
    private static final int RECORD_SIZE = 16;

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
