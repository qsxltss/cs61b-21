package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        //System.out.println(game.generateRandomString(10));
        //System.out.println(game.solicitNCharsInput(5));
        //game.flashSequence(game.generateRandomString(10));
        //game.drawTitles(1,"Type!");
        //game.printSuccess();
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("宋体", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        this.rand = new Random(seed);
        //TODO: Initialize random number generator
    }

    /** 随机生成一个String */
    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String str = "";
        for(int i=0; i<n; ++i)
        {
            int k = RandomUtils.uniform(rand,0,26);
            str += CHARACTERS[k];
        }
        return str;
    }
    /** 绘制静态显示：String+Title */
    public void drawFrame(String s,int round,String type) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("宋体",Font.PLAIN,30));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(this.width/2.0,this.height/2.0,s);
        drawTitles(round,type);
        StdDraw.show();
    }
    /** 绘制上方Title部分*/
    public void drawTitles(int round,String type)
    {
        StdDraw.setFont(new Font("宋体",Font.PLAIN,20));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.line(0,this.height-3,this.width,this.height-3);
        StdDraw.textLeft(0,this.height-1,"第"+round+"关");
        StdDraw.text(this.width/2.0,this.height-1,type);
        int i = RandomUtils.uniform(this.rand,0,7);
        StdDraw.textRight(this.width,this.height-1,ENCOURAGEMENT[i]);
    }
    /** 动态显示一个String */
    public void flashSequence(String letters,int round) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for(int i=0; i<letters.length(); ++i)
        {
            String s = Character.toString(letters.charAt(i));
            drawFrame(s,round,"观察！");
            pause(1000);
            drawFrame("",round,"观察！");
            pause(500);
        }
    }
    /** */
    public void pause(int milisecond)
    {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String solicitNCharsInput(int n,int round) {
        //TODO: Read n letters of player input
        String str = "";
        drawFrame(str,round,"输入!");
        while(n>0)
        {
            if(StdDraw.hasNextKeyTyped())
            {
                n -= 1;
                str += StdDraw.nextKeyTyped();
                drawFrame(str,round,"输入!");
            }
        }
        return str;
    }

    public boolean checkPass(String s1,String s2)
    {
        if(s1.equals(s2)) return true;
        return false;
    }

    public void printFailure()
    {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("宋体",Font.PLAIN,15));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(this.width/2.0,this.height/2.0,"胜败乃兵家常事，请大侠重新来过！");
        StdDraw.show();
        //pause(5000);
    }

    public void printPass(int round)
    {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("宋体",Font.PLAIN,15));
        StdDraw.text(this.width/2.0,this.height/2.0,"大侠通过了第"+round+"关!");
        StdDraw.show();
        pause(3000);
    }
    public void printSuccess()
    {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("宋体",Font.PLAIN,15));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(this.width/2.0,this.height/2.0,"恭喜大侠通过考验！");
        StdDraw.show();
        //pause(10000);
    }
    public void startGame() {
        //TODO: Set any relevant variables beforen the game starts
        //TODO: Establish Engine loop
        for(int i=1;i<=10; ++i)
        {
            int len = RandomUtils.uniform(rand,i/2+1,i+1);
            String RandomStr = generateRandomString(len);
            flashSequence(RandomStr,i);
            String strType = solicitNCharsInput(len,i);
            if(!checkPass(strType,RandomStr))
            {
                printFailure();
                return;
            }
            printPass(i);
        }
        printSuccess();
    }
}
