/**
 * 
 */

/**
 * @author juliam8
 * @author abbym1
 * @version 4-4-19
 * 
 *      This is a node class used in the linked list defined in 
 *      Externalsort.java. It stores data about each run so that 
 *      the records can be processed (multiway merged) a block
 *      at a time including which run it is, and the start, end 
 *      and current position in the run.
 *
 */
public class runNode {
    
    /**
     * @param run
     * @param start
     * @param end
     */
    runNode(int run, long start, long end, boolean merged){
        runNumber = run;
        curPos = start;
        startPos = start;
        endPos = end;
        hasBeenMerged = merged;
    }
    
    /**
     * @return
     */
    public boolean getMerged() {
        return hasBeenMerged;
    }
    
    /**
     * @return
     */
    public long getRunNumber() {
        return runNumber;
    }
    
    
    /**
     * @param run
     */
    public void setRunNumber(int run) {
        runNumber = run;
    }
    
    /**
     * @return
     */
    public long getCurPos() {
        return curPos;
    }
    
    /**
     * @param l
     */
    public void setCurPos(long l) {
        curPos = l;
    }
    
    /**
     * @param inc
     */
    public void incrementCurPos(int inc) {
        curPos += inc;
    }
    
    /**
     * @return
     */
    public long getStartPos() {
        return startPos;
    }
    
    /**
     * @param start
     */
    public void setStartPos(int start) {
        startPos = start;
    }
    
    /**
     * @return
     */
    public long getEndPos() {
        return endPos;
    }
    
    /**
     * @param end
     */
    public void setEndPos(int end) {
        endPos = end;
    }
    
    
    private boolean hasBeenMerged;
    /**
     * identifying number in the sequence of runs
     */
    private int runNumber;
    /**
     * keeps track of your current position in the run
     */
    private long curPos;
    /**
     * keeps track of where the run begins
     */
    private long startPos;
    /**
     * keeps track of where the run ends
     */
    private long endPos;

}
