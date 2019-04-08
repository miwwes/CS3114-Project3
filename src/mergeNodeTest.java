//import static org.junit.jupiter.api.Assertions.*;
import student.TestCase;
import java.nio.ByteBuffer;
//import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author jmkuz
 *
 */
public class mergeNodeTest extends TestCase {
    
    static int blockLength = 8192;
    
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
    /**
     * Test method for {@link mergeNode#mergeNode(int, byte[], int, int)}.
     */
    public void testMergeNode() {
        byte[] b = toByteArray(10, 4);
        mergeNode mn = new mergeNode(0, b, 0, blockLength);
        assertEquals(mn.getBlockNumber(), 0);
        assertEquals(mn.key, 4, 0.1);
    }


    /**
     * Test method for {@link mergeNode#getBlockNumber()}.
     */
    public void testGetBlockNumber() {
        byte[] b = toByteArray(1, 48);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        assertEquals(mn.getBlockNumber(), 1);
    }


    /**
     * Test method for {@link mergeNode#setBlockNumber(int)}.
     */
    public void testSetBlockNumber() {
        byte[] b = toByteArray(51, 8);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        mn.setBlockNumber(4);
        assertEquals(mn.getBlockNumber(), 4);
    }


    /**
     * Test method for {@link mergeNode#getRecord()}.
     */
    public void testGetRecord() {
        byte[] b = toByteArray(51, 8);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        assertEquals(mn.getRecord(), b);
    }


    /**
     * Test method for {@link mergeNode#getCurPos()}.
     */
    void testGetCurPos() {
        byte[] b = toByteArray(51, 8);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        assertEquals(mn.getCurPos(), blockLength);
    }


    /**
     * Test method for {@link mergeNode#setCurPos(int)}.
     */
    void testSetCurPos() {
        byte[] b = toByteArray(5, 28);
        mergeNode mn = new mergeNode(0, b, 0, blockLength);
        mn.setCurPos(blockLength);
        assertEquals(mn.getCurPos(), blockLength);
    }


    /**
     * Test method for {@link mergeNode#getStartPos()}.
     */
    void testGetStartPos() {
        byte[] b = toByteArray(51, 8);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        assertEquals(mn.getStartPos(), blockLength);
    }


    /**
     * Test method for {@link mergeNode#setStartPos(int)}.
     */
    void testSetStartPos() {
        byte[] b = toByteArray(51, 48);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        mn.setStartPos(0);
        assertEquals(mn.getStartPos(), 0);
    }


    /**
     * Test method for {@link mergeNode#getEndPos()}.
     */
    void testGetEndPos() {
        byte[] b = toByteArray(61, 8);
        mergeNode mn = new mergeNode(0, b, 0, blockLength);
        assertEquals(mn.getEndPos(), blockLength);
    }

    /**
     * Test method for {@link mergeNode#setEndPos(int)}.
     */
    void testSetEndPos() {
        byte[] b = toByteArray(9, 58);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        mn.setEndPos(blockLength + (blockLength/2));
        assertEquals(mn.getEndPos(), blockLength + (blockLength/2));
    }


    /**
     * Test method for {@link mergeNode#incrementCurPos(int)}.
     */
    void testIncrementCurPos() {
        byte[] b = toByteArray(50, 88);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        mn.incrementCurPos(16);
        assertEquals(mn.getBlockNumber(), blockLength + 16);
    }

}
