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
public class MinHeap {

    /**
     * Constant number of max records in the heap
     */
    public static final int HEAP_SIZE = 4096;
    
    /**
     * Constant number of bytes in a record
     */
    public static final int RECORD_SIZE = 16;
    
    /**
     * Pointer to the arr array
     */
    private byte[] arr; 
    /**
     * Maximum number of records in arr
     */
    private int capacity;
    /**
     * Number of records now in arr
     */
    private int recordCount; 

    /**
     * Constructor supporting pre-loading of arr contents
     * @param memory the base array
     * @param num the cur number of records 
     * @param max the max number of records
     */
    MinHeap(byte[] memory, int num, int max) { 
        arr = memory;  
        recordCount = num;  
        capacity = max;
        buildHeap(); 
    }
    
    /**
     * Constructor supporting preloading of arr contents
     */
    MinHeap() { 
        arr = new byte[HEAP_SIZE];  
        recordCount = HEAP_SIZE;  
        capacity = HEAP_SIZE;
    }
    
    /**
     * @param pos
     * @return the record at the given array position
     */
    public byte[] getRecord(int pos) {
        return Arrays.copyOfRange(arr, pos, pos + RECORD_SIZE);
    }

    /**
     * @return current number of records in the array
     */
    public int heapSize() {
        return recordCount;
    }
    
    /**
     * @return the position of the last record
     */
    public int getLastPos() {
        return recordCount * RECORD_SIZE - RECORD_SIZE;
    }
    
    /**
     * Compare records by key value
     * must convert to doubles
     * @param rec1
     * @param rec2
     * @return 0, -1, or 1 depending on the input
     */
    public int compareRecords(byte[] rec1, byte[] rec2) {
        ByteBuffer buffer1 = ByteBuffer.wrap(Arrays.copyOfRange(rec1, 
                RECORD_SIZE / 2, RECORD_SIZE));
        Double rec1Double = buffer1.getDouble();
        ByteBuffer buffer2 = ByteBuffer.wrap(Arrays.copyOfRange(rec2, 
                RECORD_SIZE / 2, RECORD_SIZE));
        Double rec2Double = buffer2.getDouble();
        return rec1Double.compareTo(rec2Double);
    }

    /**
     * @return the byte array
     */
    public byte[] arr() {
        return arr;
    }

    /**
     * Return true if pos a leaf position, false otherwise
     * @param pos
     * @return
     */
    public boolean isLeaf(int pos) {
        return ((pos / RECORD_SIZE) >= recordCount / 2) && 
               ((pos / RECORD_SIZE) < recordCount);
    }
    
    /**
     * @return if the number of records in heap is zero
     */
    public boolean empty() {
        return recordCount == 0;
    }

    /**
     * Return position for left child of pos
     * @param pos the position to return the left child of
     * @return the position of the left child
     */
    public int leftchild(int pos) {
        if ((pos / RECORD_SIZE) >= recordCount / 2) {
            return -1;
        }
        return (2 * pos) + RECORD_SIZE;
    }

    /**
     * Return position for right child of pos
     * @param pos the position to return the right child of
     * @return the position of the right child
     */
    public int rightchild(int pos) {
        if ((pos / RECORD_SIZE) >= (recordCount - 1) / 2) {
            return -1;
        }
        return (2 * pos) + 2 * RECORD_SIZE;
    }

    /**
     * Return position for parent
     * @param pos the position to return the parent of
     * @return the index of the parent
     */
    public int parent(int pos) {
        if ((pos / RECORD_SIZE) <= 0) {
            return -1;
        }
        return (pos / RECORD_SIZE - 1) / 2;
    }

    /**
     * Heapify contents of heap
     */
    public void buildHeap() {
        int i = (recordCount / 2 - 1) * RECORD_SIZE;
        for (; i >= 0; i -= RECORD_SIZE) {
            siftdown(i);
        }
    }
    
    /**
     * Heapify contents of heap of a specified size
     * @param recordsLeft the number of records in heap
     */
    public void buildHeap(int recordsLeft) {
        if (recordsLeft < capacity) {
            int start = (capacity - recordsLeft) * RECORD_SIZE;
            System.arraycopy(arr, start, arr, 0, recordsLeft * RECORD_SIZE);
        }
        recordCount = recordsLeft;
        int i = (recordCount / 2 - 1) * RECORD_SIZE;
        for (; i >= 0; i -= RECORD_SIZE) {
            siftdown(i);
        }
    }


