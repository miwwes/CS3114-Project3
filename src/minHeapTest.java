import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
     * Test method for {@link minHeap#minHeap(byte[], int)}.
     */
    @Test
    void testMinHeap() {
        byte[] byteArr = toByteArray(15, 8);
        //System.out.println(byteArr);
        //toNumber(byteArr);
        byte[] byteArr2 = toByteArray(18, 5);
        //System.out.println(byteArr2);
        //toNumber(byteArr2);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        
        minHeap myHeap = new minHeap(record, 2, 4096);
        double val = toNumber(myHeap.getMin());
        assert(val == 5);
    }
    

    /**
     * Test method for {@link minHeap#heapSize()}.
     */
    @Test
    void testHeapSize() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#compareRecords(byte[], byte[])}.
     */
    @Test
    void testCompareRecords() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#isLeaf(int)}.
     */
    @Test
    void testIsLeaf() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#leftchild(int)}.
     */
    @Test
    void testLeftchild() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#rightchild(int)}.
     */
    @Test
    void testRightchild() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#parent(int)}.
     */
    @Test
    void testParent() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#insert(byte[])}.
     */
    @Test
    void testInsert() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#buildHeap()}.
     */
    @Test
    void testBuildHeap() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#siftdown(int)}.
     */
    @Test
    void testSiftdown() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#removemin()}.
     */
    @Test
    void testRemovemin() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#getMin()}.
     */
    @Test
    void testGetMin() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#modify(int, byte[])}.
     */
    @Test
    void testModify() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link minHeap#update(int)}.
     */
    @Test
    void testUpdate() {
        fail("Not yet implemented");
    }


}
