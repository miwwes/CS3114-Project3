
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

/**
 * @author abbym1 - Abagale Malone (abbym1@vt.edu)
 * @author juliam8 - Julia Mahon Kuzin (juliam8@vt.edu)
 * @version 2019-04-10
 *          This is the main class for CS3114 Project 3
 *          
 *          This project implements external sorting using replacement
 *          selection, a min heap, and multi-way merging.
 *          
 *          The input is a binary file containing an unknown number 
 *          of 16 byte records composed of an 8 byte long record ID 
 *          value then an 8 byte double key value, by which the records 
 *          will be sorted.
 *          
 *          The first step is replacement selection using an array
 *          based min heap that can hold 8 blocks of data, equal to
 *          8192 records. Two buffers are used that each hold one 
 *          block of data, or 512 records. This process partially sorts 
 *          the data into runs that are printed in a run file, and tracked
 *          in a linked list.
 *          
 *          The final step is 8-way multi-way merging, using the 8-block
 *          byte array used to implement the heap. One buffer is used, as
 *          well as two linked lists to keep track of file positions and 
 *          run information. This process completely sorts the data, and 
 *          prints the results to standard output as well as the original
 *          input file.
 */
public class Externalsort {

    /**
     * This is the main function of project 3. It acts as the starting
     * point for the above mentioned functionality.
     * Compiler: javac
     * JDK: 11.0.2
     * OS: Windows 10
     * Completed: 4/9/2019
     * 
     * @param args
     *            input string array
     * @throws IOException 
     * 
     */
    public static void main(String[] args) throws IOException {
        
        extSort = new SortContainer(args[0]);
        
        ReplacementSelection rSel = new ReplacementSelection(extSort);
        rSel.execute();

        MultiwayMerge mMerge = new MultiwayMerge(extSort);
        mMerge.execute();
        
        extSort.inFile().close();
        extSort.runsFile().close();

    }
    
    /**
     * @return the sort container to provide access 
     * to the heap array, input and output files, etc
     * for testing purposes
     */
    public static SortContainer getSortContainer() {
        return extSort;
    }
    
    /**
     * This object stores all data and structures necessary
     * for both replacement selection and multi-way merge
     */
    private static SortContainer extSort;
}
