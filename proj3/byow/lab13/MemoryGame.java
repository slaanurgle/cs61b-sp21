package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
    }

    /** Generate random string of letters of length n */
    public String generateRandomString(int n) {
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < n; i += 1) {
            char randomChar = CHARACTERS[rand.nextInt(26)];
            returnString.append(randomChar);
        }
        return returnString.toString();
    }

    /** Clear the current screen and draw S in the center of it */
    public void drawFrame(String s) {
        /* Take the string and display it in the center of the screen */
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width/2.0, height/2.0, s);

        //If game is not over, display relevant game information at the top of the screen
        StdDraw.line(0, height * 0.94, width, height * 0.94);
        StdDraw.textLeft(0, height * 0.96, "Round: " + round);
        String turnInfo;
        if (playerTurn) {
            turnInfo = "Type!";
        } else {
            turnInfo = "Watch!";
        }
        StdDraw.text(width/2.0, height * 0.96, turnInfo);
        StdDraw.textRight(width, height * 0.96, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        /* Display each character in letters, making sure to blank the screen between letters */
        for (Character ch : letters.toCharArray()) {
            drawFrame(ch.toString());
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        playerTurn = true;
        /* Read n letters of player input */
        drawFrame("");
        int numKeyTyped = 0;
        StringBuilder stringTyped = new StringBuilder();
        while (numKeyTyped < n) {
            if (StdDraw.hasNextKeyTyped()) {
                String keyTyped = ((Character) StdDraw.nextKeyTyped()).toString();
                stringTyped.append(keyTyped);
                drawFrame(stringTyped.toString());
                numKeyTyped += 1;
            }
        }
        playerTurn = false;
        return stringTyped.toString();
    }

    public void startGame() {
        round = 0;
        /* Establish Engine loop */
        do {
            round += 1;
            drawFrame("Round: " + round);
            StdDraw.pause(2000);
            String targetString = generateRandomString(round);
            flashSequence(targetString);
            String enteredString = solicitNCharsInput(round);
            gameOver = !targetString.equals(enteredString);
        } while (!gameOver);
        drawFrame("Game Over! You made it to round: " + round);
    }
}
