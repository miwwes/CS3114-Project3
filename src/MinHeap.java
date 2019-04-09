import java.nio.ByteBuffer;
import java.util.*;


/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 * 
 * This class implements the heap data structure, 
 * as an array-based min heap meaning the value 
 * at the root is smaller than all of its children. 
 * It has some specialized methods for its use as 
 * the memory for our external sorting assignment. 
 * 
 * The heap is also modified to move groups of 16
 * array elements around at a time (records).
 *
 */
public class minHeap {

    public byte[] arr; // Pointer to the arr array
    private int size; // Maximum number of records in arr
    private int n; // Number of records now in arr

    // Constructor supporting preloading of arr contents
    minHeap(byte[] h, int num, int largeness){ 
        arr = h;  
        n = num;  
        size = largeness;
        buildHeap(); 
    }
    // Constructor supporting preloading of arr contents
    minHeap() { 
        arr = new byte[4096];  
        n = 4096;  
        size = 4096;
    }
    
    // Return current number of records in the arr
    public byte[] getRecord(int pos) {
        return Arrays.copyOfRange(arr, pos, pos + 16);
    }

    // Return current number of records in the arr
    public int heapSize() {
        return n;
    }
    
    public int getLastPos() {
        return n*16 - 16;
    }
    
    /**
     * @param rec1
     * @param rec2
     * @return
     */
    public int compareRecords(byte[] rec1, byte[] rec2) {
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 8, 16));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 8, 16));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }


    // Return true if pos a leaf position, false otherwise
    /**
     * @param pos
     * @return
     */
    public boolean isLeaf(int pos) {
        return ((pos/16) >= n / 2) && ((pos/16) < n);
    }
    
    /**
     * @return if the number of records in heap is zero
     */
    public boolean empty() {
        return n == 0;
    }

    /**
     * Return position for left child of pos
     * @param pos the position to return the left child of
     * @return the position of the left child
     */
    public int leftchild(int pos) {
        if ((pos/16) >= n / 2) {
            return -1;
        }
        return (2 * pos) + 16;
    }

    /**
     * Return position for right child of pos
     * @param pos the position to return the right child of
     * @return the position of the right child
     */
    public int rightchild(int pos) {
        if ((pos/16) >= (n - 1) / 2) {
            return -1;
        }
        return (2 * pos) + 32;
    }

    /**
     * Return position for parent
     * @param pos the position to return the parent of
     * @return the index of the parent
     */
    public int parent(int pos) {
        if ((pos/16) <= 0) {
            return -1;
        }
        return (pos/16 - 1) / 2;
    }

    /**
     * Heapify contents of heap
     */
    public void buildHeap() {
        for (int i = (n / 2 - 1) * 16; i >= 0; i -= 16) {
            siftdown(i);
        }
    }
    
    /**
     * Heapify contents of heap of a specified size
     * @param size the size of the heap to build
     */
    public void buildHeap(int recordsLeft) {
        //byte[] temp = Arrays.copyOfRange(arr, start, 65536);
        //System.arraycopy(temp, 0, arr, 0, temp.length);
        if(recordsLeft < size) {
            int start = (size - recordsLeft) * 16;
            System.arraycopy(arr, start, arr, 0, recordsLeft*16);
        }
        n = recordsLeft;
        for (int i = (n / 2 - 1)*16; i >= 0; i -= 16) {
            siftdown(i);
        }
    }


    /**
     * Put element in its correct place in heap
     * @param pos the current position to check for sifting down
     */
    private void siftdown(int pos) {
        if ((pos < 0) || ((pos/16) >= n)) {
            return; // Illegal position
        }
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            byte a[] = Arrays.copyOfRange(arr, j, j + 16);
            byte b[] = Arrays.copyOfRange(arr, j + 16, j + 32);
            if (((j/16) < (n - 1)) && (compareRecords(a, b) >= 0)) {
                j += 16; // j is now index of child with lesser value
            }
            byte c[] = Arrays.copyOfRange(arr, pos, pos + 16);
            byte d[] = Arrays.copyOfRange(arr, j, j + 16);
            if (compareRecords(c, d) <= 0) {
                return;
            }
            swap(j, pos);
            pos = j; // Move down
            
        }
    }
    
    /**
     * Swaps the values in two positions
     * @param pos1 the first position to swap
     * @param pos2 the second position to swap
     */
    private void swap(int pos1, int pos2) {
        byte[] temp = Arrays.copyOfRange(arr, pos1, pos1 + 16);
        int i = pos1;
        int j = pos2;
        for (; i < pos1 + 16; i++, j++) {
            arr[i] = arr[j];
        }
        i = 0;
        j = pos2; //reset parent value
        for (; j < pos2 + 16; j++, i++) {
            arr[j] = temp[i];
        }  
    }

    /**
     * Remove and return minimum value
     * @return the minimum value of the heap
     */
    public byte[] removemin() {
        if (n == 0) {
            return null; // Removing from empty arr
        }
        swap(0, (--n) * 16); // Swap maximum with last value
        if (n != 0) {// Not on last element
            siftdown(0); // Put new arr root val in correct place
        }
        
        return Arrays.copyOfRange(arr, n*16, n*16 + 16);
    }
    

    /**
     * Remove the minimum value and replace with array b
     * @param b the value to replace the minimum with
     * @return the minimum value in byte array form
     */
    public byte[] removemin(byte[] b) {
        if (n == 0) {
            return null; // Removing from empty arr
        }
        swap(0, (--n) * 16); // Swap minimum with last value
        if (n != 0) {// Not on last element
            siftdown(0); // Put new arr root val in correct place
        }
        System.arraycopy(b, 0, arr, n*16, 16);
        return Arrays.copyOfRange(arr, n*16, n*16 + 16);
    }

    /**
     * Modify the value at the given position
     * @param pos the position to place the newVal at
     * @param newVal the new value to insert
     */
    public void modify(int pos, byte[] newVal) {
      if ((pos < 0) || (pos >= n * 16)) {
          return; // Illegal heap position
      }
      System.arraycopy(newVal, 0, arr, pos, 16);
      update(pos);
    }

    /**
     * The value at pos has been changed, so restore the heap
     * @param pos the position that has the changed value
     */
    private void update(int pos) {
      // If it is a big value, push it up
      while ((pos > 0) && (compareRecords(
                      Arrays.copyOfRange(arr, pos, pos + 16), 
                      Arrays.copyOfRange(arr, parent(pos), parent(pos) + 16)) < 0)) {
          swap(pos, parent(pos));
          pos = parent(pos);
      }
      if (n != 0) {
          siftdown(pos); // If it is little, push down
      }
    }
    
    /**
     * Helper function that takes a long and a double and returns a
     * byte array
     * @param id the id 
     * @param key
     * @return
     */
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
    
    public double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        //System.out.println("id: " + id);
        //System.out.println("key: " + key);
        return key;
    }
    
  }
