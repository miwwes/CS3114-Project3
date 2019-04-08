import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 */

/**
 * @author jmkuz
 *
 */
class mergeNode {
    
    static int blockLength = 8192;
    
    mergeNode(int block, byte[] arr, int cur, int end){
        this.blockNumber = block;
        this.startPos = block * blockLength;
        this.endPos = end;
        byte[] keyBytes = Arrays.copyOfRange(arr, 8, 16);
        double keyVal = ByteBuffer.wrap(keyBytes).getDouble();
        this.key = keyVal;
        this.record = arr;
        this.curPos = cur;
    }
    
    public int getBlockNumber() {
        return blockNumber;
    }
    
    public void setBlockNumber(int block) {
        blockNumber = block;
    }
    
    public byte[] getRecord() {
        return record;
    }
    
    public double key() {
        return key;
    }
    
    public int getCurPos() {
        return curPos;
    }
    
    public void setCurPos(int cur) {
        curPos = cur;
    }
    
    public int getStartPos() {
        return startPos;
    }
    
    public void setStartPos(int start) {
        startPos = start;
    }
    
    public int getEndPos() {
        return endPos;
    }
    
    public void setEndPos(int end) {
        endPos = end;
    }
    
    public void incrementCurPos(int inc) {
        curPos += inc;
    }
    
    private int blockNumber;
    private int curPos;
    private int startPos;
    private int endPos;
    double key;
    private byte[] record;

}

class mergeNodeComparator implements Comparator<mergeNode>{ 
    

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(mergeNode o1, mergeNode o2) {
        Double key1 = o1.key;
        Double key2 = o2.key;
        return key1.compareTo(key2);
    } 
} 
