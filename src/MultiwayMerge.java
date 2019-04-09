import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
 
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 *  Takes a random access file holding sorted runs
 *      and uses the working memory to sort all of 
 *      the runs into one sorted run. Blocks of 
 *      length 8192 bytes are taken from the file
 *      and placed into working memory where one record
 *      at a time is taken from each block and
 *      sorted in a priority queue. The resulting run(s)
 *      are printed into the input file. If there
 *      is more than one resulting run from the first
 *      multiway merge pass, then the process begins 
 *      again with using the output file as the end file.
 */
public class MultiwayMerge {
    
    /**
     * This class performs an R-way merge
     */
    public static final int R = 8;

    /**
     * Constant number of bytes in a block
     */
    public static final int BLOCK_LENGTH = 8192;
    
    /**
     * Constant number of bytes in a record
     */
    public static final int RECORD_LENGTH = 16;
    
    /**
     * Constructor for the merge class
     * @param c the container for all shared objects 
     * between merge and replacement selection
     * @throws IOException
     */
    MultiwayMerge(SortContainer c) throws IOException {
        runs = c.runsList();
        heap = c.heap();
        readFile = c.runsFile();
        printFile = c.inFile();
        originalInputFile = c.inFile();
        c.inputBuffer().clear();
        numberOfRuns = runs.size();
        outputBuffer = c.outputBuffer();
        curRuns = new LinkedList<Integer>();
        readFile.seek(0);
        printFile.seek(0);   
    }

    /**
     * Primary method for performing the merge operation
     * @throws IOException
     */
    public void execute() throws IOException {        
        // if there is already one run, then just 
            // go straight to printing the output
        if (numberOfRuns == 1) {
            printToStandardOutput(readFile);
        }
        else {
            int numOfRuns = R;
            if (numberOfRuns < R) {
                numOfRuns = numberOfRuns;
            }
            loadBlocks(numOfRuns);
        }
    }
    
    /**
     * Loads the first R blocks from output file into working memory
     * @param numOfRuns the number of runs for which to load blocks
     * @throws IOException
     */
    private void loadBlocks(int numOfRuns) throws IOException {
        // create a priority queue that will hold the first record
        //      from each of the blocks in working memory
        pq = new PriorityQueue<BlockNode>(numOfRuns, new BlockNodeComparator());
        for (int i = 0; i < numOfRuns; i++) {
            // curRuns holds the integer value of the runs
            //      so that it is known when runs are exhausted
            //      when the elements have all been removed
            curRuns.add(i);
            RunNode fileNode = runs.get(i); 
            loadNextBlock(fileNode);
        }
        merge(numOfRuns);
    }
    
    /**
     * Merges multiple blocks together in the working memory
     * @param nRuns the number of runs for which to merge
     * @throws IOException
     */
    private void merge(int nRuns) throws IOException {
        // save the file pointer for the newly created
        //      run as a result of the multiway merge
        long nextStartPos = printFile.getFilePointer();
        outputBuffer.clear();
        
        while (curRuns.size() > 0) {   //all runs are exhausted
            // get the lowest value from the priority queue
            BlockNode minNode = pq.poll();
            outputBuffer.write(minNode.getRecord());
            // increment the place to read from in the block
            minNode.incrementCurPos(RECORD_LENGTH);
            if (outputBuffer.full()) {
                printFile.write(Arrays.copyOfRange(
                            outputBuffer.array(), 0, outputBuffer.pos()));
                outputBuffer.clear();
            }
            
            // change minNode record and increment its current Position
            int blockToRead = minNode.getBlockNumber();
            int blockSpace = minNode.getEndPos() - minNode.getCurPos(); 
            boolean blockReloaded = false;
            boolean canReadFromRun = true;
            RunNode fileNode = runs.get(blockToRead); //getting data from disk
            long runLength = fileNode.getEndPos() - fileNode.getCurPos();
            // if we ran out of block space and we can still read from
            //      the block
            if (blockSpace == 0 && curRuns.contains(blockToRead)) {  
                blockReloaded = true;
                // need to get a new block!
                if (runLength < BLOCK_LENGTH) {
                    long end = (blockToRead * BLOCK_LENGTH) + runLength;
                    minNode.setEndPos((int)end);
                }
                if (runLength == 0) { 
                    // need to remove that run from linked list
                    //      if the run is exhausted on disk
                    curRuns.remove((Integer)blockToRead);
                    canReadFromRun = false;
                }
                else {
                    loadNextBlock(fileNode);
                }
            }
            //  load the next record from the block if the run
            //      is not exhausted and a block was not just
            //      reloaded
            if (canReadFromRun && !blockReloaded) {
                loadRecordFromHeap(blockToRead, 
                    minNode.getCurPos(), minNode.getEndPos());
            }
        }
        //  remove the merged runs from the runs linked list
        for (int i = nRuns - 1; i >= 0; i--) {
            runs.remove(i);
        }
        checkIfFinished(nextStartPos);
    }
    
    /**
     * Last steps in the merge
     * @param nextStart the starting position of the newly
     *          created merged run
     * @throws IOException
     */
    private void checkIfFinished(long nextStart) throws IOException {
        // print what is left in the output buffer
        if (!outputBuffer.empty()) {
            printFile.write(Arrays.copyOfRange(
                        outputBuffer.array(), 0, outputBuffer.pos()));
            outputBuffer.clear();
        }
        // put all the leftover priority queue values into buffer
        Iterator<BlockNode> i = pq.iterator(); 
        while (i.hasNext()) { 
            BlockNode minNode = pq.poll();
            outputBuffer.write(minNode.getRecord());
        } 
        if (!outputBuffer.empty()) {
            printFile.write(Arrays.copyOfRange(
                        outputBuffer.array(), 0, outputBuffer.pos()));
            outputBuffer.clear();
        }
        
        // check if there are still runs within outfile
        if (runs.size() == 0) {
            printToStandardOutput(printFile);
        }
        else {
            // create the resulting run node from multiway merge
            long end = printFile.getFilePointer();
            boolean merged = true;
            RunNode n = new RunNode(runs.size(), 
                            nextStart, end, merged); 
            multipleRunsLeft(n);
            
        }   
    }
    