    /**
     * Put element in its correct place in heap
     * @param pos the current position to check for sifting down
     */
    private void siftdown(int pos) {
        if ((pos < 0) || ((pos / RECORD_SIZE) >= recordCount)) {
            return; // Illegal position
        }
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            byte[] a = Arrays.copyOfRange(arr, j, j + RECORD_SIZE);
            byte[] b = Arrays.copyOfRange(arr, j + RECORD_SIZE, j + 2 * RECORD_SIZE);
            if (((j / RECORD_SIZE) < (recordCount - 1)) && (compareRecords(a, b) >= 0)) {
                j += RECORD_SIZE; // j is now index of child with lesser value
            }
            byte[] c = Arrays.copyOfRange(arr, pos, pos + RECORD_SIZE);
            byte[] d = Arrays.copyOfRange(arr, j, j + RECORD_SIZE);
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
        byte[] temp = Arrays.copyOfRange(arr, pos1, pos1 + RECORD_SIZE);
        int i = pos1;
        int j = pos2;
        for (; i < pos1 + 16; i++, j++) {
            arr[i] = arr[j];
        }
        i = 0;
        j = pos2; //reset parent value
        for (; j < pos2 + RECORD_SIZE; j++, i++) {
            arr[j] = temp[i];
        }  
    }

    /**
     * Remove and return minimum value
     * @return the minimum value of the heap
     */
    public byte[] removemin() {
        if (recordCount == 0) {
            return null; // Removing from empty arr
        }
        swap(0, (--recordCount) * RECORD_SIZE); // Swap maximum with last value
        if (recordCount != 0) { // Not on last element
            siftdown(0); // Put new arr root val in correct place
        }
        
        return Arrays.copyOfRange(arr, 
                                  recordCount * RECORD_SIZE, 
                                  recordCount * RECORD_SIZE + RECORD_SIZE);
    }
    

    /**
     * Remove the minimum value and replace with array b
     * @param b the value to replace the minimum with
     * @return the minimum value in byte array form
     */
    public byte[] removemin(byte[] b) {
        if (recordCount == 0) {
            return null; // Removing from empty arr
        }
        swap(0, (--recordCount) * RECORD_SIZE); // Swap minimum with last value
        if (recordCount != 0) { // Not on last element
            siftdown(0); // Put new arr root val in correct place
        }
        System.arraycopy(b, 0, arr, recordCount * RECORD_SIZE, RECORD_SIZE);
        return Arrays.copyOfRange(arr, 
                                  recordCount * RECORD_SIZE, 
                                  recordCount * RECORD_SIZE + RECORD_SIZE);
    }

    /**
     * Modify the value at the given position
     * @param pos the position to place the newVal at
     * @param newVal the new value to insert
     */
    public void modify(int pos, byte[] newVal) {
        if ((pos < 0) || (pos >= recordCount * RECORD_SIZE)) {
            return; // Illegal heap position
        }
        System.arraycopy(newVal, 0, arr, pos, RECORD_SIZE);
        update(pos);
    }

    /**
     * The value at pos has been changed, so restore the heap
     * @param pos the position that has the changed value
     */
    private void update(int pos) {
        // If it is a big value, push it up
        while ((pos > 0) && 
               (compareRecords(
                      Arrays.copyOfRange(arr, pos, pos + RECORD_SIZE), 
                      Arrays.copyOfRange(arr, parent(pos), 
                      parent(pos) + RECORD_SIZE)) < 0)) {
            swap(pos, parent(pos));
            pos = parent(pos);
        }
        if (recordCount != 0) {
            siftdown(pos); // If it is little, push down
        }
    }
    
    /**
     * Helper function that takes a long and a double and returns a
     * byte array for testing
     * @param id the id 
     * @param key
     * @return the record as a byte array
     */
    public byte[] toByteArray(long id, double key) {
        byte[] bytes1 = new byte[RECORD_SIZE / 2];
        byte[] bytes2 = new byte[RECORD_SIZE / 2];
        ByteBuffer.wrap(bytes1).putLong(id);
        ByteBuffer.wrap(bytes2).putDouble(key);
        byte[] record = new byte[RECORD_SIZE];
        System.arraycopy( bytes1, 0, record, 0, RECORD_SIZE / 2);
        System.arraycopy( bytes2, 0, record, RECORD_SIZE / 2, RECORD_SIZE / 2);
        return record;
    }
    
    /**
     * Testing function
     * @param bytes
     * @return the key as a double
     */
    public double toNumber(byte[] bytes) {
        byte[] keyBytes = Arrays.copyOfRange(bytes, RECORD_SIZE / 2, RECORD_SIZE);
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        return key;
    }
    
}
