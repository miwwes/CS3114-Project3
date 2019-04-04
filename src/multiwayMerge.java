import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    
    
    multiwayMerge(LinkedList<runNode> locations, minHeap h, 
                    RandomAccessFile outFile, RandomAccessFile inFile, buffer outBuf) {
        this.runs = locations;
        this.heap = h;
        this.readFile = outFile;
        this.printFile = inFile;
        this.numberOfRecords = runs.size();
        this.outputBuffer = outBuf;
    }
    
    /**
     * Loads the first 8 blocks from output file into working memory
     * @throws IOException
     */
    public void loadOriginalBlocks() throws IOException {
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRecords) {
                break;
            }
            loadNextBlock(i);
        }
    }
    
    /**
     * @throws IOException
     */
    public void makePriorityQueue() throws IOException {
        int numOfRuns = 8;
        if (runs.size() < 8) {
            numOfRuns = runs.size();
        }
        this.pq = new PriorityQueue<mergeNode>(numOfRuns, new mergeNodeComparator());
        for (int i = 0; i < numOfRuns; i++) {
            //need to get first record from each block in heap
            //each run whould start from a specified point in heap
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
        long runStart = printFile.getFilePointer();
        // stop while loop when you cannot read any more from the file
        while (heap.arr.length == 0) {
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
            if ( outputBuffer.full() ) {
                printFile.write(outputBuffer.array());
                outputBuffer.clear();
            }
            // need to change record in the minNode and increment its current Position
            int nextBlock = minNode.getBlockNumber();
            if (minNode.getCurPos() == minNode.getEndPos()) {
                loadNextBlock(nextBlock);
            }
            // get the current location of the record removed from working memory
            // and when you ad the next record into the priority queue make sure
            // that the index is incremented so that we read the next record
            // from the corresponding block
            loadNextNode(nextBlock, minNode.getCurPos());
        }
        long end = printFile.getFilePointer();
        runNode n = new runNode(1, runStart, end);
        runs.push(n);
    }
    
    /**
     * @param block
     * @throws IOException
     */
    public void loadNextNode(int block, int cur) throws IOException {
        // need to remove that record from the heap
        byte[] myRecord = new byte[16];
        System.arraycopy( heap.arr, blockLength * block, myRecord, 0, 16);
        mergeNode mNode = new mergeNode(block, myRecord, cur + recordLength);
        pq.add(mNode);
    }
    
    /**
     * @param block
     * @throws IOException
     */
    public void loadNextBlock(int block) throws IOException {
        // need to reset the curPos for the next block that is read in
        runNode node = runs.get(block);
        long runLength = node.getEndPos() - node.getStartPos();
        if (runLength < blockLength) {
            // (buffer to read), (position to start reading from), (length read)
            readFile.read(heap.arr, (int)node.getStartPos(), (int)runLength);
        }
        else {
            readFile.read(heap.arr, (int)node.getStartPos(), blockLength);
        }
    }
    
    public void writeToNextFile(PriorityQueue<mergeNode> pq) {
        
    }
    
    private PriorityQueue<mergeNode> pq;
    private int numberOfRecords;
    private LinkedList<runNode> runs;
    private minHeap heap;
    private RandomAccessFile readFile;
    private RandomAccessFile printFile;
    private buffer outputBuffer;

}
