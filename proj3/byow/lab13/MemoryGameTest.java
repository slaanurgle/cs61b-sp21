package byow.lab13;

import org.junit.Test;
import org.junit.Assert;
import byow.lab13.MemoryGame;
import edu.princeton.cs.introcs.StdDraw;
public class MemoryGameTest {
    @Test
    public void randomStringTest() {
        MemoryGame newGame = new MemoryGame(40, 40, 333333);
        String randomString = newGame.generateRandomString(5);
        System.out.println(randomString);
    }

    @Test
    public void frameTest() {
        MemoryGame newGame = new MemoryGame(40, 40, 333333);
        newGame.drawFrame("Hello");
        while (!StdDraw.mousePressed()) {

        }
    }

    @Test
    public void flashSequenceTest() {
        MemoryGame newGame = new MemoryGame(40, 40, 333333);
        newGame.flashSequence("Hello");
    }

    @Test
    public void solicitInputTest() {
        MemoryGame newGame = new MemoryGame(40, 40, 333333);
        String stringInput = newGame.solicitNCharsInput(5);
        System.out.println(stringInput);
    }
}
