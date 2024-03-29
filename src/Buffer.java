import java.io.IOException;
import java.io.RandomAccessFile;
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
 *  Provides methods to check if the buffer is empty,
 *  full, return the numRecords, clear the buffer, insert, 
 *  remove, etc.
 *
 */
public class Buffer {
    
    /**
     *  Default constructor: declares a constant
     *  size byte array and initializes private variables
     *  to zero.
     */
    public Buffer() {
        byteArray = new byte[BUFFER_SIZE];
        numRecords = 0;
        pos = 0;
    }
    
    /**
     * resets current position and number of 
     * records to zero
     */
    public void clear() {
        numRecords = 0;
        pos = 0;
    }
    
    /**
     * @param raf RandomAccessFile to load from
     * used when new data is written into the array
     * resets read position and calculates number of 
     * records
     * @throws IOException 
     */
    public void loadBlock(RandomAccessFile raf) throws IOException {
        clear();
        // get the current position
        long before = raf.getFilePointer();
        raf.read(byteArray);
        // calculate the byte difference
        long diff = raf.getFilePointer() - before;
        
        // calculate number of records based on nex file pos
        numRecords = (int)diff / RECORD_SIZE;
        pos = 0;
    }
    
    /**
     * @param record is a new record to add at the current
     * position if there is space
     */
    public void write(byte[] record) {
        if (!full()) {
            System.arraycopy(record, 0, byteArray, pos, RECORD_SIZE);
            pos += RECORD_SIZE;
            numRecords++;
        }
    }
    
    /**
     * @return the record at the current position
     * and increment the position
     */
    public byte[] read() {
        if (pos >= BUFFER_SIZE || pos < 0) {
            return null;
        }
        pos += RECORD_SIZE;
        return Arrays.copyOfRange(byteArray, pos - RECORD_SIZE, pos);
    }
    
    /**
     * @return boolean indicating if the current
     * position is at the end of the buffer
     */
    public boolean doneReading() {
        return pos == BUFFER_SIZE;
    }
    
    /**
     * @return true if buffer is full, else false
     */
    public boolean full() {
        return numRecords * RECORD_SIZE == BUFFER_SIZE;
    }
    
    /**
     * @return true if buffer is empty, else false
     */
    public boolean empty() {
        return numRecords == 0;
    }
    
    /**
     * @return the current position from which
     * a record will be read/written
     */
    public int pos() {
        return pos;
    }
    
    /**
     * @return the byte array (for reloading)
     */
    public byte[] array() {
        return byteArray;
    }
    
    /**
     * The constant size of the byte array
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * The constant number of bytes per record
     */
    private static final int RECORD_SIZE = 16;
    
    /**
     * byte array storing max BUFFER_SIZE/RECORD_SIZE 
     * number of records
     */
    private byte[] byteArray;
    
    /**
     * number of records currently in the array
     */
    private int numRecords;
    
    /**
     * current position from which you read/write in the array
     */
    private int pos;
    
}
