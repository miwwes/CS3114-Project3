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
    
    
    multiwayMerge(LinkedList<runNode> locations, minHeap h, RandomAccessFile file) {
        runLocations = locations;
        heap = h;
        readFile = file;
        numberOfRecords = runLocations.size();
    }
    
    public void readBlocks() throws IOException {
        
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRecords) {
                break;
            }
            readFile.read(heap.arr, blockLength * i, blockLength);
        }
    }
    
    public void makeSmallHeap() {
        byte[] eightHeap = new byte[16*8];
        for (int i = 0; i < 8; i++) {
            if (i == numberOfRecords) {
                break;
            }
            System.arraycopy( heap.arr, blockLength * i, eightHeap, i, 16);
        }
        int numOfNodes = 8;
        if (numberOfRecords < 8) {
            numOfNodes = numberOfRecords;
        }
        minHeap smallHeap = new minHeap(eightHeap, numOfNodes, 8); 
    }
    
    private int numberOfRecords;
    private LinkedList<runNode> runLocations;
    private minHeap heap;
    private RandomAccessFile readFile;

}
