package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import static java.lang.Math.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if(input.charAt(0)== 'n')
        {
            //生成随机世界
            int i = input.indexOf("s");
            String str = input.substring(1,min(i,8));
            long s = Long.parseLong(str);
            WorldGenerator w = new WorldGenerator(80,40,s);
            //System.out.println(s);
            w.GenerateWorld();
            //把s给读掉
            System.out.println(w.getPer().getX()+" "+w.getPer().getY());
            i++;
            while(i<input.length())
            {
                char c = input.charAt(i);
                if(c == 'w' || c=='a' || c=='s' || c=='d')
                {
                    w.MovePer(c);
                }
                else if(c == ':')
                {
                    WorldSaveRead.saveWorld(w,"SaveWorld.dat");
                    break;
                }
                i++;
            }
            System.out.println(w.getPer().getX()+" "+w.getPer().getY());
            return w.getWorld();
        }
        else if(input.charAt(0)== 'l')
        {
            String filename = "SaveWorld.dat";
            WorldGenerator w = WorldSaveRead.readWorld(filename);
            int i = 1;
            while(i<input.length())
            {
                char c = input.charAt(i);
                if(c == 'w' || c=='a' || c=='s' || c=='d')
                {
                    w.MovePer(c);
                }
                else if(c == ':')
                {
                    WorldSaveRead.saveWorld(w,"SaveWorld.dat");
                    break;
                }
                i++;
            }
            return w.getWorld();
        }
        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
}
