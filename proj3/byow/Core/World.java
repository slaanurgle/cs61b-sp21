package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.Random;
import java.util.HashSet;
import static byow.Core.RandomUtils.*;

public class World implements Serializable {
    TERenderer ter = new TERenderer();
    public Random rand;
    public TETile[][] worldTiles;
    public transient HashSet<Room> rooms;
    public static final int ROOMNUM = 20;

    public World(long seed) {
        rand = new Random(seed);
        rooms = new HashSet<>();
        worldTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i += 1) {
            for (int j = 0; j < Engine.HEIGHT; j += 1) {
                worldTiles[i][j] = Tileset.NOTHING;
            }
        }
        createWorld();
        StdDraw.clear(StdDraw.BLACK);
        renderWorld();
        StdDraw.pause(1000000);
    }

    private void createWorld() {
        generateRooms();
    }

    private void generateRooms() {
        for (int i = 0; i < ROOMNUM; i += 1) {
            Room newRoom = generateRandomRoom();
            fillRoomTiles(newRoom);
        }
    }

    /** Create a random valid room object and add it to rooms */
    private Room generateRandomRoom() {
        // TODO: check whether it is needed to use other contribution
        boolean roomValid;
        Room newRoom;
        do {
            roomValid = true;
            int width = uniform(rand, 3, 10);
            int height = uniform(rand, 3, 10);
            int xloc = uniform(rand, 0, Engine.WIDTH - width + 1);
            int yloc = uniform(rand, 0, Engine.HEIGHT - height + 1);
            newRoom = new Room(width, height, xloc, yloc);
            for (Room r : rooms) {
                if (newRoom.overlap(r)) {
                    roomValid = false;
                }
            }
        } while (!roomValid);
        rooms.add(newRoom);
        return newRoom;
    }

    private void fillRoomTiles(Room room) {
        // fill the walls
        for (int i = room.xloc; i <= room.xloc + room.width - 1; i += 1) {
            worldTiles[i][room.yloc] = Tileset.WALL;
            worldTiles[i][room.yloc + room.height - 1] = Tileset.WALL;
        }
        for (int i = room.yloc + 1; i <= room.yloc + room.height - 2; i += 1) {
            worldTiles[room.xloc][i] = Tileset.WALL;
            worldTiles[room.xloc + room.width - 1][i] = Tileset.WALL;
        }
        // fill the floors.
        for (int i = room.xloc + 1; i <= room.xloc + room.width - 2; i += 1) {
            for (int j = room.yloc + 1; j <= room.yloc + room.height - 2; j += 1) {
                worldTiles[i][j] = Tileset.FLOOR;
            }
        }
    }

    private void renderWorld() {
        ter.renderFrame(worldTiles);
    }


}
