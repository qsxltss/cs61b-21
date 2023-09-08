package byow.Core;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class Phase2Test
{
    private int width;
    private int height;
    private long seed;
    public static void main(String[] args)
    {
        Phase2Test test = new Phase2Test(100,50,3);
        test.test();
    }
    public Phase2Test(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.seed = seed;
        //TODO: Initialize random number generator
    }
    public void test()
    {
        WorldGenerator w = new WorldGenerator(60,20, seed);
        Game game = new Game();
        game.initialize(70, 40);
        w.GenerateWorld();
        game.renderFrame(w.getWorld(),w.getPer());
    }
}
