import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 */

/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 *  This is a node class for the priority queue used in 
 *  multiwayMerge. Each node stores information corresponding
 *  to one of the eight blocks of the byte array such as 
 *  start, end, and current positions within the block,
 *  as well as a block ID number.
 */
class blockNode {
    
    /**
     * Static macro to hold the block length
     */
    static int blockLength = 8192;
    
    /**
     * Constructor for a merge node, which holds the data for the 
     * records being pulled from working memory
     * @param block the block the merge node corresponds to
     * @param arr the record value pulled from the block
     * @param cur the current place in the block
     * @param end the end of the data in that block
     */
    blockNode(int block, byte[] arr, int cur, int end){
        this.blockNumber = block;
        this.startPos = block * blockLength;
        this.endPos = end;
        byte[] keyBytes = Arrays.copyOfRange(arr, 8, 16);
        double keyVal = ByteBuffer.wrap(keyBytes).getDouble();
        this.key = keyVal;
        this.record = arr;
        this.curPos = cur;
    }
    
    /**
     * Gets the block number of this node
     * @return the block number of the corresponding node
     */
    public int getBlockNumber() {
        return blockNumber;
    }
    
    /**
     * Sets the block number of this node
     * @param block the block number to set this block to
     */
    public void setBlockNumber(int block) {
        blockNumber = block;
    }
    
    /**
     * Get the record value of this node to put into the priority queue
     * @return the record value of this node
     */
    public byte[] getRecord() {
        return record;
    }
    
    /** 
     * Get the current location of the node within the block
     * @return the current position of the node
     */
    public int getCurPos() {
        return curPos;
    }
    
    /**
     * Sets the current location of the node within the block
     * @param cur the location to set the node to
     */
    public void setCurPos(int cur) {
        curPos = cur;
    }
    
    /**
     * Gets the starting position of the node
     * @return the starting place of the block in working memory
     */
    public int getStartPos() {
        return startPos;
    }
    
    /**
     * Sets the starting position of the node
     * @param start the position to start the node at
     */
    public void setStartPos(int start) {
        startPos = start;
    }
    
    /**
     * Get the ending position of the node
     * @return the end position of the node within working memory
     */
    public int getEndPos() {
        return endPos;
    }
    
    /**
     * Set the ending position of the node
     * @param end the ending position value to set the node to
     */
    public void setEndPos(int end) {
        endPos = end;
    }
    
    /**
     * Increments the current position value by a specified amount
     * @param inc the value to increment the node by
     */
    public void incrementCurPos(int inc) {
        curPos += inc;
    }
    
    /**
     * Private variable holding the block corresponding to the node
     */
    private int blockNumber;
    /**
     * Private variable holding the node's current position
     */
    private int curPos;
    /**
     * Private variable holding the node's starting position
     */
    private int startPos;
    /**
     * Private variable holding the node's ending position
     */
    private int endPos;
    /**
     * Private variable holding the node's key value within the record
     */
    double key;
    /**
     * Private variable holding the node's entire record
     */
    private byte[] record;

}
/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 *  blockNodeComparator class
 *      Used in the priority queue 
 */
class blockNodeComparator implements Comparator<blockNode>{ 
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(blockNode o1, blockNode o2) {
        Double key1 = o1.key;
        Double key2 = o2.key;
        return key1.compareTo(key2);
    } 
} 
