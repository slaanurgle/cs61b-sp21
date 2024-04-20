package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.PriorityQueue;

import static byow.Core.Utils.*;
import static byow.Core.RandomUtils.*;

public class Monster extends byow.Core.Unit{
    public Monster(World w) {
        super(w);
        w.worldTiles[posx][posy] = Tileset.MOUNTAIN;
    }

//    public Pos[] foundPath(Unit other) {
//        int[][] dirTo = new int[Engine.WIDTH][Engine.HEIGHT];
//        PriorityQueue<Pos> fringe = new PriorityQueue<>();
//        fringe.push()
//
//    }


}
