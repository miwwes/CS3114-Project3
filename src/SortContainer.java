import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author abbym1
 * @author juliam8
 * @version 4-4-19
 * 
 * ***Description***
 *
 */
public class SortContainer {
    
    
    /**
     * @param s the name of the input bin file
     * @throws IOException 
     */
    SortContainer(String s) throws IOException{
        ib = new Buffer();
        ob = new Buffer();
        l = new LinkedList<RunNode>();
        
        in = new RandomAccessFile(s, "rw");
        runs = new RandomAccessFile("runfile.bin", "rw");
        runs.setLength(HEAP_SIZE);
        in.seek(0);
        runs.seek(0);
        
        byte[] heapArray = new byte[HEAP_SIZE];
        in.read(heapArray);
        h = new MinHeap(heapArray, MAX_REC_HEAP, MAX_REC_HEAP);
        h.buildHeap();
    }
    
    
    /**
     * @param input the random access file to set as the input file
     */
    public void setInFile(RandomAccessFile input) {
        in = input;
    }
    
    
    /**
     * @return the input random access file
     */
    public RandomAccessFile getInFile() {
        return in;
    }
    
    
    /**
     * @param output the random access file to set as output file
     */
    public void setRunsFile(RandomAccessFile output) {
        runs = output;
    }
    
    
    /**
     * @return the output random access file
     */
    public RandomAccessFile getRunsFile() {
        return runs;
    }
    
    
    /**
     * @param heap the working memory to set
     */
    public void setHeap(MinHeap heap) {
        h = heap;
    }
    
    
    /**
     * @return the working memory heap
     */
    public MinHeap getHeap() {
        return h;
    }
    
    
    /**
     * @param list the runs list to set for this container
     */
    public void setRunsList(LinkedList<RunNode> list) {
        l = list;
    }
    
    
    /**
     * @return the list holding the runs 
     */
    public LinkedList<RunNode> getRunsList() {
        return l;
    }
    
    
    /**
     * @param ibuf the input buffer to set for this container
     */
    public void setInputBuffer(Buffer ibuf) {
        ib = ibuf;
    }
    
    
    /**
     * @return the input buffer, used in replacement selection
     */
    public Buffer getInputBuffer() {
        return ib;
    }
    
    /**
     * @param obuf the output buffer to set for this container
     */
    public void setOutputBuffer(Buffer obuf) {
        ob = obuf;
    }
    
    
    /**
     * @return the output buffer used for memory storage
     */
    public Buffer getOutputBuffer() {
        return ob;
    }
    
  
    private RandomAccessFile in;
    private RandomAccessFile runs;
    private MinHeap h;
    private LinkedList<RunNode> l;
    private Buffer ib;
    private Buffer ob;
    
    private static final int HEAP_SIZE = 8*8192;
    private static final int MAX_REC_HEAP = 4096;
}
