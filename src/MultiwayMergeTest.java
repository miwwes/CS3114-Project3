import java.io.IOException;
import java.io.RandomAccessFile;
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
public class MultiwayMergeTest extends TestCase{

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
        assertTrue(c.getInputBuffer().empty());
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
        assertTrue(sc.getHeap().empty());
        assertTrue(sc.getInputBuffer().empty());
        assertTrue(sc.getOutputBuffer().empty());
    }


}
