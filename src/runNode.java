/**
 * 
 */

/**
 * @author jmkuz
 *
 */
public class runNode {
    
    runNode(int run, int cur, int start, int end){
        runNumber = run;
        curPos = cur;
        startPos = start;
        endPos = end;
    }
    
    public int getRunNumber() {
        return runNumber;
    }
    
    public void setRunNumber(int run) {
        runNumber = run;
    }
    
    public int getCurPos() {
        return curPos;
    }
    
    public void setCurPos(int cur) {
        curPos = cur;
    }
    
    public void incrementCurPos(int inc) {
        curPos += inc;
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
    
    private int runNumber;
    private int curPos;
    private int startPos;
    private int endPos;

}
