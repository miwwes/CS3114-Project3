import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * @author abbym1
 * @author juliam8
 * @version 03-21-19
 * 
 *      Main class for CS3114 Project 2
 *
 */
public class Externalsort {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            RandomAccessFile raf = new RandomAccessFile(args[0], "r");
            replacementSelection rSel = new replacementSelection(raf);
        }

    }

}
