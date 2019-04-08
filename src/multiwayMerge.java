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
    
    multiwayMerge(sortContainer c) throws IOException{
        runs = c.l;
        heap = c.h;
        readFile = c.runs;
        printFile = c.in;
        originalInputFile = c.in;
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
                while ((length = printFile.read(heap.arr)) > 0){
                    originalInputFile.write(heap.arr, 0, length);
                }
            }
            //System.out.println();
            //System.out.print(originalInputFile.getFilePointer());
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
        pq = new PriorityQueue<mergeNode>(numOfRuns, new mergeNodeComparator());
        for (int i = 0; i < numOfRuns; i++) { //get first record from each block
            //each run would start from a specified point in heap
            int end = (i + 1) * blockLength;
            runNode f = runs.get(i);
            long runLength = f.getEndPos() - f.getCurPos();
            if (runLength < blockLength) {
                end = (int)runLength;
            }
            loadRecordFromHeap(i, blockLength*i, end);
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
        outputBuffer.clear();
        int k = 0;
        while (curRuns.size() > 0) {   //all 8 (or however many) runs are exhausted
            mergeNode minNode = pq.poll();
            outputBuffer.insert(minNode.getRecord());
            if (Arrays.equals(minNode.getRecord(), toByteArray(0, 0))) {
                System.out.print("STOP");
            }
            
            minNode.incrementCurPos(recordLength);
            if (outputBuffer.full()) {
                printFile.write(Arrays.copyOfRange(
                            outputBuffer.array(), 0, outputBuffer.pos()));
                outputBuffer.clear();
            }
            // need to change record in the minNode and increment its current Position
            int blockToRead = minNode.getBlockNumber();
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
            runNode fileNode = runs.get(blockToRead); //getting data from disk
            long runLength = fileNode.getEndPos() - fileNode.getCurPos();
            if (blockSpace == 0 && curRuns.contains(blockToRead)) {  
                //need to get a new block!
                
                if (runLength < blockLength) {
                    minNode.setEndPos((int)((blockToRead *  blockLength) + runLength));
                }
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
                minNode.setCurPos(blockToRead * blockLength);
            }
            if (minNode.getEndPos() < minNode.getStartPos()) {
                System.out.print('d');
            }
            if (canReadFromRun) {
                //if (changeNodeEnd) {
                //    minNode.setEndPos((blockToRead + 1) * runLength);
                //}
                loadRecordFromHeap(blockToRead, 
                    minNode.getCurPos(), minNode.getEndPos());
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
        if (!outputBuffer.empty()) {
            printFile.write(Arrays.copyOfRange(
                        outputBuffer.array(), 0, outputBuffer.pos()));
            outputBuffer.clear();
        }
        
        // check if there are still runs within outfile
        if (runs.size() == 0) {
            printToStandardOutput(printFile);
            
            if(printFile != originalInputFile) {
                originalInputFile.seek(0);
                int length;
                while ((length = printFile.read(heap.arr)) > 0){
                    originalInputFile.write(heap.arr, 0, length);
                }
            }
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
    public void loadRecordFromHeap(int runNum, int cur, int end) throws IOException {
        // need to remove that record from the heap
        // get the current location of the record removed from working memory
        // and when you add the next record into the priority queue make sure
        // that the index is incremented so that we read the next record
        // from the corresponding block
        byte[] myRecord = new byte[16];
        try {
            System.arraycopy(heap.arr, cur, myRecord, 0, 16);
        }
        catch(ArrayIndexOutOfBoundsException exception) {
            System.out.println(exception);
        }
        
        mergeNode mNode = new mergeNode(runNum, myRecord, cur, end);
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
        while (endFile.getFilePointer() != endFile.length()) {
            byte[] b = new byte[16];
            endFile.read(b);
            i++;
            endFile.seek(blockLength*i);
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
    
    private PriorityQueue<mergeNode> pq;
    private int numberOfRuns;
    private LinkedList<runNode> runs;
    private LinkedList<Integer> curRuns;
    private minHeap heap;
    private RandomAccessFile readFile;
    private RandomAccessFile originalInputFile;
    private RandomAccessFile printFile;
    private buffer outputBuffer;
}
 