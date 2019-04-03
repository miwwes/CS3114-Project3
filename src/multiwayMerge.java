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
    
    void mergesortOpt(Comparable[] A, Comparable[] temp, int left, int right) {
        int i, j, k, mid = (left+right)/2;  // Select the midpoint
        if (left == right) {
            return;          // List has one record
        }
        if ((mid-left) >= THRESHOLD) {
            mergesortOpt(A, temp, left, mid);
        }
        else {
            inssort(A, left, mid);
        }
        if ((right-mid) > THRESHOLD) {
            mergesortOpt(A, temp, mid+1, right);
        }
        else {
            inssort(A, mid+1, right);
        }
        // Do the merge operation.  First, copy 2 halves to temp.
        for (i=left; i<=mid; i++) {
            temp[i] = A[i];
        }
        for (j=right; j>mid; j--) {
            temp[i++] = A[j];
        }
        // Merge sublists back to array
        for (i=left,j=right,k=left; k<=right; k++) {
          if (temp[i].compareTo(temp[j]) <= 0) {
              A[k] = temp[i++];
          }
          else {
              A[k] = temp[j--];
          }
        }
      }
    
    
    private LinkedList<runNode> runLocations;
    private minHeap heap;

}
