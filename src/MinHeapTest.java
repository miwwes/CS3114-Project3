import student.TestCase;
import java.util.Arrays;
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 * 
 *  Test class for MinHeap
 */
public class MinHeapTest extends TestCase {
    
    
    /**
     * Test method for MinHeap
     */
    public void testMinHeap() {
        MinHeap m = new MinHeap();
        byte[] y = m.toByteArray(15, 8);
        byte[] y2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( y, 0, record, 0, 16);
        System.arraycopy( y2, 0, record, 16, 16);

        MinHeap myHeap = new MinHeap(record, 2, 4096);
        double val = m.toNumber(myHeap.getRecord(0));
        assertEquals((int)val, 5);
    }


    /**
     * Test method for HeapSize
     */
    public void testHeapSize() {
        MinHeap m = new MinHeap();
        byte[] byteArr = m.toByteArray(15, 8);
        byte[] byteArr2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        MinHeap myHeap = new MinHeap(record, 2, 4096);
        assertEquals(myHeap.heapSize(), 2);
    }


    /**
     * Test method for CompareRecords
     */
    public void testCompareRecords() {
        MinHeap m = new MinHeap();
        byte[] b = m.toByteArray(15, 8);
        byte[] b2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( b, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        MinHeap myHeap = new MinHeap(record, 2, 4096);
        assertEquals(myHeap.compareRecords(b, b2), 1);
    }


    /**
     * Test method for IsLeaf
     */
    public void testIsLeaf() {
        MinHeap m = new MinHeap();
        byte[] byteArr = m.toByteArray(15, 8);
        byte[] byteArr2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);

        MinHeap myHeap = new MinHeap(record, 2, 4096);
        assertTrue(myHeap.isLeaf(16));
    }


    /**
     * Test method for Leftchild
     */
    public void testLeftchild() {
        MinHeap m = new MinHeap();
        byte[] byteArr = m.toByteArray(15, 8);
        byte[] byteArr2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);

        MinHeap myHeap = new MinHeap(record, 2, 4096);
        assertEquals(myHeap.leftchild(0), 16);
    }


    /**
     * Test method for Rightchild
     */
    public void testRightchild() {
        MinHeap m = new MinHeap();
        byte[] byteArr = m.toByteArray(15, 8);
        byte[] byteArr2 = m.toByteArray(18, 5);
        byte[] byteArr3 = m.toByteArray(14, 2);
        byte[] record = new byte[48];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);
        System.arraycopy( byteArr3, 0, record, 32, 16);

        MinHeap myHeap = new MinHeap(record, 3, 4096);

        assertEquals(myHeap.rightchild(0), 32);
    }


    /**
     * Test method for Parent
     */
    public void testParent() {
        MinHeap m = new MinHeap();
        byte[] byteArr = m.toByteArray(15, 8);
        byte[] byteArr2 = m.toByteArray(18, 5);
        byte[] record = new byte[32];
        System.arraycopy( byteArr, 0, record, 0, 16);
        System.arraycopy( byteArr2, 0, record, 16, 16);

        MinHeap myHeap = new MinHeap(record, 2, 4096);
        assertEquals(myHeap.parent(16), 0);
    }


    /**
     * Test method for BuildHeap
     */
    public void testBuildHeap() {
        MinHeap m = new MinHeap();
        byte[] b1 = m.toByteArray(15, 8);
        byte[] b2 = m.toByteArray(18, 5);
        byte[] b3 = m.toByteArray(15, 8);
        byte[] b4 = m.toByteArray(18, 5);
        byte[] b5 = m.toByteArray(15, 8);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        MinHeap myHeap = new MinHeap(record, 5, 4096);
        assertEquals(myHeap.heapSize(), 5);
        assertEquals(myHeap.parent(32), 0);
    }


    /**
     * Test method for Siftdown
     */
    public void testSiftdown() {
        MinHeap m = new MinHeap();
        byte[] b1 = m.toByteArray(15, 2);
        byte[] b2 = m.toByteArray(18, 3);
        byte[] b3 = m.toByteArray(15, 55);
        byte[] b4 = m.toByteArray(18, 7);
        byte[] b5 = m.toByteArray(15, 8);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        MinHeap myHeap = new MinHeap(record, 5, 4096);
        assertTrue(Arrays.equals(myHeap.getRecord(0),
                    m.toByteArray(15, 2)));
        assertTrue(Arrays.equals(myHeap.getRecord(16),
                    m.toByteArray(18, 3)));
    }


    /**
     * Test method for Removemin
     */
    public void testRemovemin() {
        MinHeap m = new MinHeap();
        byte[] b1 = m.toByteArray(15, 7);
        byte[] b2 = m.toByteArray(18, 6);
        byte[] b3 = m.toByteArray(15, 4);
        byte[] b4 = m.toByteArray(18, 8);
        byte[] b5 = m.toByteArray(15, 5);
        byte[] record = new byte[80];
        System.arraycopy( b1, 0, record, 0, 16);
        System.arraycopy( b2, 0, record, 16, 16);
        System.arraycopy( b3, 0, record, 32, 16);
        System.arraycopy( b4, 0, record, 48, 16);
        System.arraycopy( b5, 0, record, 64, 16);
        MinHeap myHeap = new MinHeap(record, 5, 4096);
        assertTrue(Arrays.equals(myHeap.getRecord(0), m.toByteArray(15, 4)));
        myHeap.removemin();
        assertTrue(Arrays.equals(myHeap.getRecord(0), m.toByteArray(15, 5)));
        myHeap.removemin();
        assertTrue(Arrays.equals(myHeap.getRecord(0), m.toByteArray(18, 6)));
        myHeap.removemin();
        assertTrue(Arrays.equals(myHeap.getRecord(0), m.toByteArray(15, 7)));
        myHeap.removemin();
        assertTrue(Arrays.equals(myHeap.getRecord(0), m.toByteArray(18, 8)));

        byte[] a1 = m.toByteArray(15, 7);
        byte[] a2 = m.toByteArray(18, 6);
        byte[] a3 = m.toByteArray(15, 4);
        byte[] a4 = m.toByteArray(18, 8);
        byte[] a5 = m.toByteArray(15, 5);
        byte[] r = new byte[80];
        System.arraycopy( a1, 0, r, 0, 16);
        System.arraycopy( a2, 0, r, 16, 16);
        System.arraycopy( a3, 0, r, 32, 16);
        System.arraycopy( a4, 0, r, 48, 16);
        System.arraycopy( a5, 0, r, 64, 16);
        MinHeap h = new MinHeap(r, 5, 4096);
        assertTrue(Arrays.equals(h.getRecord(0), m.toByteArray(15, 4)));
        byte[] t1 = m.toByteArray(15, 2);
        h.removemin(t1);
        assertTrue(Arrays.equals(h.getRecord(0), m.toByteArray(15, 5)));
        h.removemin(t1);
        assertTrue(Arrays.equals(h.getRecord(0), m.toByteArray(18, 6)));
        h.removemin(t1);
        assertTrue(Arrays.equals(h.getRecord(0), m.toByteArray(15, 7)));
        h.removemin(t1);
        assertTrue(Arrays.equals(h.getRecord(0), m.toByteArray(18, 8)));
    }

}
