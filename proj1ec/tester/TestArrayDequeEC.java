package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;
public class TestArrayDequeEC {
    @Test
    public void comparingTest() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addFirst
                int randomVal = StdRandom.uniform(0, 100);
                sad.addFirst(randomVal);
                ads.addFirst(randomVal);
                System.out.println("addFirst(" + randomVal + ")");
            } else if (operationNumber == 1) {
                // addLast
                int randomVal = StdRandom.uniform(0, 100);
                sad.addLast(randomVal);
                ads.addLast(randomVal);
                System.out.println("addLast(" + randomVal + ")");
            } else if (operationNumber == 2) {
                // removeFirst
                if (!sad.isEmpty()) {
                    assertEquals("removeFirst()", ads.removeFirst(), sad.removeFirst());
                }
            } else if (operationNumber == 3) {
                // removeLast
                if (!sad.isEmpty()) {
                    assertEquals("removeLast()", ads.removeLast(), sad.removeLast());
                }
            } else if (operationNumber == 4) {
                // isEmpty
                assertEquals("isEmpty()", ads.isEmpty(), sad.isEmpty());
                System.out.println("isEmpty()");
            } else if (operationNumber == 5) {
                // size
                assertEquals("size()", ads.size(), sad.size());
                System.out.println("size()");
            }
        }
    }

}
