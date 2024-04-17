package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class StartGUI {
    /** The options of the MENU */
    public static final char NEWGAME = 'N';
    public static final char LOADGAME = 'L';
    public static final char QUIT = 'Q';
    public int cond;
    public static final int ENTERINGSEED = 1;
    public static final int MENU = 0;
    public int seed;
    private String seedInput = "";

    public StartGUI() {
        cond = MENU;
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(Engine.WIDTH * 0.5, Engine.HEIGHT * 0.8, Engine.GAMENAME);
        font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(Engine.WIDTH * 0.5, Engine.HEIGHT * 0.55, "New Game (" + NEWGAME + ")");
        StdDraw.text(Engine.WIDTH * 0.5, Engine.HEIGHT * 0.50, "Load Game (" + LOADGAME + ")");
        StdDraw.text(Engine.WIDTH * 0.5, Engine.HEIGHT * 0.45, "Quit (" + QUIT + ")");
        StdDraw.show();
    }


    public void inputOption(char ch) {
        switch(cond) {
            case MENU:
                switch(ch) {
                    case NEWGAME:
                        cond = ENTERINGSEED;
                        break;
                    case LOADGAME:
                        // TODO
                        break;
                    case QUIT:
                        // TODO
                        break;
                }
                break;
            case ENTERINGSEED:
                if (inputSeed(ch)) {
                    World world = new World(seed);
                }
                break;
        }

    }

    /** Input one character of the seed, return true if read the end of seed. */
    private boolean inputSeed(char ch) {
        // TODO: add a GUI for entering
        if (Character.isDigit(ch)) {
            seedInput += ch;
        } else if (ch == 'S') {
            seed = Integer.parseInt(seedInput);
            return true;
        }
        return false;
    }
}
