import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
 
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 * Need to place 8 blocks into working memory
 * Take the first record from each block and put into
 *  8 record heap
 * Get the minimum value from the 8 record heap and place
 *  into output buffer
 * If a run is exhausted, then get the next block from the
 *  data file for that run.
 */
public class MultiwayMerge {

    /**
     * Constant number of bytes in a block
     */
    public static final int BLOCK_LENGTH = 8192;
    
    /**
     * Constant number of bytes in a record
     */
    public static final int RECORD_LENGTH = 16;
    
    MultiwayMerge(SortContainer c) throws IOException{
        runs = c.l;
        heap = c.h;
        readFile = c.runs;
        printFile = c.in;
        originalInputFile = c.in;
        c.ib.clear();
        //printFile = new RandomAccessFile("test.bin", "rw");
        numberOfRuns = runs.size();
        outputBuffer = c.ob;
        curRuns = new LinkedList<Integer>();
        readFile.seek(0);
        printFile.seek(0);
        
    }
    
    public void execute() throws IOException {        
        
        if (numberOfRuns == 1) {
            printToStandardOutput(readFile);
            if(printFile != originalInputFile) {
                originalInputFile.seek(0);
                int length;
                while ((length = printFile.read(heap.arr())) > 0){
                    originalInputFile.write(heap.arr(), 0, length);
                }
            }
            //System.out.println();
            //System.out.print(originalInputFile.getFilePointer());
        }
        else {
            int numOfRuns = 8;
            if (numberOfRuns < 8) {
                numOfRuns = numberOfRuns;
            }
            loadBlocks(numOfRuns);
        }
    }
    
    /**
     * Loads the first 8 blocks from output file into working memory
     * @throws IOException
     */
    public void loadBlocks(int numOfRuns) throws IOException {
        pq = new PriorityQueue<BlockNode>(numOfRuns, new BlockNodeComparator());
        for (int i = 0; i < numOfRuns; i++) {
            curRuns.add(i);
            RunNode fileNode = runs.get(i); 
            loadNextBlock(fileNode);
        }
        //makePriorityQueue();
        merge(pq, numOfRuns);
    }
    
    /**
     * @throws IOException
     */
    /*public void makePriorityQueue() throws IOException {
        for (int i = 0; i < 2; i++) { //get first record from each block
            //each run would start from a specified point in heap
            int end = (i + 1) * BLOCK_LENGTH;
            RunNode f = runs.get(i);
            long runLength = f.getEndPos() - f.getCurPos();
            if (runLength < BLOCK_LENGTH) {
                end = (int)((i *  BLOCK_LENGTH) + runLength);
            }
            loadRecordFromHeap(i, BLOCK_LENGTH*i, end);
        }
        // priority queue is now full with the first record from 8 runs
        //merge(pq, numOfRuns);
    }*/
    
    /**
     * @param pq
     * @throws IOException
     */
    public void merge(PriorityQueue<BlockNode> pq, int nRuns) throws IOException {
        long nextStartPos = printFile.getFilePointer();
        outputBuffer.clear();
        int k = 0;
        while (curRuns.size() > 0) {   //all 8 (or however many) runs are exhausted
            BlockNode minNode = pq.poll();
            outputBuffer.write(minNode.getRecord());
            //toNumber(minNode.getRecord());
            //System.out.println();
            if (Arrays.equals(minNode.getRecord(), toByteArray(0, 0))) {
                System.out.print("STOP");
            }
            
            minNode.incrementCurPos(RECORD_LENGTH);
            if (outputBuffer.full()) {
                printFile.write(Arrays.copyOfRange(
                            outputBuffer.array(), 0, outputBuffer.pos()));
                outputBuffer.clear();
            }
            // need to change record in the minNode and increment its current Position
            int blockToRead = minNode.getBlockNumber();
            if (blockToRead == 0) {
                k++;
            }
            int blockSpace = minNode.getEndPos() - minNode.getCurPos(); 
            if (blockSpace < 0) {
                System.out.print("STOP2");
            }
            boolean blockReloaded = false;
            boolean canReadFromRun = true;
            //boolean changeNodeEnd = false;
            //if (minNode.getEndPos() < minNode.getStartPos()) {
            //    System.out.print('d');
            //}
            RunNode fileNode = runs.get(blockToRead); //getting data from disk
            long runLength = fileNode.getEndPos() - fileNode.getCurPos();
            if (blockSpace == 0 && curRuns.contains(blockToRead)) {  
                blockReloaded = true;
                //need to get a new block!
                if (runLength < BLOCK_LENGTH) {
                    minNode.setEndPos((int)((blockToRead *  BLOCK_LENGTH) + runLength));
                }
                if (runLength == 0) {//BLOCK_LENGTH) {
                    // need to remove that run from linked list
                    curRuns.remove((Integer)blockToRead);
                    canReadFromRun = false;
                }
                else {
                    loadNextBlock(fileNode);
                }
            }
            if (canReadFromRun && !blockReloaded) {
                loadRecordFromHeap(blockToRead, 
                    minNode.getCurPos(), minNode.getEndPos());
            }
        }
        for (int i = nRuns-1; i >= 0; i--) {
            runs.remove(i);
        }
        checkAftermath(nextStartPos);
    }
    
