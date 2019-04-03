
import java.nio.ByteBuffer;
import java.util.*;
/**
 * @author jmkuz
 *
 */
public class Record {
    /*
     * Each block will contain a
series of records, where each record has 16 bytes. The first 8-byte field is a non-negative integer
value (long) for the record ID and the second 8-byte field is a double value for the key, which will
be used for sorting
     */
    /**
     * @param record
     */
    Record(byte[] record){ 
        id = ByteBuffer.wrap(Arrays.copyOfRange(record, 0, 7)).getLong();
        key = ByteBuffer.wrap(Arrays.copyOfRange(record, 8, 15)).getDouble();
    }
    
    /**
     * @return
     */
    public long getID() {
        return id;
    }
    
    /**
     * @return
     */
    public double getKey() {
        return key;
    }
    
    /**
     * @param newID
     */
    public void setID(long newID) {
        id = newID;
    }
    
    /**
     * @param newKey
     */
    public void setKey(double newKey) {
        key = newKey;
    }
    
    
    /**
     * Private member holding ID value
     */
    private long id;
    
    /**
     * Private member holding key value
     */
    private double key;

}