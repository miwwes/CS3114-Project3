import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author amalone46
 *
 */
public class sortContainer {
    
    sortContainer(String s){
        ib = new buffer();
        ob = new buffer();
        l = new LinkedList<runNode>();
        try {
            in = new RandomAccessFile(s, "rw");
            runs = new RandomAccessFile("runfile.bin", "rw");
            
            byte[] heapArray = new byte[HEAP_SIZE];
            in.read(heapArray);
            h = new minHeap(heapArray, MAX_REC_HEAP, MAX_REC_HEAP);
            h.buildHeap();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public RandomAccessFile in;
    public RandomAccessFile runs;
    public minHeap h;
    public LinkedList<runNode> l;
    public buffer ib;
    public buffer ob;
    
    private static final int HEAP_SIZE = 8*8192;
    private static final int MAX_REC_HEAP = 4096;
}
