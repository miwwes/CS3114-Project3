
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
     * @throws IOException 
     * @throws FileNotFoundException
     * 
     */
    public static void main(String[] args) throws IOException {
            
        sortContainer extSort = new sortContainer(args[0]);
        
        replacementSelection rSel = new replacementSelection(extSort);
        rSel.execute();
        
        multiwayMerge mMerge = new multiwayMerge(extSort);
        mMerge.execute();

    }
}
