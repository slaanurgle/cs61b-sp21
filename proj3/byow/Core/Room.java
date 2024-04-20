package byow.Core;

import byow.TileEngine.Tileset;

import java.util.*;

import static byow.Core.Utils.*;
public class Room {

    // A room is at least 3 tiles of width and height, walls are included in a room.
    public int width;
    public int height;
    public int xloc;
    public int yloc;
    public HashMap<Pos, Integer> walls;
    public static final int EDGE = 0;
    public static final int SOUTH = 1;
    public static final int NORTH = 2;
    public static final int EAST = 3;
    public static final int WEST = 4;

    public Room(int w, int h, int x, int y) {
        width = w;
        height = h;
        xloc = x;
        yloc = y;
        walls = new HashMap<>();
        walls.put(new Pos(xloc, yloc), EDGE);
        walls.put(new Pos(xloc, yloc + height - 1), EDGE);
        walls.put(new Pos(xloc + width - 1, yloc), EDGE);
        walls.put(new Pos(xloc + width - 1, yloc + height - 1), EDGE);
        for (int i = xloc + 1; i <= xloc + width - 2; i += 1) {
            walls.put(new Pos(i, yloc), SOUTH);
            walls.put(new Pos(i, yloc + height - 1), NORTH);
        }
        for (int i = yloc + 1; i <= yloc + height - 2; i += 1) {
            walls.put(new Pos(xloc, i), EAST);
            walls.put(new Pos(xloc + width - 1, i), WEST);
        }
    }

    /** Return walls as a Set */
    public Set<Pos> getWalls() {
        return walls.keySet();
    }

    public boolean isEdgeWall(Pos wall) {
        return walls.get(wall) == EDGE;
    }

    public Pos[] getEdgeWalls() {
        LinkedList<Pos> edgeWalls = new LinkedList<>();
        for (Map.Entry<Pos, Integer> wall : walls.entrySet()) {
            int dir = wall.getValue();
            if (dir == EDGE) {
                edgeWalls.add(wall.getKey());
            }
        }
        return edgeWalls.toArray(new Pos[edgeWalls.size()]);
    }

    /** check if two rooms is overlapped */
    public boolean overlap(Room room) {
        return rangeOverlap(xloc, width, room.xloc, room.width) &&
                rangeOverlap(yloc, height, room.yloc, room.height);
    }

    /** check if two range is overlapped. */
    private boolean rangeOverlap(int x1, int len1, int x2, int len2) {
        if (x2 > x1) {
            return x2 <= x1 + len1;
        } else if (x2 < x1) {
            return x1 <= x2 + len2;
        } else {
            return true;
        }
    }

    public HashMap<Pos, Integer> getProperWalls() {
        HashMap<Pos, Integer> properWalls = new HashMap<>();
        for (Map.Entry<Pos, Integer> p : walls.entrySet()) {
            if (p.getValue() == EDGE) {
                continue;
            }
            int x = p.getKey().x;
            int y = p.getKey().y;
            if (x >= 1 && x <= Engine.WIDTH - 3 && y >= 1 && y <= Engine.HEIGHT - 3) {
                properWalls.put(p.getKey(), p.getValue());
            }
        }
        return properWalls;
//        LinkedList<Pos> properWallPos = new LinkedList<>();
//        for (int i = xloc; i <= xloc + width - 1; i += 1) {
//            if (yloc >= 1) {
//                properWallPos.addLast(new Pos(i, yloc));
//            }
//            if (yloc + height - 1 <= Engine.HEIGHT - 3) {
//                properWallPos.addLast(new Pos(i, yloc + height - 1));
//            }
//        }
//        for (int i = yloc + 1; i <= yloc + height - 2; i += 1) {
//            if (xloc >= 1) {
//                properWallPos.addLast(new Pos(xloc, i));
//            }
//            if (xloc + width - 1 <= Engine.WIDTH - 3) {
//                properWallPos.addLast(new Pos(xloc + width - 1, i));
//            }
//        }
//        return properWallPos.toArray(new Pos[properWallPos.size()]);
    }

}
