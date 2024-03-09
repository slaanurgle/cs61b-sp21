package deque;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.princeton.cs.algs4.StdRandom;

import java.lang.reflect.Array;

public class randomizedCompareTest {
    @Test
    public void comparingTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        int N = 1000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 7);
            if (operationNumber == 0) {
                // addFirst
                int randomVal = StdRandom.uniform(0, 100);
                ad.addFirst(randomVal);
                lld.addFirst(randomVal);
                System.out.println("addFirst(" + randomVal + ")");
            } else if (operationNumber == 1) {
                // addLast
                int randomVal = StdRandom.uniform(0, 100);
                ad.addLast(randomVal);
                lld.addLast(randomVal);
                System.out.println("addLast(" + randomVal + ")");
            } else if (operationNumber == 2) {
                // removeFirst
                if (!ad.isEmpty()) {
                    assertEquals("removeFirst()", lld.removeFirst(), ad.removeFirst());
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (!ad.isEmpty()) {
                    assertEquals("removeLast()", lld.removeLast(), ad.removeLast());
                }
            } else if (operationNumber == 4) {
                // isEmpty
                assertEquals("isEmpty()", lld.isEmpty(), ad.isEmpty());
                System.out.println("isEmpty()");
            } else if (operationNumber == 5) {
                // size
                assertEquals("size()", lld.size(), ad.size());
                System.out.println("size()");
            } else if (operationNumber == 6) {
                // get
                if (!lld.isEmpty()) {
                    int randomVal = StdRandom.uniform(0, lld.size());
                    assertEquals("get(" + randomVal + ")", lld.get(randomVal), ad.get(randomVal));
                }
            }
        }
    }

}
