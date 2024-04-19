package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import static byow.Core.Shortcuts.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.LinkedList;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final String GAMENAME = "CS61B: THE GAME";
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    /* The game Conditions */
    private static final int MENU = 1;
    private static final int ENTERINGSEED = 2;
    private static final int WORLD = 3;


    /** Current game condition */
    public int gameCond;
    private StartGUI starter;
    public long seed;
    private LinkedList<Character> keyBuffer = new LinkedList<>();
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initialGame();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                interactWithChar(ch);
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        TETile[][] finalWorldFrame = null;
        initialGame();
        for (Character c : input.toCharArray()) {
            keyBuffer.addLast(c);
        }
        while (hasKeyBuffer()) {
            char c = nextKey();
            interactWithChar(c);
        }
        return finalWorldFrame;
    }

    /** Helper method to deal with the characters of keyboard input or strings.
     *  The character will be changed to upper case. */
    private TETile[][] interactWithChar(char ch) {
        ch = Character.toUpperCase(ch);
        switch(gameCond) {
            case MENU:
                inputMenuOption(ch);
                break;
            case ENTERINGSEED:
                solicitSeed();
                World world = new World(seed);
                gameCond = WORLD;
                break;
            case WORLD:

                break;
        }
        return null;
    }

    public void inputMenuOption(char ch) {
        switch(ch) {
            case NEWGAME:
                gameCond = ENTERINGSEED;
                break;
            case LOADGAME:
                // TODO
                break;
            case QUIT:
                // TODO
                break;
        }
    }

    /** Solicit a seed */
    private void solicitSeed() {
        String seedString = "";
        char ch;
        while (true) {
            if (hasKeyBuffer()) {
                ch = nextKey();
                if (ch == ENDOFSEED) {
                    seed = Long.parseLong(seedString);
                    return;
                }
                seedString += ch;
            }

        }

    }

    /** Initialize the game, set some variables and prepare the canvas */
    private void initialGame() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        starter = new StartGUI();
        gameCond = MENU;
    }

    private boolean hasKeyBuffer() {
        return !keyBuffer.isEmpty();
    }

    private char nextKey() {
        return keyBuffer.removeFirst();
    }
}
