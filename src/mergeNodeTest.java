//import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import student.TestCase;
import java.nio.ByteBuffer;
//import org.junit.jupiter.api.Test;
import java.util.Arrays;

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
    //@Test
    public void testMergeNode() {
        byte[] b = toByteArray(10, 4);
        mergeNode mn = new mergeNode(0, b, 0, blockLength);
        assertEquals(mn.getBlockNumber(), 0);
        assertEquals(mn.key, 4);
    }


    /**
     * Test method for {@link mergeNode#getBlockNumber()}.
     */
    //@Test
    public void testGetBlockNumber() {
        byte[] b = toByteArray(1, 48);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        assertEquals(mn.getBlockNumber(), 1);
    }


    /**
     * Test method for {@link mergeNode#setBlockNumber(int)}.
     */
    //@Test
    public void testSetBlockNumber() {
        byte[] b = toByteArray(51, 8);
        mergeNode mn = new mergeNode(1, b, blockLength, blockLength * 2);
        mn.setBlockNumber(4);
        assertEquals(mn.getBlockNumber(), 4);
    }


    /**
     * Test method for {@link mergeNode#getRecord()}.
     */
    //@Test
    public void testGetRecord() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#key()}.
     */
    @Test
    void testKey() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#getCurPos()}.
     */
    @Test
    void testGetCurPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#setCurPos(int)}.
     */
    @Test
    void testSetCurPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#getStartPos()}.
     */
    @Test
    void testGetStartPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#setStartPos(int)}.
     */
    @Test
    void testSetStartPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#getEndPos()}.
     */
    @Test
    void testGetEndPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#setEndPos(int)}.
     */
    @Test
    void testSetEndPos() {
        fail("Not yet implemented");
    }


    /**
     * Test method for {@link mergeNode#incrementCurPos(int)}.
     */
    @Test
    void testIncrementCurPos() {
        fail("Not yet implemented");
    }

}
