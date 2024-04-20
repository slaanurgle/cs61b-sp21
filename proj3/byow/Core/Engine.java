package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import static byow.Core.Utils.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;

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
    private static final int ENTERINGCOMMAND = 9;
    private static final int TILESIZE = 16;
    private static final int UIHEIGHT = 3;
    private static final File SAVEFILE = new File("save1.txt");

    /** Current game condition */
    public int gameCond;
    private StartGUI starter;
    private World world;
    public long seed;
    public String buffer = "";
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initialGame();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                interactWithChar(StdDraw.nextKeyTyped());
            }
            if (gameCond == WORLD) {
                renderWorldUI();
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
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        initialGame();
        for (Character c : input.toCharArray()) {
            interactWithChar(c);
        }
        return world.worldTiles;
    }

    /** Helper method to deal with the characters of keyboard input or strings.
     *  The character will be changed to upper case. */
    private void interactWithChar(char ch) {
        ch = Character.toUpperCase(ch);
        switch(gameCond) {
            case MENU:
                inputMenuOption(ch);
                break;
            case ENTERINGSEED: // TODO: add a GUI for entering seed.
                if (ch == Shortcuts.ENDOFSEED) {
                    seed = Long.parseLong(buffer);
                    buffer = "";
                    world = new World(seed);
                    gameCond = WORLD;
                    //renderWorldUI();
                } else {
                    buffer += ch;
                }
                break;
            case WORLD:
                inputWorldOption(ch);
                break;
            case ENTERINGCOMMAND:
                if (ch == 'Q') {
                    saveWorld();
                    System.exit(0);
                } else {
                    gameCond = WORLD;
                }
        }
    }

    private void inputMenuOption(char ch) {
        switch(ch) {
            case Shortcuts.NEWGAME:
                gameCond = ENTERINGSEED;
                break;
            case Shortcuts.LOADGAME:
                loadWorld();
                gameCond = WORLD;
                break;
            case Shortcuts.QUIT:
                System.exit(0);
                break;
        }
    }

    private void inputWorldOption(char ch) {
        switch(ch) {
            case Shortcuts.MOVEUP:
                world.avatar.move(Unit.UP);
                break;
            case Shortcuts.MOVELEFT:
                world.avatar.move(Unit.LEFT);
                break;
            case Shortcuts.MOVEDOWN:
                world.avatar.move(Unit.DOWN);
                break;
            case Shortcuts.MOVERIGHT:
                world.avatar.move(Unit.RIGHT);
                break;
            case Shortcuts.EXITCOMMAND:
                gameCond = ENTERINGCOMMAND;
                break;

        }
    }

    /** Initialize the game, set some variables and prepare the canvas */
    private void initialGame() {
//        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
//        StdDraw.setXscale(0, WIDTH);
//        StdDraw.setYscale(0, HEIGHT);
//        StdDraw.clear(Color.BLACK);
//        StdDraw.enableDoubleBuffering();
        ter.initialize(WIDTH, HEIGHT + UIHEIGHT);
        starter = new StartGUI();
        gameCond = MENU;
    }

    private void renderWorldUI() {
        StdDraw.clear(StdDraw.BLACK);
        Pos mousePos = getMousePosition();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, TILESIZE - 1));
        ter.renderFrame(world.worldTiles);
        if (mouseInTiles(mousePos)) {
            renderUI(mousePos);
        }
        StdDraw.show();
    }

    private void renderUI(Pos mousePos) {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 16));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(3, HEIGHT + UIHEIGHT - 1, getMouseInfo(mousePos));
    }

    public Pos getMousePosition() {
        return new Pos((int) Math.floor(StdDraw.mouseX()), (int) Math.floor(StdDraw.mouseY()));
    }

    /** Get the info of the tile which is pointed by the mouse. */
    private String getMouseInfo(Pos pos) {
        return world.worldTiles[pos.x][pos.y].description();
    }

    private boolean mouseInTiles(Pos pos) {
        double mousex = pos.x;
        double mousey = pos.y;
        if (mousex >= 0 && mousex < WIDTH && mousey >= 0 && mousey < HEIGHT) {
            return true;
        }
        return false;
    }

    /** Save the current world */
    public void saveWorld() {
        if (!SAVEFILE.exists()) {
            safetyCreate(SAVEFILE);
        }
        writeObject(SAVEFILE, world);
    }

    /** Load a saved world and change game condition to WORLD */
    public void loadWorld() {
        if (!SAVEFILE.exists()) {
            // TODO: add a message to tell there are no saving.
            return;
        }
        world = readObject(SAVEFILE, World.class);
        world.ter = new TERenderer();
        gameCond = WORLD;
    }
    @Override
    public String toString() {
        return TETile.toString(world.worldTiles);
    }
}
