package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final int HEXASIZE = 3;
    private static final int WORLDSIZE = 3;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static void addHexagon(TETile[][] world, int xpos, int ypos, int size, TETile tile) {
        // add pixels by rows from bottom to up.
        int row = ypos;
        int rowStart = xpos;
        int len = size;
        // add the half of the bottom first
        for (int i = 0; i < size; i += 1, row += 1, len += 2, rowStart -= 1) {
            addRow(world, row, rowStart, len, tile);
        }
        len -= 2;
        rowStart += 1;
        for (int i = 0; i < size; i += 1, row += 1, len -= 2, rowStart += 1) {
            addRow(world ,row, rowStart, len, tile);
        }
    }

    private static void addRow(TETile[][] world, int row, int start, int len, TETile tile) {
        int end = start + len;
        for (int x = start; x < end; x += 1) {
            world[x][row] = tile;
        }
    }

    private static void addTesselation(TETile[][] world, int hexaSize) {
        // repeating adding a bottom-left slash of hexagons
        int len = WORLDSIZE;
        int xStart = 5 * hexaSize - 2;
        int yStart = 0;
        for (int i = 0; i < WORLDSIZE - 1; i += 1,
                xStart -= 2 * hexaSize - 1, yStart += hexaSize, len += 1) {
            addHexaDiagonalUp(world, xStart, yStart, len, hexaSize);
        }
        for (int i = 0; i < WORLDSIZE; i += 1, yStart += 2 * hexaSize, len -= 1) {
            addHexaDiagonalUp(world, xStart, yStart, len, hexaSize);
        }
    }

    private static void addHexaDiagonalUp
            (TETile[][] world, int xstart, int ystart, int len, int hexaSize) {
        for (int i = 0; i < len; i += 1, xstart += 2 * hexaSize - 1, ystart += hexaSize) {
            addHexagon(world, xstart, ystart, hexaSize, randomTile());
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.FLOOR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        // add hexagons to the world.
        //addRow(world, 5, 3, 5, Tileset.FLOWER);
        addTesselation(world, HEXASIZE);


        // draws the world to the screen
        ter.renderFrame(world);
    }
}
