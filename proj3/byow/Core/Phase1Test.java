package byow.Core;

import byow.TileEngine.TERenderer;

public class Phase1Test {
    public static void main(String[] args)
    {
        TERenderer ter = new TERenderer();
        ter.initialize(100, 50);
        WorldGenerator w = new WorldGenerator(100,50,1145991);
        w.GenerateWorld();
        //w.GenerateHallways_fromRoom(w.getRoom_queue().poll(),w.getRoom_queue().poll());
        //w.GenerateRoom(w.new node(77,8),1,3);
        //w.GenerateRoom(w.new node(15,15),5,5);
        //w.Draw_room(w.new room(w.new node(30,5),w.new node(35,10)));
        //w.GenerateHallways(w.new node(10,10),w.new node(15,5));
        ter.renderFrame(w.getWorld());
    }
}
