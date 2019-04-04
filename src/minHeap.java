import java.nio.ByteBuffer;
import java.util.*;


/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 */
public class minHeap {

    private byte[] arr; // Pointer to the arr array
    private final int size = 4096; // Maximum number of records in arr
    private int n; // Number of records now in arr

    // Constructor supporting preloading of arr contents
    minHeap(byte[] h, int num){ 
        arr = h;  
        n = num;  
        buildHeap(); 
    }


    // Return current number of records in the arr
    int heapSize() {
        return n;
    }
    
    int getLastPos() {
        return n*16 - 16;
    }
    
    /**
     * @param rec1
     * @param rec2
     * @return
     */
    int compareRecords(byte[] rec1, byte[] rec2) {
        byte[] k1 = new byte[8];
        byte[] k2 = new byte[8];
        System.arraycopy( rec1, 8, k1, 0, 8);
        System.arraycopy( rec2, 8, k2, 8, 8);
        double key1 = ByteBuffer.wrap(k1).getDouble();
        double key2 = ByteBuffer.wrap(k2).getDouble();
        if (key1 < key2) {
            return -1;
        }
        else if (key1 > key2) {
            return 1;
        }
        else {
            return 0;
        }
    }


    // Return true if pos a leaf position, false otherwise
    /**
     * @param pos
     * @return
     */
    boolean isLeaf(int pos) {
        return ((pos/16) >= n / 2) && ((pos/16) < n);
    }


    // Return position for left child of pos
    /**
     * @param pos
     * @return
     */
    int leftchild(int pos) {
        if ((pos/16) >= n / 2)
            return -1;
        return (2 * pos) + 16;
    }


    // Return position for right child of pos
    /**
     * @param pos
     * @return
     */
    int rightchild(int pos) {
        if ((pos/16) >= (n - 1) / 2)
            return -1;
        return (2 * pos) + 32;
    }


    // Return position for parent
    /**
     * @param pos
     * @return
     */
    int parent(int pos) {
        if ((pos/16) <= 0)
            return -1;
        return (pos - 16) / 2;
    }


    // Insert val into arr
    /**
     * @param key
     */
    void insert(byte[] key) {
        if (n*16 >= size) {
            System.out.println("Heap is full");
            return;
        }
        int curr = n*16;
        n++;
        System.arraycopy(key, 0, arr, curr, 16);
        // Now sift up until curr's parent's key < curr's key
        byte a[] = Arrays.copyOfRange(arr, curr, curr + 16);
        byte b[] = Arrays.copyOfRange(arr, parent(curr), parent(curr) + 16);
        while ((curr != 0) && (compareRecords(a, b) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * @param arr2
     * @param curr
     * @param parent
     */
    private void swap(int curr, int parent) {
        byte[] temp = Arrays.copyOfRange(arr, curr, curr + 16);
        int i = curr;
        int j = parent;
        for (; i < curr + 16; i++, j++) {
            arr[i] = arr[j];
        }
        i = 0;
        j = parent; //reset parent value
        for (; j < parent + 16; j++, i++) {
            arr[j] = temp[i];
        }  
    }


    // Heapify contents of heap
    /**
     * 
     */
    void buildHeap() {
        for (int i = n / 2 - 1; i >= 0; i--)
            siftdown(i * 16);
    }


    // Put element in its correct place
    /**
     * @param pos
     */
    void siftdown(int pos) {
        if ((pos < 0) || ((pos/16) >= n))
            return; // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if (rightchild(pos) != -1) {
                byte a[] = Arrays.copyOfRange(arr, j, j + 16);
                byte b[] = Arrays.copyOfRange(arr, j + 16, j + 32);
                
                if (((j/16) < (n - 1)) && (compareRecords(a, b) >= 0))
                    j += 16; // j is now index of child with lesser value
            }
            byte c[] = Arrays.copyOfRange(arr, pos, pos + 16);
            byte d[] = Arrays.copyOfRange(arr, j, j + 16);
            if (compareRecords(c, d) < 0)
                return;
            swap(pos, j);
            pos = j; // Move down
        }
    }


    // Remove and return maximum value
    /**
     * @return
     */
    byte[] removemin() {
        if (n == 0)
            return null; // Removing from empty arr
        swap(0, (--n) * 16); // Swap maximum with last value
        if (n != 0) // Not on last element
            siftdown(0); // Put new arr root val in correct place
        return Arrays.copyOfRange(arr, n, n + 16);
    }
    
    byte[] removemin(byte[] b) {
        if (n == 0)
            return null; // Removing from empty arr
        swap(0, (--n) * 16); // Swap maximum with last value
        if (n != 0) // Not on last element
            siftdown(0); // Put new arr root val in correct place
        System.arraycopy(b, 0, arr, n, n+16);
        return Arrays.copyOfRange(arr, n, n + 16);
    }
    
    // return maximum value
    /**
     * @return
     */
    public byte[] getMin() {
        return Arrays.copyOfRange(arr, 0, 16);
    }


    // Modify the value at the given position
    /**
     * @param pos
     * @param newVal
     */
    public void modify(int pos, byte[] newVal) {
      if ((pos < 0) || (pos >= n * 16)) return; // Illegal heap position
      System.arraycopy(newVal, 0, arr, pos, 16);
      update(pos);
    }


    // The value at pos has been changed, restore the arr property
    /**
     * @param pos
     */
    void update(int pos) {
      // If it is a big value, push it up
      byte a[] = Arrays.copyOfRange(arr, pos, pos + 16);
      byte b[] = Arrays.copyOfRange(arr, parent(pos), parent(pos) + 16);        
      while ((pos > 0) && (compareRecords(a, b) < 0)) {
        swap(pos, parent(pos));
        pos = parent(pos);
      }
      if (n != 0) siftdown(pos); // If it is little, push down
    }
  }
