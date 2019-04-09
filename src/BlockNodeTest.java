import student.TestCase;
import java.nio.ByteBuffer;

/**
 * 
 */

/**
 * @author juliam8
 * @author abbym1
 * @version 4-9-19
 * 
 * This is the testing class for BlockNode
 */
public class BlockNodeTest extends TestCase {
    
    /**
     * Constant representing block length
     */
    public static final int BLOCK_LENGTH = 8192;
    
    /**
     * @param id long value 
     * @param key double key
     * @return record as bytes
     */
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
     * Test method for MergeNode
     */
    public void testMergeNode() {
        byte[] b = toByteArray(10, 4);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        assertEquals(mn.getBlockNumber(), 0);
        assertEquals(mn.key(), 4, 0.1);
    }


    /**
     * Test method for GetBlockNumber
     */
    public void testGetBlockNumber() {
        byte[] b = toByteArray(1, 48);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getBlockNumber(), 1);
    }


    /**
     * Test method for SetBlockNumber
     */
    public void testSetBlockNumber() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setBlockNumber(4);
        assertEquals(mn.getBlockNumber(), 4);
    }


    /**
     * Test method for GetRecord
     */
    public void testGetRecord() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getRecord(), b);
    }


    /**
     * Test method for GetCurPos
     */
    void testGetCurPos() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getCurPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for SetCurPos
     */
    void testSetCurPos() {
        byte[] b = toByteArray(5, 28);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        mn.setCurPos(BLOCK_LENGTH);
        assertEquals(mn.getCurPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for GetStartPos
     */
    void testGetStartPos() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getStartPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for SetStartPos
     */
    void testSetStartPos() {
        byte[] b = toByteArray(51, 48);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setStartPos(0);
        assertEquals(mn.getStartPos(), 0);
    }


    /**
     * Test method for GetEndPos
     */
    void testGetEndPos() {
        byte[] b = toByteArray(61, 8);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        assertEquals(mn.getEndPos(), BLOCK_LENGTH);
    }

    /**
     * Test method for SetEndPos
     */
    void testSetEndPos() {
        byte[] b = toByteArray(9, 58);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setEndPos(BLOCK_LENGTH + (BLOCK_LENGTH / 2));
        assertEquals(mn.getEndPos(), BLOCK_LENGTH + (BLOCK_LENGTH / 2));
    }


    /**
     * Test method for IncrementCurPos
     */
    void testIncrementCurPos() {
        byte[] b = toByteArray(50, 88);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.incrementCurPos(16);
        assertEquals(mn.getBlockNumber(), BLOCK_LENGTH + 16);
    }

}
