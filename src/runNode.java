/**
 * 
 */

/**
 * @author jmkuz
 *
 */
public class runNode {
    
    runNode(int run, long start, long end){
        runNumber = run;
        curPos = start;
        startPos = start;
        endPos = end;
    }
    
    public long getRunNumber() {
        return runNumber;
    }
    
    public void setRunNumber(int run) {
        runNumber = run;
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
    
    public long getStartPos() {
        return startPos;
    }
    
    public void setStartPos(int start) {
        startPos = start;
    }
    
    public long getEndPos() {
        return endPos;
    }
    
    public void setEndPos(int end) {
        endPos = end;
    }
    
    private int runNumber;
    private long curPos;
    private long startPos;
    private long endPos;

}
