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
    
    mergeNode(int block, byte[] arr){
        this.blockNumber = block;
        byte[] keyBytes = Arrays.copyOfRange(arr, 8, 16);
        double keyVal = ByteBuffer.wrap(keyBytes).getDouble();
        this.key = keyVal;
        this.record = arr;
        this.curPos = 0;
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
    
    public long getCurPos() {
        return curPos;
    }
    
    public void setCurPos(int cur) {
        curPos = cur;
    }
    
    public void incrementCurPos(int inc) {
        curPos += inc;
    }
    
    private int blockNumber;
    private int curPos;
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
