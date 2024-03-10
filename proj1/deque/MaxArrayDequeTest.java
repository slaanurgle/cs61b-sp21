package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeTest {
    public class intComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);
        }
    }

    public class strComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return  a.compareTo(b);
        }
    }

    public class antiStrComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return  b.compareTo(a);
        }
    }
    @Test
    public void intCompareTest() {
        Comparator c = new intComparator();
        MaxArrayDeque<Integer> mad1 = new MaxArrayDeque<>(c);
        mad1.addFirst(3);
        mad1.addLast(5);
        mad1.addFirst(2);
        mad1.addLast(1);
        int maxItem = mad1.max();
        assertEquals(5, maxItem);


    }

    @Test
    public void comparatorsTest() {
        Comparator c1 = new strComparator();
        Comparator c2 = new antiStrComparator();
        MaxArrayDeque<String> mad1 = new MaxArrayDeque<>(c1);
        mad1.addFirst("apple");
        mad1.addFirst("banana");
        mad1.addLast("cherry");
        String maxItem = mad1.max();
        String minItem = mad1.max(c2);
        assertEquals("cherry", maxItem);
        assertEquals("apple", minItem);
    }
}
