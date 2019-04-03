import java.util.Arrays;

/**
 * 
 */

/**
 * @author amalone46
 *
 */
public class buffer {
    
    public buffer() {
        byteArray = new byte[BUFFER_SIZE];
        size = 0;
        pos = 0;
    }
    
    public void loadBlock(byte[] b) {
        byteArray = b;
        size = 0;
        pos = 0;
    }
    
    public void insert(byte[] b) {
        if( !full() ) {
            System.arraycopy(b, 0, byteArray, pos, 16);
            pos += 16;
            size++;
        }
    }
    
    public byte[] remove() {
        byte[] last = null;
        if( !empty() ) {
            last = Arrays.copyOfRange(byteArray, pos, pos + 16);
            pos -= 16;
            size--;
        }
        return last;
    }
    
    public boolean full() {
        return size * 16 == BUFFER_SIZE;
    }
    
    public boolean empty() {
        return size == 0;
    }

    private static final int BUFFER_SIZE = 8192;

    private byte[] byteArray;
    private int size;
    private int pos;
}
