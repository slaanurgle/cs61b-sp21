package deque;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayDequeTest {
    @Test
    public void basicAddFirstLastTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addLast(5);
        assertEquals(3, ad.size());
        ad.printDeque();
    }

    @Test
    public void beyondLengthAddTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(5);
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addLast(6);
        ad.addFirst(2);
        ad.addFirst(1);
        ad.addFirst(0);
        assertEquals(7, ad.size());
        ad.printDeque();
    }

    @Test
    public void resizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        for (int i = 0; i < 20; i += 1) {
            ad1.addFirst(i);
            ad2.addLast(i);
        }
        return;
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i += 1) {
            ad1.addFirst(i);
            ad1.addLast(i);
        }
        for (int j = 0; j < 40; j += 1) {
            ad1.removeFirst();
        }
        assertEquals(0, ad1.size());
    }

    @Test
    public void removeLastGetTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i += 1) {
            ad1.addLast(i);
        }
        for (int j = 0; j < 10; j += 1) {
            int m = ad1.get(j);
            assertEquals(j, m);
        }
        for (int i = 0; i < 4; i += 1) {
            ad1.removeLast();
            ad1.printDeque();
        }
    }

    @Test
    public void iterationTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i += 1) {
            ad1.addLast(i);
        }
        int i = 0;
        for (int item : ad1) {
            assertEquals(i, item);
            i += 1;
        }

    }

    @Test
    public void emptyIteration() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int item : ad1) {
            System.out.println(item);
        }
    }
}
