
// On my honor:
//
// -I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// -All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// -I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
//
// Signed: Abagale Rane Malone, Julia Mahon Kuzin


import java.io.*;
import java.util.LinkedList;

/**
 * @author abbym1 - Abagale Malone (abbym1@vt.edu)
 * @author juliam8 - Julia Mahon Kuzin (juliam8@vt.edu)
 * @version 2019-04-10
 *          This is the main class for CS3114 Project 3
 *          
 *          ~Description~    
 *          
 */
public class Externalsort {

    /**
     * This is the main function of project 3. It acts as the starting
     *      point for the above mentioned functionality.
     * Compiler: javac
     * JDK: 11.0.2
     * OS: Windows 10
     * Completed: _________________
     * 
     * @param args
     *            input string array
     * @throws FileNotFoundException
     * 
     */
    public static void main(String[] args) {
        final int HEAP_SIZE = 8*8192;
        final int MAX_REC_HEAP = 4096;

        try {
            RandomAccessFile in = new RandomAccessFile(args[0], "rw");
            
            
            File newFile = new File("output.bin");
            newFile.createNewFile();
            RandomAccessFile runs = new RandomAccessFile(newFile, "rw");
                        
            byte[] heapArray = new byte[HEAP_SIZE];
            in.read(heapArray);
            minHeap h = new minHeap(heapArray, MAX_REC_HEAP, MAX_REC_HEAP);
            h.buildHeap();
            
            LinkedList<runNode> l = new LinkedList<runNode>();
            
            replacementSelection rSel = new replacementSelection(in, runs, l, h);
            rSel.execute();
            
            multiwayMerge mMerge = new multiwayMerge(in, runs, l, h);
            
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
