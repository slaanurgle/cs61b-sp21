package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import static byow.Core.Shortcuts.*;
import java.awt.*;

public class StartGUI {
    public StartGUI() {
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
}
