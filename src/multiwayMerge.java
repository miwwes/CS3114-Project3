import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
 
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 */
/** 
 * Need to place 8 blocks into working memory
 * Take the first record from each block and put into
 *  8 record heap
 * Get the minimum value from the 8 record heap and place
 *  into output buffer
 * If a run is exhausted, then get the next block from the
 *  data file for that run.
 */
public class multiwayMerge {
    
    static int blockLength = 8192;
    static int recordLength = 16;
    
    
    //multiwayMerge(LinkedList<runNode> locations, minHeap h, 
    //                RandomAccessFile outFile, RandomAccessFile inFile, buffer outBuf) {
    multiwayMerge(sortContainer c) throws FileNotFoundException{
        this.runs = c.l;
        this.heap = c.h;
        this.readFile = c.runs;
        //this.printFile = c.in;
        this.printFile = new RandomAccessFile("test.bin", "rw");
        this.numberOfRuns = runs.size();
        this.outputBuffer = c.ob;
        //this.heapLength = 0;
        this.curRuns = new LinkedList<Integer>();
    }
    
    public void execute() throws IOException {
        readFile.seek(0);
        byte[] rec = new byte[16];
        readFile.read(rec);
        System.out.println("  ");
        toNumber(rec);
        byte[] rec1 = new byte[16];
        readFile.read(rec1);
        System.out.println("  ");
        toNumber(rec1);
        byte[] rec2 = new byte[16];
        readFile.read(rec2);
        System.out.println("  ");
        toNumber(rec2);
        
        //if (numberOfRuns == 1) {
        //    printToStandardOutput(readFile);
        //}
        //else {
        //    loadBlocks();
        //}
    }
    
    /**
     * Loads the first 8 blocks from output file into working memory
     * @throws IOException
     */
    public void loadBlocks() throws IOException {
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRuns) {
                break;
            }
            curRuns.add(i);
            runNode fileNode = runs.get(i); 
            loadNextBlock(fileNode);
        }
        makePriorityQueue();
    }
    
    /**
     * @throws IOException
     */
    public void makePriorityQueue() throws IOException {
        int numOfRuns = 8;
        if (numberOfRuns < 8) {
            numOfRuns = numberOfRuns;
        }
        this.pq = new PriorityQueue<mergeNode>(numOfRuns, new mergeNodeComparator());
        for (int i = 0; i < numOfRuns; i++) { //get first record from each block
            //each run would start from a specified point in heap
            loadRecordFromHeap(i, blockLength*i);
        }
        // priority queue is now full with the first record from 8 runs
        merge(pq, numOfRuns);
    }
    
    /**
     * @param pq
     * @throws IOException
     */
    public void merge(PriorityQueue<mergeNode> pq, int nRuns) throws IOException {
        printFile.seek(0);
        while (curRuns.size() > 0) {   //all 8 (or however many) runs are exhausted
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
            minNode.incrementCurPos(recordLength);
            if (outputBuffer.full()) {
                printFile.write(Arrays.copyOfRange(
                            outputBuffer.array(), 0, outputBuffer.pos()));
                outputBuffer.clear();
            }
            // need to change record in the minNode and increment its current Position
            int blockToRead = minNode.getBlockNumber();
            int blockSpace = minNode.getEndPos() - minNode.getCurPos(); 
            boolean blockReloaded = false;
            boolean canReadFromRun = true;
            
            if (blockSpace == 0 && curRuns.contains(blockToRead)) {  
                //need to get a new block!
                runNode fileNode = runs.get(blockToRead); //getting data from disk
                long runLength = fileNode.getEndPos() - fileNode.getCurPos();
                blockReloaded = true;
                if (runLength == 0) {//blockLength) {
                    // need to remove that run from linked list
                    curRuns.remove((Integer)blockToRead);
                    canReadFromRun = false;
                }
                else {
                    loadNextBlock(fileNode);
                }
            }
            if (blockReloaded) {
                minNode.setCurPos(0);
            }
            if (canReadFromRun) {
                loadRecordFromHeap(blockToRead, minNode.getCurPos());
            }
        }
        for (int i = nRuns-1; i >= 0; i--) {
            runs.remove(i);
        }
        checkAftermath();
    }
    
    public void checkAftermath() throws IOException {
        // print whats left in the output buffer
        if (!outputBuffer.empty()) {
            printFile.write(Arrays.copyOfRange(
                        outputBuffer.array(), 0, outputBuffer.pos()));
            outputBuffer.clear();
        }
        Iterator<mergeNode> i = pq.iterator(); 
        while (i.hasNext()) { 
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
        } 
        printFile.write(outputBuffer.array());
        outputBuffer.clear();
        
        // check if there are still runs within outfile
        if (runs.size() == 0) {
            printToStandardOutput(printFile);
        }
        else {
            int numberOfRunsLeft = runs.size();
            // if heap is empty, then up to 8 runs are exhausted.
            long end = printFile.getFilePointer();
            runNode n = new runNode(numberOfRunsLeft, 0, end);   
            runs.add(n);
            //switch input and output
            RandomAccessFile temp = readFile;
            readFile = printFile;
            printFile = temp;
            this.numberOfRuns = runs.size();
            loadBlocks();
        }
        
    }
    
    /**
     * @param runNum
     * @throws IOException
     */
    //merge nodes correspond to each run that has a block in working memory
    public void loadRecordFromHeap(int runNum, int cur) throws IOException {
        // need to remove that record from the heap
        // get the current location of the record removed from working memory
        // and when you add the next record into the priority queue make sure
        // that the index is incremented so that we read the next record
        // from the corresponding block
        byte[] myRecord = new byte[16];
        System.arraycopy(heap.arr, cur, myRecord, 0, 16);
        mergeNode mNode = new mergeNode(runNum, myRecord, cur);
        pq.add(mNode);
    }
    
    /**
     * @param runNum
     * @throws IOException
     */
    // need to check if you can load the next block
    // need to reset the curPos for the next block that is read in
    public void loadNextBlock(runNode fileNode) throws IOException {
        long runLength = fileNode.getEndPos() - fileNode.getCurPos();
        long runNum = fileNode.getRunNumber();
        
        if (runLength < blockLength) {
            // (buffer to read), (position to start reading from), (length read)
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr, (int)(blockLength*runNum), (int)runLength);
            fileNode.setCurPos(readFile.getFilePointer());
        }
        else {
            readFile.seek(fileNode.getCurPos());
            readFile.read(heap.arr, (int)(blockLength*runNum), blockLength);
            fileNode.setCurPos(readFile.getFilePointer());
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
        while ((line = endFile.readLine()) != null) {
            byte[] rec = new byte[16];
            endFile.read(rec);
            i++;
            endFile.seek(blockLength*i);
            toNumber(rec);
            if (i % 5 == 0) {
                System.out.println('\n');
            }
            System.out.println(' ');
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
    
    private PriorityQueue<mergeNode> pq;
    private int numberOfRuns;
    private LinkedList<runNode> runs;
    private LinkedList<Integer> curRuns;
    private minHeap heap;
    private RandomAccessFile readFile;
    private RandomAccessFile printFile;
    private buffer outputBuffer;
}
 