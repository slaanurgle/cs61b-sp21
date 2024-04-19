package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.*;

import static byow.Core.RandomUtils.*;

public class World implements Serializable {
    TERenderer ter = new TERenderer();
    public Random rand;
    public TETile[][] worldTiles;
    public transient HashSet<Room> rooms;
    public static final int ROOMNUM = 20;
    public static final int DOORNUM = 2;
    public static final int MINROOMSIZE = 4;
    public static final int MAXROOMSIZE = 12;
    public World(long seed) {
        // Initialize the variables.
        rand = new Random(seed);
        rooms = new HashSet<>();
        worldTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i += 1) {
            for (int j = 0; j < Engine.HEIGHT; j += 1) {
                worldTiles[i][j] = Tileset.NOTHING;
            }
        }
        createWorld();
        renderWorld();
        StdDraw.pause(1000000); // TODO: delete this after finishing.
    }



    private void createWorld() {
        generateRooms();
        createHallways();
        addLockedDoor();
    }

    private void generateRooms() {
        for (int i = 0; i < ROOMNUM; i += 1) {
            Room newRoom = generateRandomRoom();
            fillRoomTiles(newRoom, Tileset.FLOOR);
        }
    }

    /** Create a random valid room object and add it to rooms */
    private Room generateRandomRoom() {
        // TODO: check whether it is needed to use other contribution
        boolean roomValid;
        Room newRoom;
        do {
            roomValid = true;
            int width = uniform(rand, MINROOMSIZE, MAXROOMSIZE);
            int height = uniform(rand, MINROOMSIZE, MAXROOMSIZE);
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

    private void fillRoomTiles(Room room, TETile floorType) {
        // fill the walls
        fillWalls(room);
        // fill the floors.
        fillRoomFloors(room, floorType);
    }
    private void fillWalls(Room room) {
        for (Pos p : room.getWalls()) {
            worldTiles[p.x][p.y] = Tileset.WALL;
        }
    }

    private void fillRoomFloors(Room room, TETile type) {
        for (int i = room.xloc + 1; i <= room.xloc + room.width - 2; i += 1) {
            for (int j = room.yloc + 1; j <= room.yloc + room.height - 2; j += 1) {
                worldTiles[i][j] = type;
            }
        }
    }
    private void renderWorld() {
        StdDraw.clear(StdDraw.BLACK);
        ter.renderFrame(worldTiles);
    }

    private void createHallways() {
        // make a wall in the edges, prevent the hallways reach the edges.
        int xmax = Engine.WIDTH - 1;
        int ymax = Engine.HEIGHT - 1;
        for (int i = 0; i <= xmax; i += 1) {
            worldTiles[i][0] = Tileset.WALL;
            worldTiles[i][ymax] = Tileset.WALL;
        }
        for (int i = 0; i <= ymax; i += 1) {
            worldTiles[0][i] = Tileset.WALL;
            worldTiles[xmax][i] = Tileset.WALL;
        }
        // traverse all tiles and create hallways until there are no NOTHING tile in the world.
        for (int i = 1; i <= xmax; i += 1) {
            for (int j = 1; j <= ymax; j += 1) {
                TETile tile = worldTiles[i][j];
                if (tile.equals(Tileset.NOTHING)) {
                    generateHallway(i, j);
                }
            }
        }
        // link the rooms
        linkRoomHallway();
        // remove dead end;
        for (int i = 1; i <= xmax; i += 1) {
            for (int j = 1; j <= ymax; j += 1) {
                if (isDeadEnd(new Pos(i, j))) {
                    removeDeadEnd(i, j);
                }
            }
        }
        removeRedundantWall();
        addRoomEdges();
        for (Room room : rooms) {
            fillRoomFloors(room, Tileset.FLOOR);
        }
    }

    /** Check whether worldTiles[X][Y] is valid to be added to Hallway. */
    private boolean isValidHallway(Pos p) {
        TETile tile = posToTile(p);
        // The tile should be Tileset.NOTHING
        if (!tile.equals(Tileset.NOTHING)) {
            return false;
        }
        // The tile
        int cnt = 0;
        for (Pos adj : getAdjacent(p)) {
            TETile adjTile = posToTile(adj);
            if (adjTile.equals(Tileset.FLOOR)) {
                cnt += 1;
            }
        }
        // valid hallway has only one adjacent FLOOR tile
        return cnt == 1;
    }


    /** Use DFS to create a random hallway from worldTiles[X][Y] */
    private void generateHallway(int x, int y) {
        Pos startPos = new Pos(x, y);
        Stack<Pos> fringe = new Stack<>();
        // initialize marked[][]
        boolean[][] marked = new boolean[Engine.WIDTH][Engine.HEIGHT];
        boolean[][] valid = new boolean[Engine.WIDTH][Engine.HEIGHT];
        for (int i = 0; i < Engine.WIDTH; i += 1) {
            for (int j = 0; j < Engine.HEIGHT; j += 1) {
                marked[i][j] = false;
                valid[i][j] = true;
            }
        }
        // start DFS
        fringe.push(startPos);
        while (!fringe.empty()) {
            Pos currPos = fringe.pop();
            if (worldTiles[currPos.x][currPos.y].equals(Tileset.WALL)) {
                continue;
            }
            worldTiles[currPos.x][currPos.y] = Tileset.FLOOR;
            marked[currPos.x][currPos.y] = true;
            Pos[] adjacents = getAdjacent(currPos);
            // Randomly push the nearby valid tiles to stack
            shuffle(rand, adjacents);
            for (Pos p : adjacents) {
                if (!marked[p.x][p.y]) {
                    if (isValidHallway(p)) {
                        fringe.push(p);
                    } else {
                        worldTiles[p.x][p.y] = Tileset.WALL;
                    }
                }
            }
        }

    }

    private TETile posToTile(Pos pos) {
        int x = pos.x;
        int y = pos.y;
        TETile returnTile = worldTiles[x][y];
        return returnTile;
    }

    /** return a TETile array containing the tiles adjacent to worldTiles[X][Y] */
    private Pos[] getAdjacent(Pos p) {
        LinkedList<Pos> tiles = new LinkedList<>();
        int x = p.x;
        int y = p.y;
        if (x != 0) {
            tiles.add(new Pos(x - 1, y));
        }
        if (x != Engine.WIDTH - 1) {
            tiles.add(new Pos(x + 1, y));
        }
        if (y != 0) {
            tiles.add(new Pos(x, y - 1));
        }
        if (y != Engine.HEIGHT - 1) {
            tiles.add(new Pos(x, y + 1));
        }
        return tiles.toArray(new Pos[tiles.size()]);
    }

    /** Remove the dead end from worldTiles[X][Y], the start tile should be dead end */
    private void removeDeadEnd(int x, int y) {
        // TODO: use random to delete only some of the dead ends.
        Pos currPos = new Pos(x, y);
        // when remove dead end, remove redundant walls
        worldTiles[x][y] = Tileset.WALL;
        for (Pos adjPos : getAdjacent(currPos)) {
            if (isDeadEnd(adjPos)) {
                removeDeadEnd(adjPos.x, adjPos.y);
            }
        }
    }

    private void addRoomEdges() {
        for (Room room : rooms) {
            for (Pos edge : room.getEdgeWalls()) {
                worldTiles[edge.x][edge.y] = Tileset.WALL;
            }
        }
    }

    /** Remove redundant walls, but it may destruct some of the room edges. */
    private void removeRedundantWall() {
        for (int i = 0; i < Engine.WIDTH; i += 1) {
            for (int j = 0; j < Engine.HEIGHT; j += 1) {
                if (worldTiles[i][j].equals(Tileset.WALL)) {
                    boolean redundant = true;
                    for (Pos adjPos : getAdjacent(new Pos(i, j))) {
                        if (worldTiles[adjPos.x][adjPos.y].equals(Tileset.FLOOR)) {
                            redundant = false;
                            break;
                        }
                    }
                    if (redundant) {
                        worldTiles[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    private boolean isDeadEnd(Pos pos) {
        if (!worldTiles[pos.x][pos.y].equals(Tileset.FLOOR)) {
            return false;
        }
        int floorNum = 0;
        for (Pos adj : getAdjacent(pos)) {
            if (worldTiles[adj.x][adj.y].equals(Tileset.FLOOR)) {
                floorNum += 1;
            }
        }
        return floorNum == 1 || floorNum == 0;
    }

    private void linkRoomHallway() {
        // Traverse all room walls
        for (Room room : rooms) {
            // Find a wall near the hallway.
            LinkedList<Pos> properWalls = new LinkedList<>();
            for (Pos pos : room.getWalls()) {
                if (room.isEdgeWall(pos)) {
                    continue;
                }
                int adjNum = 0;
                for (Pos adjPos : getAdjacent(pos)) {
                    if (worldTiles[adjPos.x][adjPos.y].equals(Tileset.FLOOR)) {
                        adjNum += 1;
                    }
                }
                if (adjNum >= 2) {
                    properWalls.addLast(pos);
                }
            }
            // randomly choose proper walls and make it a FLOOR
            Pos[] shuffledWalls = properWalls.toArray(new Pos[properWalls.size()]);
            shuffle(rand, shuffledWalls);
            for (int i = 0; i < DOORNUM && i < properWalls.size(); i += 1) {
                Pos wall = shuffledWalls[i];
                worldTiles[wall.x][wall.y] = Tileset.FLOOR;
            }
        }

//        for (Room room : rooms) {
//            // Traverse all rooms, find their proper walls
//            HashMap<Pos, Integer> properwalls = room.getProperWalls();
//            Pos[] walls = properwalls.keySet().toArray
//                    (new Pos[room.getProperWalls().keySet().size()]);
//            shuffle(rand, walls);
//            for (int i = 0; i < DOORNUM; i += 1) {
//                linkToHallway(walls[i], properwalls.get(walls[i]));
//            }
//        }
    }

    private void linkToHallway(Pos wall, int dir) {
        int x = wall.x;
        int y = wall.y;
        Pos currPos = wall;
        TETile currTile;
        // search the next tile in the DIR, check if it is linked to FLOOR
        boolean linked = false;
        do {
            worldTiles[x][y] = Tileset.FLOOR;
            int cnt = 0;
            for (Pos adjPos : getAdjacent(currPos)) {
                if (worldTiles[adjPos.x][adjPos.y].equals(Tileset.FLOOR)) {
                    cnt += 1;
                }
            }
            if (cnt > 1) {
                linked = true;
            } else {
                currPos = nextInDirection(currPos, dir);
            }
        } while (!linked);
    }

    private Pos nextInDirection(Pos pos, int dir) {
        switch (dir) {
            case Room.SOUTH:
                return new Pos(pos.x - 1, pos.y);
            case Room.NORTH:
                return new Pos(pos.x + 1, pos.y);
            case Room.EAST:
                return new Pos(pos.x, pos.y + 1);
            case Room.WEST:
                return new Pos(pos.x, pos.y - 1);
        }
        return null;
    }

    private void addLockedDoor() {
        // traverse all room to get non-edge walls
        LinkedList<Pos> nonEdgeWalls = new LinkedList<>();
        for (Room room : rooms) {
            for (Pos wall : room.getWalls()) {
                // the object place should be a non-edge wall
                if (!room.isEdgeWall(wall) && worldTiles[wall.x][wall.y].equals(Tileset.WALL)) {
                    nonEdgeWalls.addLast(wall);
                }
            }
        }
        // randomly choose a non-edge wall to be a locked door.
        int properWallNum = nonEdgeWalls.size();
        Pos[] properWalls = nonEdgeWalls.toArray(new Pos[0]);
        Pos lockDoorPos = properWalls[rand.nextInt(properWallNum)];
        worldTiles[lockDoorPos.x][lockDoorPos.y] = Tileset.LOCKED_DOOR;
    }
}
