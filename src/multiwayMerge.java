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
    
    
    multiwayMerge(LinkedList<runNode> locations, 
                    minHeap h, RandomAccessFile file, buffer outBuf) {
        this.runLocations = locations;
        this.heap = h;
        this.readFile = file;
        this.numberOfRecords = runLocations.size();
        this.outputBuffer = outBuf;
    }
    
    public void readBlocks() throws IOException {
        
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRecords) {
                break;
            }
            readFile.read(heap.arr, blockLength * i, blockLength);
        }
    }
    
    public void makePriorityQueue() {
        PriorityQueue<mergeNode> pq = 
            new PriorityQueue<mergeNode>(8, new mergeNodeComparator());
            
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRecords) {
                break;
            }
            byte[] myNode = new byte[16];
            System.arraycopy( heap.arr, blockLength * i, myNode, 0, 16);
            mergeNode mNode = new mergeNode(i, myNode);
            pq.add(mNode);
        }
        
        while (heap.heapSize() > 0) {
            //do stuff
        }
        mergeNode minNode = pq.poll();
        outputBuffer.insert(minNode.getRecord());
        int nextBlock = minNode.getBlockNumber();
        
        //int numOfNodes = 8;
        //if (numberOfRecords < 8) {
        //    numOfNodes = numberOfRecords;
        //}
        //minHeap smallHeap = new minHeap(eightHeap, numOfNodes, 8); 
        //addToOutputBuffer(smallHeap);
    }
    
    public void writeToNextFile(PriorityQueue<mergeNode> pq) {
        
    }
    
    private int numberOfRecords;
    private LinkedList<runNode> runLocations;
    private minHeap heap;
    private RandomAccessFile readFile;
    private buffer outputBuffer;

}
