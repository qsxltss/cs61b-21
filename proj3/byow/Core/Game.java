package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import org.antlr.v4.runtime.misc.Utils;

import java.awt.Color;
import java.awt.Font;
import java.io.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class Game implements Serializable {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private WorldGenerator World;
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
                if (s == 'N')
                {
                    long seed = System.currentTimeMillis();
                    System.out.println(seed);
                    World = new WorldGenerator(width,height-10,seed);
                    World.GenerateWorld();
                    renderFrame();
                    pause(3000);
                    GameStart();
                    Font font1 = new Font("Times New Romes", Font.BOLD, 20);
                    StdDraw.setFont(font1);
                    renderFramePart(1);
                    play();
                }
                else if (s == 'L')
                {
                    File f = new File("./save_world1.txt");
                    if(!f.exists())
                    {
                        System.out.println("No Saving File Before!");
                    }
                    else
                    {
                        try {
                            InputStream inputStream = new FileInputStream("./save_world1.txt");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else if (s == 'Q')
                {
                    return;
                }
                else
                {
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
    public void renderFrame() {
        TETile[][] world = World.getWorld();
        WorldGenerator.node n1 = World.getPer();
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.line(0,height-5,width,height-4);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2,height-1,"Your Pic: @");
        StdDraw.text(width/2,height-3,"Your Position: ("+n1.getX()+","+n1.getY()+")");
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

    /** 读取world的内容*/
    public void renderFramePart(int i) {
        TETile[][] world = World.getWorld();
        WorldGenerator.node n1 = World.getPer();
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.line(0,height-5,width,height-4);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2,height-1,"Your Pic: @");
        StdDraw.text(width/2,height-3,"Your Position: ("+n1.getX()+","+n1.getY()+")");
        for (int x = max(0,n1.getX()-i); x <= min(width,n1.getX()+i); x += 1) {
            for (int y = max(0,n1.getY()-i); y < min(width,n1.getY()+i); y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x+xOffset , y + yOffset);
            }
        }
        StdDraw.show();
    }

    /** 游戏的进行过程 */
    public void play()
    {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char s = StdDraw.nextKeyTyped();
                // 退出请按Q
                if(s == 'Q')
                {
                    System.exit(0);
                }
                WorldGenerator.node n = World.MovePer(s);
                if(n == null) continue;
                if(CheckWin(n))
                {
                    Congratulation();
                }
                if(World.CheckFlowers() == 3)
                {
                    renderFramePart(2);
                }
                else if(World.CheckFlowers() == 2)
                {
                    renderFramePart(4);
                }
                else if(World.CheckFlowers() == 1)
                {
                    renderFramePart(6);
                }
                else
                {
                    renderFrame();
                }
            }
        }
    }

    /** 判赢 */
    public boolean CheckWin(WorldGenerator.node n)
    {
        if(World.getPer().getX() == World.getDoor().getX() && World.getPer().getY() == World.getDoor().getY())
        {
            return true;
        }
        return false;
    }

    /** 暂停一段时间 */
    public void pause(int milisecond)
    {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /** 游戏正式开始（熄灯）界面 */
    public void GameStart()
    {
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Times New Romes", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2,height/2+2,"Light Will Be Turn Off!");
        StdDraw.text(width/2,height/2-2,"Enjoy Your Game");
        StdDraw.show();
        pause(1500);
    }
    /** 胜利结算界面 */
    public void Congratulation()
    {
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Times New Romes", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width/2,height/2+2,"Congratulation!");
        StdDraw.text(width/2,height/2-2,"You Are The Genius!");
        StdDraw.show();
        pause(3000);
        System.exit(0);
    }
}
