/**
 * 
 */

/**
 * @author jmkuz
 *
 */
public class MinHeap<R> {

    private Record[] Heap; // Pointer to the heap array
    private final int size = 4096; // Maximum number of records in heap
    private int n; // Number of things now in heap


    // Constructor supporting preloading of heap contents
    MinHeap(Record[] h, int num){ 
        Heap = h;  
        n = num;  
        buildheap(); 
    }


    // Return current size of the heap
    int heapsize() {
        return n;
    }


    // Return true if pos a leaf position, false otherwise
    boolean isLeaf(int pos) {
        return (pos >= n / 2) && (pos < n);
    }


    // Return position for left child of pos
    int leftchild(int pos) {
        if (pos >= n / 2)
            return -1;
        return 2 * pos + 1;
    }


    // Return position for right child of pos
    int rightchild(int pos) {
        if (pos >= (n - 1) / 2)
            return -1;
        return 2 * pos + 2;
    }


    // Return position for parent
    int parent(int pos) {
        if (pos <= 0)
            return -1;
        return (pos - 1) / 2;
    }


    // Insert val into heap
    void insert(byte[] key) {
        if (n >= size) {
            System.out.println("Heap is full");
            return;
        }
        Record myRecord = new Record(key);
        int curr = n++;
        Heap[curr] = myRecord; // Start at end of heap
        // Now sift up until curr's parent's key < curr's key
        while ((curr != 0) && (Heap[curr].compareTo(Heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * @param heap2
     * @param curr
     * @param parent
     */
    private void swap(int curr, int parent) {
        Record temp = Heap[curr];
        Heap[curr] = Heap[parent];
        Heap[parent] = temp;   
    }


    // Heapify contents of Heap
    void buildheap() {
        for (int i = n / 2 - 1; i >= 0; i--)
            siftdown(i);
    }


    // Put element in its correct place
    void siftdown(int pos) {
        if ((pos < 0) || (pos >= n))
            return; // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (Heap[j].compareTo(Heap[j + 1]) >= 0))
                j++; // j is now index of child with greater value
            if (Heap[pos].compareTo(Heap[j]) < 0)
                return;
            swap(pos, j);
            pos = j; // Move down
        }
    }


    // Remove and return maximum value
    /*Comparable removemax() {
        if (n == 0)
            return -1; // Removing from empty heap
        swap(Heap, 0, --n); // Swap maximum with last value
        if (n != 0) // Not on last element
            siftdown(0); // Put new heap root val in correct place
        return Heap[n];
    }*/


    // Remove and return element at specified position
    /*Comparable remove(int pos) {
        if ((pos < 0) || (pos >= n))
            return -1; // Illegal heap position
        if (pos == (n - 1))
            n--; // Last element, no work to be done
        else {
            swap(Heap, pos, --n); // Swap with last value
            update(pos);
        }
    }*/


    // Modify the value at the given position
    void modify(int pos, Record newVal) {
      if ((pos < 0) || (pos >= n)) return; // Illegal heap position
      Heap[pos] = newVal;
      update(pos);
    }


    // The value at pos has been changed, restore the heap property
    void update(int pos) {
      // If it is a big value, push it up
      while ((pos > 0) && (Heap[pos].compareTo(Heap[parent(pos)]) < 0)) {
        swap(pos, parent(pos));
        pos = parent(pos);
      }
      if (n != 0) siftdown(pos); // If it is little, push down
    }
  }
