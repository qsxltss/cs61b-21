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
    private static int WIDTH = 30;
    private static int HEIGHT = 30;

    public static void addHexagon(int s,int x,int y,TETile[][] tiles)
    {
        //计划一行一行的修改，分为上下两个部分
        //len是某层的长度
        int len = s;
        //上半部分
        for(int j =0; j<s; ++j)
        {
            System.out.println(j);
            for(int i = x-j; i<x-j+len; ++i)
            {
                tiles[i][y+j] = Tileset.FLOWER;
            }
            len+=2;
        }
        //上半部分
        for(int j = s; j< 2*s; ++j)
        {
            len-=2;
            for(int i =x-(2*s-j-1); i<x-(2*s-j-1)+len; ++i)
            {
                tiles[i][y+j] = Tileset.FLOWER;
            }
        }
    }

    public static void world_init(TETile[][] world)
    {
        for(int i=0;i<WIDTH;++i)
        {
            for(int j=0;j<HEIGHT;++j)
            {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        world_init(world);
        addHexagon(3,10,10,world);
        ter.renderFrame(world);
    }
}
