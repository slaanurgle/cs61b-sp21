package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    public void manyNumbersTest() {
        for (int i = 0; i < 1000; i += 1) {
            int j = i;
            Integer x = (Integer) i;
            Integer y = (Integer) j;
            assertTrue(i == j);
            assertTrue(x.toString() + " " + y.toString(), Flik.isSameNumber(i, j));
        }
    }
}
