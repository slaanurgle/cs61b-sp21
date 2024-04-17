package byow.Core;

public class Room {

    // A room is at least 3 tiles of width and height, walls are included in a room.
    public int width;
    public int height;
    public int xloc;
    public int yloc;
    public Room(int w, int h, int x, int y) {
        width = w;
        height = h;
        xloc = x;
        yloc = y;
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

}
