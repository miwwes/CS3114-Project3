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
     * Test method for {@link minHeap#minHeap(byte[], int)}.
     */
    @Test
    void testMinHeap() {  
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
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
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        minHeap myHeap = new minHeap(record, 2, 4096);
        byte[] byteArr3 = toByteArray(14, 12);
        myHeap.insert(byteArr3);
        assert(myHeap.heapSize() == 3);
    }


    /**
     * Test method for {@link minHeap#compareRecords(byte[], byte[])}.
     */
    @Test
    void testCompareRecords() {
        byte[] b = toByteArray(15, 8);
        byte[] b2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( b, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        minHeap myHeap = new minHeap(record, 2, 4096);
        assert(myHeap.compareRecords(b, b2) == -1);
    }


    /**
     * Test method for {@link minHeap#isLeaf(int)}.
     */
    @Test
    void testIsLeaf() {
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        
        minHeap myHeap = new minHeap(record, 2, 4096);
        assert(myHeap.isLeaf(16));
    }


    /**
     * Test method for {@link minHeap#leftchild(int)}.
     */
    @Test
    void testLeftchild() {
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        
        minHeap myHeap = new minHeap(record, 2, 4096);
        assert(myHeap.leftchild(0) == 16);
    }


    /**
     * Test method for {@link minHeap#rightchild(int)}.
     */
    @Test
    void testRightchild() {
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        
        minHeap myHeap = new minHeap(record, 2, 4096);
        byte[] byteArr3 = toByteArray(14, 2);
        myHeap.insert(byteArr3);
        assert(myHeap.rightchild(0) == 32);
    }


    /**
     * Test method for {@link minHeap#parent(int)}.
     */
    @Test
    void testParent() {
        byte[] byteArr = toByteArray(15, 8);
        byte[] byteArr2 = toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        
        minHeap myHeap = new minHeap(record, 2, 4096);
        byte[] byteArr3 = toByteArray(14, 2);
        myHeap.insert(byteArr3);
        assert(myHeap.parent(32) == 0);
    }


    /**
     * Test method for {@link minHeap#insert(byte[])}.
     */
    @Test
    void testInsert() {
        byte[] byteArr = toByteArray(15, 8);
        minHeap myHeap = new minHeap(byteArr, 2, 4096);
        byte[] b1 = toByteArray(14, 2);
        myHeap.insert(b1);
        byte[] b2 = toByteArray(1, 22);
        myHeap.insert(b2);
        byte[] b3 = toByteArray(143, 41);
        myHeap.insert(b3);
        byte[] b4 = toByteArray(144, 32);
        myHeap.insert(b4);
        assert(myHeap.heapSize() == 5);
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(14, 2)));
        assert(Arrays.equals(myHeap.getRecord(64), toByteArray(143, 41)));
    }


    /**
     * Test method for {@link minHeap#buildHeap()}.
     */
    @Test
    void testBuildHeap() {
        byte[] b1 = toByteArray(15, 8);
        byte[] b2 = toByteArray(18, 5);
        byte[] b3 = toByteArray(15, 8);
        byte[] b4 = toByteArray(18, 5);
        byte[] b5 = toByteArray(15, 8);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        minHeap myHeap = new minHeap(record, 5, 4096);
        assert(myHeap.heapSize() == 5);
        assert(myHeap.parent(32) == 0);
    }


    /**
     * Test method for {@link minHeap#siftdown(int)}.
     */
    @Test
    void testSiftdown() {
        byte[] b1 = toByteArray(15, 2);
        byte[] b2 = toByteArray(18, 3);
        byte[] b3 = toByteArray(15, 55);
        byte[] b4 = toByteArray(18, 7);
        byte[] b5 = toByteArray(15, 8);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        minHeap myHeap = new minHeap(record, 5, 4096);
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(15, 2)));
        byte[] b6 = toByteArray(143, 41);
        myHeap.insert(b6);
        byte[] b7 = toByteArray(144, 1);
        myHeap.insert(b7);
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(144, 1)));
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
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(15, 2)));
        byte[] b6 = toByteArray(143, 41);
        myHeap.insert(b6);
        byte[] b7 = toByteArray(144, 1);
        myHeap.insert(b7);
        assert(Arrays.equals(myHeap.getRecord(0), toByteArray(144, 1)));
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
