package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> lst = new BuggyAList<>();
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        lst.addLast(4);
        lst.addLast(5);
        lst.addLast(6);

        correct.addLast(4);
        correct.addLast(5);
        correct.addLast(6);

        assertEquals(correct.removeLast(), lst.removeLast());
        assertEquals(correct.removeLast(), lst.removeLast());
        assertEquals(correct.removeLast(), lst.removeLast());
    }

    @Test
    public void testRandomizedFunction() {
        AListNoResizing<Integer> L = new AListNoResizing<>();

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() > 0) {
                    int last = L.getLast();
                    System.out.println("getLast()" + last);
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() > 0) {
                    int last = L.removeLast();
                    System.out.println("getLast()" + last);
                }
            }
        }
    }

    @Test
    public void testRandomizedComparison() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0,4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                assertEquals(correct.size(), broken.size());
            } else if (operationNumber == 2) {
                // getLast
                if (correct.size() > 0) {
                    assertEquals(correct.getLast(), broken.getLast());
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (correct.size() > 0) {
                    assertEquals(correct.removeLast(), broken.removeLast());
                }
            }
        }

    }
}
