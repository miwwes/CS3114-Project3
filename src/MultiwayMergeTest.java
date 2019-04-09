import java.io.IOException;
import student.TestCase;

/**
 * 
 */

/**
 * @author juliam8
 * @author abbym1
 * @version 2019-04-10
 *
 *  Testing class for the MultiwayMerge class
 */
public class MultiwayMergeTest extends TestCase {

    /**
     * Test method for {@link MultiwayMerge#MultiwayMerge(SortContainer)}.
     * @throws IOException 
     */
    public void testMultiwayMerge() throws IOException {
        String[] args = {"mm_test.bin", "10"};
        Genfile.main(args);
        SortContainer c = new SortContainer("mm_test.bin");
        ReplacementSelection rSel = new ReplacementSelection(c);
        rSel.execute();

        MultiwayMerge mMerge = new MultiwayMerge(c);
        assertEquals(mMerge.numberOfRuns, 2);
        assertNull(mMerge.pq);
        assertTrue(c.inputBuffer().empty());
    }


    /**
     * Test method for {@link MultiwayMerge#execute()}.
     * @throws IOException 
     */
    public void testExecute() throws IOException {
        String[] args = {"mm_test2.bin", "10"};
        Genfile.main(args);
        SortContainer sc = new SortContainer("mm_test2.bin");
        ReplacementSelection rSel = new ReplacementSelection(sc);
        rSel.execute();

        MultiwayMerge mMerge = new MultiwayMerge(sc);
        mMerge.execute();
        assertTrue(mMerge.pq.isEmpty());
        assertTrue(mMerge.runs.isEmpty());
        assertTrue(sc.inputBuffer().empty());
        assertTrue(sc.outputBuffer().empty());
        
        String[] args2 = {"mm_test3.bin", "130"};
        Genfile.main(args2);
        SortContainer sc2 = new SortContainer("mm_test3.bin");
        ReplacementSelection rSel2 = new ReplacementSelection(sc2);
        rSel2.execute();

        MultiwayMerge mMerge2 = new MultiwayMerge(sc2);
        mMerge2.execute();
        assertTrue(mMerge2.pq.isEmpty());
        assertTrue(mMerge2.runs.isEmpty());
        assertTrue(sc.inputBuffer().empty());
        assertTrue(sc.outputBuffer().empty());
    }


}
