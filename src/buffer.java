import java.util.Arrays;

/**
 * 
 */

/**
 * @author abbym1
 * @author juliam8
 * @version 4-4-19
 * 
 *  This class is to help interface with the byte
 *  arrays that are used to store the 16 byte records.
 *  Can be used for both input and output buffers.
 *
 */
public class buffer {
    
    /**
     *  Default constructor
     */
    public buffer() {
        byteArray = new byte[BUFFER_SIZE];
        size = 0;
        pos = 0;
        readPos = 0;
    }
    
    /**
     * resets the helper variables to zero
     */
    public void clear() {
        size = 0;
        pos = 0;
        readPos = 0;
    }
    
    /**
     * used when new data is written into the array
     */
    public void update() {
        size = byteArray.length/16;
        pos = 0;
        readPos = 0;
    }
    
    /**
     * @param b
     */
    public void insert(byte[] b) {
        if( !full() ) {
            System.arraycopy(b, 0, byteArray, pos, 16);
            pos += 16;
            size++;
        }
    }
    
    /**
     * @return the record being removed
     */
    public byte[] remove() {
        byte[] last = null;
        if( !empty() ) {
            last = Arrays.copyOfRange(byteArray, pos, pos + 16);
            pos -= 16;
            size--;
        }
        return last;
    }
    
    /**
     * @return the record at readPos
     */
    public byte[] read() {
        if(readPos >= pos)
            return null;
        readPos += 16;
        return Arrays.copyOfRange(byteArray, readPos - 16, readPos);
    }
    
    /**
     * @return true if array is full, else false
     */
    public boolean full() {
        return size * 16 == BUFFER_SIZE;
    }
    
    /**
     * @return true if array is empty, else false
     */
    public boolean empty() {
        return size == 0;
    }
    
    /**
     * @return the byte array (for writing)
     */
    public byte[] array() {
        return byteArray;
    }

    /**
     * @return the size of the byte array
     */
    public int size() {
        return size;
    }
    
    /**
     * The constant size for both input and output buffers
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * byte array storing records
     */
    private byte[] byteArray;
    /**
     * number of records in array
     */
    private int size;
    /**
     * current position in the array
     */
    private int pos;
    /**
     * Keeps track of what has been read from the array
     */
    private int readPos;

}
