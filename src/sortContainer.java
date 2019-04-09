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
public class sortContainer {
    
    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        return key;
    }
    
    sortContainer(String s){
        ib = new Buffer();
        ob = new Buffer();
        l = new LinkedList<runNode>();
        try {
            in = new RandomAccessFile(s, "rw");
            runs = new RandomAccessFile("runfile.bin", "rw");
            in.seek(0);
            runs.seek(0);
            
            byte[] heapArray = new byte[HEAP_SIZE];
            in.read(heapArray);
            h = new MinHeap(heapArray, MAX_REC_HEAP, MAX_REC_HEAP);

            
        } catch (FileNotFoundException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public RandomAccessFile in;
    public RandomAccessFile runs;
    public MinHeap h;
    public LinkedList<runNode> l;
    public Buffer ib;
    public Buffer ob;
    
    private static final int HEAP_SIZE = 8*8192;
    private static final int MAX_REC_HEAP = 4096;
}