    public void checkAftermath(long nextStart) throws IOException {
        // print whats left in the output buffer
        if (!outputBuffer.empty()) {
            printFile.write(Arrays.copyOfRange(
                        outputBuffer.array(), 0, outputBuffer.pos()));
            outputBuffer.clear();
        }
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
            
            /*if(printFile != originalInputFile) {
                originalInputFile.seek(0);
                int length;
                while ((length = printFile.read(heap.arr())) > 0){
                    originalInputFile.write(heap.arr(), 0, length);
                }
            }*/
        }
        else {
            long end = printFile.getFilePointer();
            boolean merged = true;
            RunNode n = new RunNode(runs.size(), nextStart, end, merged);  // add to the end 
            multipleRunsLeft(n);
            
        }   
    }
    
    public void multipleRunsLeft(RunNode n) throws IOException {
        int numberOfRunsLeft = 0;
        Iterator<RunNode> j = runs.iterator(); 
        int i = 0;
        while (j.hasNext()) { 
            RunNode r = j.next();
            if (!r.gotMerged()) {
                r.setRunNumber(i);
                numberOfRunsLeft++;
                i++;
            }
        }
        runs.add(n);
        
        if (numberOfRunsLeft == 0) { // won't go here first time through, because 
                                //of previous runs.size() == 0 check
            // if all nodes are merged, need to set them all to not merged
            Iterator<RunNode> k = runs.iterator(); 
            int count = 0;
            while (k.hasNext()) { 
                RunNode nNode = k.next();
                nNode.setRunNumber(count);
                nNode.setMerged(false);
                count++;
            } 
            numberOfRunsLeft = runs.size();
            //switch input and output
            RandomAccessFile temp = readFile;
            readFile = printFile;
            printFile = temp;
            readFile.seek(0);
            printFile.seek(0);
            this.numberOfRuns = runs.size();
            numberOfRunsLeft = this.numberOfRuns;
            //loadBlocks();
        }
        int numOfRuns = 8;
        if (numberOfRunsLeft < 8) {
            numOfRuns = numberOfRunsLeft;
        }
        loadBlocks(numOfRuns);
    }
    
    /**
     * @param runNum
     * @throws IOException
     */
    //merge nodes correspond to each run that has a block in working memory
    public void loadRecordFromHeap(int runNum, int cur, int end) throws IOException {
        // need to remove that record from the heap
        // get the current location of the record removed from working memory
        // and when you add the next record into the priority queue make sure
        // that the index is incremented so that we read the next record
        // from the corresponding block
        byte[] myRecord = new byte[16];
        try {
            System.arraycopy(heap.arr(), cur, myRecord, 0, 16);
        }
        catch(ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        }
        
        BlockNode mNode = new BlockNode(runNum, myRecord, cur, end);
        pq.add(mNode);
    }
    
    /**
     * @param runNum
     * @throws IOException
     */
    // need to check if you can load the next block
    // need to reset the curPos for the next block that is read in
    public void loadNextBlock(RunNode fileNode) throws IOException {
        long runLength = fileNode.getEndPos() - fileNode.getCurPos();
        long runNum = fileNode.getRunNumber();
        
        if (runLength < BLOCK_LENGTH) {
            // (buffer to read), (position to start reading from), (length read)
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr(), (int)(BLOCK_LENGTH*runNum), (int)runLength);
            fileNode.setCurPos(readFile.getFilePointer());
            int start = (int)(BLOCK_LENGTH*runNum);
            int end = (int)(BLOCK_LENGTH*runNum + runLength);
            loadRecordFromHeap((int)runNum, start, end);
        }
        else {
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr(), (int)(BLOCK_LENGTH*runNum), BLOCK_LENGTH);
            fileNode.setCurPos(readFile.getFilePointer());
            int start = (int)(BLOCK_LENGTH*runNum);
            int end = (int)(BLOCK_LENGTH*runNum + BLOCK_LENGTH);
            loadRecordFromHeap((int)runNum, start, end);
        }
    }
    
    
    /**
     * @param endFile
     * @throws IOException
     */
    public void printToStandardOutput(RandomAccessFile endFile) throws IOException {
        String line;// = null;
        endFile.seek(0);
        int i = 0;
        while (endFile.getFilePointer() != endFile.length()) {
            byte[] b = new byte[16];
            endFile.read(b);
            i++;
            endFile.seek(BLOCK_LENGTH * i);
            toNumber(b);
            if (i % 5 == 0) {
                System.out.println('\n');
            }
            System.out.print(' ');
        }
    }
    
    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.print(id + " " + key);
        return key;
    }
    public byte[] toByteArray(long id, double key) {
        byte[] bytes1 = new byte[8];
        byte[] bytes2 = new byte[8];
        ByteBuffer.wrap(bytes1).putLong(id);
        ByteBuffer.wrap(bytes2).putDouble(key);
        byte[] record = new byte[16];
        System.arraycopy( bytes1, 0, record, 0, 8);
        System.arraycopy( bytes2, 0, record, 8, 8);
        return record;
    }
    
    private PriorityQueue<BlockNode> pq;
    private int numberOfRuns;
    private LinkedList<RunNode> runs;
    private LinkedList<Integer> curRuns;
    private MinHeap heap;
    private RandomAccessFile readFile;
    private RandomAccessFile originalInputFile;
    private RandomAccessFile printFile;
    private Buffer outputBuffer;
}
 