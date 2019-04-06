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
 * 
 * Take the first record from each block and put into
 *  8 record heap
 * 
 * Get the minimum value from the 8 record heap and place
 *  into output buffer
 * 
 * If a run is exhausted, then get the next block from the
 *  data file for that run.
 *
 */
public class multiwayMerge {
    
    static int blockLength = 8192;
    static int recordLength = 16;
    
    
    //multiwayMerge(LinkedList<runNode> locations, minHeap h, 
    //                RandomAccessFile outFile, RandomAccessFile inFile, buffer outBuf) {
    multiwayMerge(sortContainer c){
        this.runs = c.l;
        this.heap = c.h;
        this.readFile = c.runs;
        this.printFile = c.in;
        this.numberOfRuns = runs.size();
        this.outputBuffer = c.ob;
        this.heapLength = 0;
    }
    
    public void execute() throws IOException {
        
        if (numberOfRuns == 1) {
            printToStandardOutput(readFile);
        }
        else {
            loadBlocks();
        }
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
            loadNextBlock(i);
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
        for (int i = 0; i < numOfRuns; i++) {
            //need to get first record from each block in heap
            //each run would start from a specified point in heap
            loadNextNode(i, 0);
        }
        // priority queue is now full with the first record from 8 runs
        merge(pq);
    }
    
    /**
     * @param pq
     * @throws IOException
     */
    public void merge(PriorityQueue<mergeNode> pq) throws IOException {
        printFile.seek(0);
        long runStart = printFile.getFilePointer();
        // stop while loop when you cannot read any more from the file
        while (heapLength > 0) {   //all 8 (or however many) runs are exhausted
            System.out.println(pq.peek().key());
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
            if (outputBuffer.full()) {
                printFile.write(outputBuffer.array());
                outputBuffer.clear();
            }
            // need to change record in the minNode and increment its current Position
            int nextBlock = minNode.getBlockNumber();
            if (nextBlock == 1) {
                System.out.println("1");
            }
            boolean canLoadNode = true;
            if (minNode.getCurPos() == minNode.getEndPos()) {
                canLoadNode = loadNextBlock(nextBlock);
            }
            // get the current location of the record removed from working memory
            // and when you add the next record into the priority queue make sure
            // that the index is incremented so that we read the next record
            // from the corresponding block
            if (canLoadNode) {
                loadNextNode(nextBlock, minNode.getCurPos());
            }
        }
        System.out.println("HERE");
        // print whats left in the output buffer
        if (!outputBuffer.empty()) {
            printFile.write(outputBuffer.array());
            outputBuffer.clear();
        }
        Iterator<mergeNode> i = pq.iterator(); 
        while (i.hasNext()) { 
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
        } 
        printFile.write(outputBuffer.array());
        outputBuffer.clear();
        
        int numberOfRunsLeft = runs.size();
        // if heap is empty, then up to 8 runs are exhausted.
        long end = printFile.getFilePointer();
        runNode n = new runNode(numberOfRunsLeft, runStart, end);   
        //I guess add newly created run to end of linked list
        runs.add(n);
        // check if there are still runs within outfile
        if (runs.size() == 0) {
            printToStandardOutput(printFile);
        }
        else {
            heapLength = 0;
            //switch input and output
            RandomAccessFile temp = readFile;
            readFile = printFile;
            printFile = temp;
            this.numberOfRuns = runs.size();
            loadBlocks();
        }
    }
    
    /**
     * @param block
     * @throws IOException
     */
    public void loadNextNode(int block, int cur) throws IOException {
        // need to remove that record from the heap
        byte[] myRecord = new byte[16];
        byte[] myRecordt = new byte[16];
        int nextNodeLocation = (blockLength * block) + (cur);
        int c = (blockLength * block) + (cur * 10);
        System.arraycopy(heap.arr, c, myRecord, 0, 16);        
        
        toNumber(myRecord);
        System.arraycopy(heap.arr, nextNodeLocation, myRecord, 0, 16);
        String v = Arrays.toString(Arrays.copyOfRange(heap.arr, 0, 16));
        System.out.println(v);
        System.arraycopy(heap.arr, nextNodeLocation + 16, myRecordt, 0, 16);
        String d = Arrays.toString(Arrays.copyOfRange(heap.arr, 16, 32));
        System.out.println(d);
        //System.out.println("loc: " + nextNodeLocation);
        toNumber(myRecord);
        toNumber(myRecordt);
        heapLength -= 16;
        mergeNode mNode = new mergeNode(block, myRecord, cur + recordLength);
        pq.add(mNode);
    }
    
    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        System.out.println("id bytes" + idBytes);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        System.out.println("key bytes" + keyBytes);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        return key;
    }
    
    /**
     * @param block
     * @throws IOException
     */
    public boolean loadNextBlock(int block) throws IOException {
        // need to check if you can load the next block
        // need to reset the curPos for the next block that is read in
        runNode node = runs.get(block);
        long runLength = node.getEndPos() - node.getStartPos();
        if (runLength == 0) {
            // need to remove that run from linked list
            runs.remove(node);
            return false;
        }
        else {
            if (runLength < blockLength) {
                heapLength += runLength;
                // (buffer to read), (position to start reading from), (length read)
                readFile.seek(node.getStartPos());
                readFile.read(heap.arr, (int)(blockLength*runLength), (int)runLength);
            }
            else {
                heapLength += blockLength;
                //long fp = readFile.getFilePointer();
                readFile.seek(node.getStartPos());
                readFile.read(heap.arr, blockLength*block, blockLength);
            }
            return true;
        }
    }
    
    public void writeToNextFile(PriorityQueue<mergeNode> pq) {
        
    }
    
    /**
     * @param endFile
     * @throws IOException
     */
    public void printToStandardOutput(RandomAccessFile endFile) throws IOException {
        String line = null;
        while ((line = endFile.readLine()) != null) {
            System.out.println(line);
        }
    }
    
    private int heapLength;
    private PriorityQueue<mergeNode> pq;
    private int numberOfRuns;
    private LinkedList<runNode> runs;
    private minHeap heap;
    private RandomAccessFile readFile;
    private RandomAccessFile printFile;
    private buffer outputBuffer;

}
