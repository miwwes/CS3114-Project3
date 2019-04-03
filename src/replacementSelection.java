import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author amalone46
 *
 */
public class replacementSelection {

    public replacementSelection(RandomAccessFile raf){
        inFile = raf;
        inputBuffer = new byte[BUFFER_SIZE];
        outputBuffer = new byte[BUFFER_SIZE];
    }
    
    
    public void execute() {
        try {
            numWords = inFile.readByte();
            
            for (int pos = 0; pos < numWords; pos++) {
             // Get the offset of the first word:
             wordOffset = inFile.readShort();
             // Save offset to return for next offset:
             long offsetOfNextOffset = inFile.getFilePointer();
             // Go to that position.
             inFile.seek(wordOffset);
             // Get the length of the word:
             wordLength = inFile.readByte();
             // Get the word (in ASCII):
             byte Word[] = new byte[wordLength];
             inFile.read(Word);
             // Make Java happy:
             String sWord = new String(Word);
             inFile.seek(offsetOfNextOffset);
            }
            inFile.close();
        }
        catch (IOException e) {
            System.err.println("IO error: " + e);
        }
    }
    
    private static final int BUFFER_SIZE = 8192;
    
    MinHeap records;
    byte[] inputBuffer;
    byte[] outputBuffer;
    
    RandomAccessFile inFile;
    RandomAccessFile outFile;
    byte numWords = 0;
    short wordOffset = 0;
    byte wordLength = 0;
}
