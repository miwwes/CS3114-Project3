import student.TestCase;
import java.nio.ByteBuffer;


/**
 * @author juliam8
 * @author abbym1
 * @version 4-4-19
 * 
 * This is the testing class for BlockNode
 *
 */
public class BlockNodeTest extends TestCase {
    
    /**
     * Named constant for the length of one block
     */
    public final static int BLOCK_LENGTH = 8192;
    
    /**
     * Helper function to convert a long and a double into a record byte array
     * @param id the ID for the record
     * @param key the key for the record
     * @return the key value
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
     * Test method for MergeNode constructor
     */
    public void testMergeNode() {
        byte[] b = toByteArray(10, 4);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        assertEquals(mn.getBlockNumber(), 0);
        assertEquals(mn.key(), 4, 0.1);
    }


    /**
     * Test method for getBlockNumber function
     */
    public void testGetBlockNumber() {
        byte[] b = toByteArray(1, 48);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getBlockNumber(), 1);
    }


    /**
     * Test method for setBlockNumber function
     */
    public void testSetBlockNumber() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setBlockNumber(4);
        assertEquals(mn.getBlockNumber(), 4);
    }


    /**
     * Test method for getRecord function
     */
    public void testGetRecord() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getRecord(), b);
    }


    /**
     * Test method for getCurPos function
     */
    public void testGetCurPos() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getCurPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for setCurPos function
     */
    public void testSetCurPos() {
        byte[] b = toByteArray(5, 28);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        mn.setCurPos(BLOCK_LENGTH);
        assertEquals(mn.getCurPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for getStartPos function
     */
    public void testGetStartPos() {
        byte[] b = toByteArray(51, 8);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        assertEquals(mn.getStartPos(), BLOCK_LENGTH);
    }


    /**
     * Test method for setStartPos function
     */
    public void testSetStartPos() {
        byte[] b = toByteArray(51, 48);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setStartPos(0);
        assertEquals(mn.getStartPos(), 0);
    }


    /**
     * Test method for getEndPos function
     */
    public void testGetEndPos() {
        byte[] b = toByteArray(61, 8);
        BlockNode mn = new BlockNode(0, b, 0, BLOCK_LENGTH);
        assertEquals(mn.getEndPos(), BLOCK_LENGTH);
    }

    /**
     * Test method for setEndPos function
     */
    public void testSetEndPos() {
        byte[] b = toByteArray(9, 58);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.setEndPos(BLOCK_LENGTH + (BLOCK_LENGTH / 2));
        assertEquals(mn.getEndPos(), BLOCK_LENGTH + (BLOCK_LENGTH / 2));
    }


    /**
     * Test method for incrementCurPos function
     */
    public void testIncrementCurPos() {
        byte[] b = toByteArray(50, 88);
        BlockNode mn = new BlockNode(1, b, BLOCK_LENGTH, BLOCK_LENGTH * 2);
        mn.incrementCurPos(16);
        assertEquals(mn.getCurPos(), BLOCK_LENGTH + 16);
    }

}
