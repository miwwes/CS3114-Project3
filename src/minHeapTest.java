import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 */
class minHeapTest {
    
    private byte[] toByteArray(long id, double key) {
        byte[] bytes1 = new byte[8];
        byte[] bytes2 = new byte[8];
        ByteBuffer.wrap(bytes1).putLong(id);
        ByteBuffer.wrap(bytes2).putDouble(key);
        byte[] record = new byte[16];
        System.arraycopy( bytes1, 0, record, 0, 8);
        System.arraycopy( bytes2, 0, record, 8, 8);
        return record;
    }

    private double toNumber(byte[] bytes) {
        byte[] idBytes = Arrays.copyOfRange(bytes, 0, 8);
        byte[] keyBytes = Arrays.copyOfRange(bytes, 8, 16);
        long id = ByteBuffer.wrap(idBytes).getLong();
        double key = ByteBuffer.wrap(keyBytes).getDouble();
        System.out.println("id: " + id);
        System.out.println("key: " + key);
        return key;
    }
   


    /**
     * Test method for {@link minHeap#removemin()}.
     */
    @Test
    void testRemovemin() {
        byte[] b1 = toByteArray(15, 2);
        byte[] b2 = toByteArray(18, 6);
        byte[] b3 = toByteArray(15, 4);
        byte[] b4 = toByteArray(18, 8);
        byte[] b5 = toByteArray(15, 5);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        minHeap myHeap = new minHeap(record, 5, 4096);
        byte[] b6 = toByteArray(143, 41);
        myHeap.insert(b6);
        byte[] b7 = toByteArray(144, 1);
        myHeap.insert(b7);
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(144, 1)));
        myHeap.removemin();
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(15, 2)));
        
    }


    /**
     * Test method for {@link minHeap#getMin()}.
     */
    //@Test
    //void testGetMin() {
    //    fail("Not yet implemented");
    //}


    /**
     * Test method for {@link minHeap#modify(int, byte[])}.
     */
    //@Test
    //void testModify() {
    //    fail("Not yet implemented");
    //}    

}
