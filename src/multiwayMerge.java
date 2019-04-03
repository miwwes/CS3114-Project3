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
    
    multiwayMerge(LinkedList<Integer> locations, minHeap h) {
        runLocations = locations;
        heap = h;
    }
    
    
    private LinkedList<Integer> runLocations;
    private minHeap heap;

}