    /**
     * @param n the newly created merged node
     * @throws IOException
     */
    private void multipleRunsLeft(RunNode n) throws IOException {
        int numberOfRunsLeft = 0;
        Iterator<RunNode> j = runs.iterator(); 
        int i = 0;
        while (j.hasNext()) { 
            RunNode r = j.next();
            // the runs left to merge are the ones that have
            //      not been merged yet
            if (!r.gotMerged()) {
                r.setRunNumber(i);
                numberOfRunsLeft++;
                i++;
            }
        }
        runs.add(n);
        // won't go here first time through, because 
        // of previous runs.size() == 0 check
        if (numberOfRunsLeft == 0) {             
            // if all nodes are merged, need to set them all to not merged
            Iterator<RunNode> k = runs.iterator(); 
            int count = 0;
            // set the nodes to be "not merged" so that the entire
            //      process can be repeated like it is a brand
            //      new slate
            while (k.hasNext()) { 
                RunNode nNode = k.next();
                // need to reset the run number 
                nNode.setRunNumber(count);
                nNode.setMerged(false);
                count++;
            } 
            numberOfRunsLeft = runs.size();
            //switch input and output files
            RandomAccessFile temp = readFile;
            readFile = printFile;
            printFile = temp;
            readFile.seek(0);
            printFile.seek(0);
            this.numberOfRuns = runs.size();
            numberOfRunsLeft = this.numberOfRuns;
        }
        int numOfRuns = R;
        if (numberOfRunsLeft < R) {
            numOfRuns = numberOfRunsLeft;
        }
        // load only up to R blocks
        loadBlocks(numOfRuns);
    }
    
    /**
     * Load a record from the corresponding place in working memory
     * @param runNum the run number to load from
     * @param cur the current position within the block
     * @param end the end position of the data in the block
     * @throws IOException
     */
    private void loadRecordFromHeap(int runNum, int cur, int end) 
            throws IOException {
        // have current location of the record read from working memory
        // and when you add the next record into the priority queue make sure
        // that the index is incremented so that we read the next record
        // from the corresponding block
        byte[] myRecord = new byte[16];
        try {
            System.arraycopy(heap.arr(), cur, myRecord, 0, 16);
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        }
        
        BlockNode mNode = new BlockNode(runNum, myRecord, cur, end);
        pq.add(mNode);
    }
    
    /**
     * Load the next block from disk into the working memory
     * @param fileNode the node containing the run data
     * @throws IOException
     */
    private void loadNextBlock(RunNode fileNode) throws IOException {
        // need to check if you can load the next block
        // need to reset the curPos for the next block that is read in
        long runLength = fileNode.getEndPos() - fileNode.getCurPos();
        long runNum = fileNode.getRunNumber();
        
        if (runLength < BLOCK_LENGTH) {
            // if the run length is less than block length only
            //      read in the run length
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr(), (int)(BLOCK_LENGTH * runNum), 
                            (int)runLength);
            fileNode.setCurPos(readFile.getFilePointer());
            int start = (int)(BLOCK_LENGTH * runNum);
            int end = (int)(BLOCK_LENGTH * runNum + runLength);
            // load the first record from the newly loaded block
            loadRecordFromHeap((int)runNum, start, end);
        }
        else {
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr(), (int)(BLOCK_LENGTH * runNum), 
                            BLOCK_LENGTH);
            fileNode.setCurPos(readFile.getFilePointer());
            int start = (int)(BLOCK_LENGTH * runNum);
            int end = (int)(BLOCK_LENGTH * runNum + BLOCK_LENGTH);
            loadRecordFromHeap((int)runNum, start, end);
        }
    }
    
    
    /**
     * @param endFile the sorted data will be printed
     * @throws IOException
     */
    private void printToStandardOutput(RandomAccessFile endFile) 
            throws IOException {
        endFile.seek(0);
        int i = 0;
        while (endFile.getFilePointer() != endFile.length()) {
            byte[] b = new byte[16];
            endFile.read(b);
            i++;
            // print the first record of every block
            endFile.seek(BLOCK_LENGTH * i);
            toNumber(b);
            // for every five records, print an endline
            if (i % 5 == 0) {
                System.out.println();
            }
            else {
                System.out.print(' ');
            }
        }
    }
    
    /**
     * Helper function to convert bytes into ID and key
     * @param bytes the byte array to convert
     * @return the key value
     */
    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 
                            RECORD_LENGTH / 2);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 
                            RECORD_LENGTH / 2, RECORD_LENGTH);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.print(id + " " + key);
        return key;
    }
    
    /**
     * priority queue used to sort up to R records at a time
     */
    protected PriorityQueue<BlockNode> pq;
    /**
     * number of runs left to merge
     */
    protected int numberOfRuns;

    /**
     * Linked list storing run information
     */
    protected LinkedList<RunNode> runs;
    /**
     * Linked list storing current run information
     */

    protected LinkedList<Integer> curRuns;
    /**
     * The heap which contains the R block array
     */
    protected MinHeap heap;
    /**
     * file runs are read from
     */
    protected RandomAccessFile readFile;
    /**
     * reference to original input file
     */
    protected RandomAccessFile originalInputFile;
    /**
     * The file results are printed to
     */
    protected RandomAccessFile printFile;
    /**
     * The output 1 block byte array
     */
    protected Buffer outputBuffer;
}
 