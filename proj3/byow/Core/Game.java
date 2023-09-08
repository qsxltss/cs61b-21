package byow.Core;


import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class Game {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private WorldGenerator w;
    /** 初始化 */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Times New Romes", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        initMenu();
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        char s = 0;
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                s = StdDraw.nextKeyTyped();
                if (s == 'N') {
                    System.out.println("12345");
                } else if (s == 'L') {
                    System.out.println("678910");
                } else if (s == 'Q') {
                    return;
                } else {
                    System.out.println("Please try again!");
                }
            }
        }
    }
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /** 初始化界面 */
    public void initMenu()
    {
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Times New Romes", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2,height/4*3,"CS61B Game");

        Font font1 = new Font("Times New Romes", Font.BOLD, 20);
        StdDraw.setFont(font1);
        StdDraw.text(width/2,height/4*3-3,"By WPL");
        StdDraw.text(width/2,height/4,"New Game(N)");
        StdDraw.text(width/2,height/4-2,"Load Game(L)");
        StdDraw.text(width/2,height/4-4,"Quit(Q)");
    }


    /** 读取world的内容*/
    public void renderFrame(TETile[][] world, WorldGenerator.node n1) {

        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.line(0,height-5,width,height-4);
        StdDraw.textLeft(0,height-1,"Your Pic: @");
        StdDraw.textLeft(0,height-3,"Your Position: ("+n1.getX()+","+n1.getY()+")");
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x+xOffset , y + yOffset);
            }
        }
        StdDraw.show();
    }
}
