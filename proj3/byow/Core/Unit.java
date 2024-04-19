package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import static byow.Core.RandomUtils.*;

public class Unit {
    public int posx;
    public int posy;
    public World world;

    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;
    public static final int RIGHT = 4;
    public Unit(World w) {
        world = w;
        TETile[][] tiles = world.worldTiles;
        Pos[] floorTiles = world.getFloorTiles();
        shuffle(world.rand, floorTiles);
        Pos objPos = floorTiles[0];
        posx = objPos.x;
        posy = objPos.y;
        tiles[posx][posy] = Tileset.AVATAR;

    }

    /** move the unit one tile in DIR */
    public void move(int dir) {
        TETile[][] tiles = world.worldTiles;
        Pos nextPos = nextTile(new Pos(posx, posy), dir);
        // move only if the next tile is FLOOR
        if (tiles[nextPos.x][nextPos.y].equals(Tileset.FLOOR)) {
            tiles[posx][posy] = Tileset.FLOOR;
            posx = nextPos.x;
            posy = nextPos.y;
            tiles[posx][posy] = Tileset.AVATAR;
            world.renderWorld();
        }

    }

    public Pos nextTile(Pos pos, int dir) {
        switch (dir) {
            case UP:
                return new Pos(pos.x, pos.y + 1);
            case LEFT:
                return new Pos(pos.x - 1, pos.y);
            case DOWN:
                return new Pos(pos.x, pos.y - 1);
            case RIGHT:
                return new Pos(pos.x + 1, pos.y);
        }
        return null;
    }

}
