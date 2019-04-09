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
public class RunNode {
    
    /**
     * Constructor for a node class that holds the run data
     * @param run the run number corresponding to this run
     * @param start the starting place in the file of this run
     * @param end the ending position in the file of this run
     * @param merged whether this run was a result of a multiway merge
     */
    RunNode(int run, long start, long end, boolean merged) {
        runNumber = run;
        curPos = start;
        startPos = start;
        endPos = end;
        hasBeenMerged = merged;
    }
    
    /**
     * Returns whether this run was a result of a multiway merge
     * @return true if this run was merged, false if not
     */
    public boolean gotMerged() {
        return hasBeenMerged;
    }
    
    /**
     * Sets whether this run needs to be merged again
     * @param isMerged sets whether this run was merged
     */
    public void setMerged(boolean isMerged) {
        hasBeenMerged = isMerged;
    }
    
    /**
     * Gets the run number of this run node
     * @return the value of the run number
     */
    public long getRunNumber() {
        return runNumber;
    }
    
    /**
     * Sets the run value of this run node
     * @param run the number to set it to
     */
    public void setRunNumber(int run) {
        runNumber = run;
    }
    
    /**
     * Get the current position within the run within the file
     * @return the current position within the file
     */
    public long getCurPos() {
        return curPos;
    }
    
    /**
     * Sets the current position within the run within the file
     * @param l the location within the file
     */
    public void setCurPos(long l) {
        curPos = l;
    }
    
    /**
     * Increment the current position by a certain amount
     * @param inc the value to increment the current position by
     */
    public void incrementCurPos(int inc) {
        curPos += inc;
    }
    
    /**
     * Get the starting position of the run
     * @return the starting position of the run within the file
     */
    public long getStartPos() {
        return startPos;
    }
    
    /**
     * Set the starting position of the run within the file
     * @param start the value to set the starting position to
     */
    public void setStartPos(int start) {
        startPos = start;
    }
    
    /**
     * Get the ending position of the run
     * @return the ending position of the run within the file
     */
    public long getEndPos() {
        return endPos;
    }
    
    /**
     * Sets the ending position of the run node within the file
     * @param end the ending position to set
     */
    public void setEndPos(int end) {
        endPos = end;
    }
    
    
    /**
     * whether this run has been merged
     */
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
