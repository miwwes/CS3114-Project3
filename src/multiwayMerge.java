import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 */
/** 
 * pos The offset position, measured in bytes from the 
 * beginning of the file, at which to set the file pointer.
 * 
 * Need to keep track of the byte position of each run in
 * the file pointer.
 * 
 * So keep track of how large the output buffer is in bytes
 * 
 * Once the first run is complete (i.e., the heap becomes empty),
 *
 */
public class multiwayMerge {
    
    static double THRESHOLD = 0.1;
    
    multiwayMerge(LinkedList<runNode> locations, minHeap h) {
        runLocations = locations;
        heap = h;
        File newFile = new File("output2.bin");
        try {
            outFile = new RandomAccessFile(newFile, "rw");
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    private LinkedList<runNode> runLocations;
    private minHeap heap;
    private RandomAccessFile outFile;

}
