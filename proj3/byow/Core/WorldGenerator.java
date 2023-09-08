package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

import static java.lang.Math.*;

public class WorldGenerator {
    private int width;
    private int height;
    private TETile[][] world;
    private Queue<room> Room_queue;
    private long seed;
    private node per;

    public class node {
        private int x;
        private int y;

        public node(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX(){return x;}
        public int getY(){return y;}
    }

    public class room implements Comparable<room> {
        private node a;
        private node b;
        private int w;
        private int h;

        public room(node a, node b) {
            this.a = a;
            this.b = b;
            this.w = b.x - a.x;
            this.h = b.y - a.y;
        }

        public int len() {
            return 2 * (w + h);
        }

        @Override
        public int compareTo(room o) {
            if (this.b.x >= o.b.x) return 1;
            else return -1;
        }
    }

    public WorldGenerator(int w, int h,long seed) {
        this.width = w;
        this.height = h;
        world = new TETile[w][h];
        Room_queue = new PriorityQueue<>();
        this.seed = seed;
    }

    public TETile[][] getWorld() {
        return world;
    }
    /** 返回当前人物位置 */
    public node getPer(){return per;}

    public Queue<room> getRoom_queue()
    {
        return Room_queue;
    }
    //初始化world
    public void InitWorld()
    {
        for(int i=0; i<width; ++i)
        {
            for(int j=0; j<height; ++j)
            {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    //画格子
    public void Draw_grid(node n,TETile s)
    {
        if(world[n.x][n.y] == Tileset.FLOOR) return;
        world[n.x][n.y] = s;
    }
    public void Draw_grid(int x,int y,TETile s)
    {
        if(world[x][y] == Tileset.FLOOR) return;
        world[x][y] = s;
    }
    //画房间
    public void Draw_room(room r)
    {
        for(int i = r.a.x; i<=r.b.x; i++)
        {
            for(int j=r.a.y; j<=r.b.y; j++)
            {
                Draw_grid(i,j,Tileset.FLOOR);
            }
        }
        for(int i = r.a.x;i<=r.b.x;i++)
        {
            Draw_grid(i,r.a.y-1,Tileset.WALL);
            Draw_grid(i,r.b.y+1,Tileset.WALL);
        }
        for(int i = r.a.y;i<=r.b.y;i++)
        {
            Draw_grid(r.a.x-1,i,Tileset.WALL);
            Draw_grid(r.b.x+1,i,Tileset.WALL);
        }
        Draw_grid(r.a.x-1,r.a.y-1,Tileset.WALL);
        Draw_grid(r.a.x-1,r.b.y+1,Tileset.WALL);
        Draw_grid(r.b.x+1,r.a.y-1,Tileset.WALL);
        Draw_grid(r.b.x+1,r.b.y+1,Tileset.WALL);
    }

    //检查两个房间是否重叠
    public boolean OverlapRooms(room r1,room r2)
    {
        if(r1.b.x < (r2.a.x-2) || r1.b.y < (r2.a.y-2) || r2.b.x < (r1.a.x-2) || r2.b.y < (r1.a.y-2))
        {
            return false;
        }
        return true;
    }

    //生成房间
    public void GenerateRoom(node n,int w,int h)
    {
        room r = new room(n,new node(n.x+w,n.y+h));
        Object[] nn= this.Room_queue.toArray();
        for(int i=nn.length-1;i>=0;i--)
        {
            if(OverlapRooms((room)nn[i],r))
            {
                //System.out.println("overlap!");
                return;
            }
        }
        Draw_room(r);
        Room_queue.add(r);
    }

    //随机生成n个房间
    public void GenerateRoom_Randomly(int n)
    {
        int k = 0;
        while(Room_queue.size()<n)
        {
            Random r = new Random(seed+k);
            k++;
            int x = RandomUtils.uniform(r,1,width-11);
            int y = RandomUtils.uniform(r,1,height-11);
            node n1 = new node(x,y);
            int w = RandomUtils.uniform(r,1,10);
            int h = RandomUtils.uniform(r,1,10);
            //System.out.println(x+" "+y+" "+w+" "+h);
            GenerateRoom(n1,w,h);
        }
    }

    /** 从一个房间的外部边缘随机返回一个点 */
    public node NodeFromRoomOutside(room r1)
    {
        Random r = new Random(seed);
        node n1 = r1.a;
        node n2 = r1.b;
        int len1 = n2.x-n1.x+1;
        int len2 = n2.y-n1.y+1;
        int len = len1*2 + len2*2;
        int i = RandomUtils.uniform(r,0,len);
        if(i>=0 && i<len1)
        {
            return new node(n1.x+i,n1.y-1);
        }
        else if(i>=len1 && i<len1*2)
        {
            i -= len1;
            return new node(n1.x+i,n2.y+1);
        }
        else if(i>=len1*2 && i<len1*2+len2)
        {
            i -= len1*2;
            return new node(n1.x-1,n1.y+i);
        }
        else
        {
            i -= (len1*2+len2);
            return new node(n2.x+1,n1.y+i);
        }
    }

    //从一个房间的内部边缘随机返回一个点
    public node NodeFromRoom(room r1)
    {
        Random r = new Random(seed);
        int i = RandomUtils.uniform(r,0,r1.len());
        if(i>=0 && i<r1.h)
        {
            return new node(r1.a.x,i+r1.a.y);
        }
        else if(i>=r1.h && i<r1.w+r1.h)
        {
            i -= r1.h;
            return new node(i+r1.a.x,r1.b.y);
        }
        else if(i>=r1.h+r1.w && i<r1.w+2*r1.h)
        {
            i -= (r1.h+r1.w);
            return new node(r1.b.x,r1.b.y-i);
        }
        else if(i>=2*r1.h+r1.w && i<2*r1.w+2*r1.h)
        {
            i -= (2*r1.h+r1.w);
            return new node(r1.b.x-i,r1.a.y);
        }
        return null;
    }
    public void GenerateHallways_fromRoom(room r1,room r2)
    {
        node n1 = NodeFromRoom(r1);
        node n2 = NodeFromRoom(r2);
        if(n2.x>=n1.x) GenerateHallways(n1,n2);
        else GenerateHallways(n2,n1);
    }
    public void GenerateHallways_AllRoom()
    {
        Object[]Room_list = Room_queue.toArray();
        if(Room_list.length == 0) return;
        room r = (room)Room_list[0];
        for(int i=1;i<Room_list.length;++i)
        {
            GenerateHallways_fromRoom(r,(room)Room_list[i]);
            r = (room)Room_list[i];
        }
    }

    //从(x1,y1)到(x2,y2)构建过道
    //要求b.x>=a.x
    public void GenerateHallways(node a, node b)
    {
        for(int i = a.x; i<=b.x; i++)
        {
            Draw_grid(i,a.y,Tileset.FLOOR);
            Draw_grid(i,a.y-1,Tileset.WALL);
            Draw_grid(i,a.y+1,Tileset.WALL);
        }
        Draw_grid(b.x+1,a.y,Tileset.WALL);
        if(b.y >= a.y)
        {
            Draw_grid(b.x+1,a.y-1,Tileset.WALL);
            for(int j = a.y+1; j<=b.y; j++)
            {
                Draw_grid(b.x,j,Tileset.FLOOR);
                Draw_grid(b.x-1,j,Tileset.WALL);
                Draw_grid(b.x+1,j,Tileset.WALL);
            }
        }
        else
        {
            Draw_grid(b.x+1,a.y+1,Tileset.WALL);
            for(int j = b.y; j<=a.y-1; j++)
            {
                Draw_grid(b.x,j,Tileset.FLOOR);
                Draw_grid(b.x-1,j,Tileset.WALL);
                Draw_grid(b.x+1,j,Tileset.WALL);
            }
        }
    }


    public node NodeInRoom(room r1)
    {
        Random r = new Random(seed);
        int i = RandomUtils.uniform(r,0,(r1.h+1)*(r1.w+1));
        int m = i%(r1.w+1);
        int n = i/(r1.w+1);
        return new node(r1.a.x+m,r1.a.y+n);
    }
    public void GeneratePlayer()
    {
        Object[] room_list = Room_queue.toArray();
        Random r = new Random(seed);
        if(room_list.length == 0) return;
        int i = RandomUtils.uniform(r,0,room_list.length);
        System.out.println(i);
        node n1 = NodeInRoom((room)room_list[i]);
        per = n1;
        world[n1.x][n1.y] = Tileset.AVATAR;
    }
    public void GenerateDoor()
    {
        Object[] room_list = Room_queue.toArray();
        Random r = new Random(seed+3);
        if(room_list.length == 0) return;
        int i = RandomUtils.uniform(r,0, room_list.length);
        room r1 = (room)room_list[i];
        node n1 = NodeFromRoomOutside(r1);
        if(world[n1.x][n1.y] == Tileset.FLOOR)
        {
            if(world[n1.x-2][n1.y] == Tileset.WALL)
            {
                world[n1.x-2][n1.y] = Tileset.LOCKED_DOOR;
            }
            else if(world[n1.x+2][n1.y] == Tileset.WALL)
            {
                world[n1.x+2][n1.y] = Tileset.LOCKED_DOOR;
            }
            else if(world[n1.x][n1.y-2] == Tileset.WALL)
            {
                world[n1.x][n1.y-2] = Tileset.LOCKED_DOOR;
            }
            else {world[n1.x][n1.y+2] = Tileset.LOCKED_DOOR; }
        }
        else world[n1.x][n1.y] = Tileset.LOCKED_DOOR;
        /*node n1 = NodeFromRoom((room)room_list[i]);
        if(world[n1.x+1][n1.y] == Tileset.WALL)
        {
            world[n1.x+1][n1.y] = Tileset.LOCKED_DOOR;
            //Draw_grid(n1,Tileset.LOCKED_DOOR);
        }
        else if(world[n1.x-1][n1.y] == Tileset.WALL)
        {
            world[n1.x-1][n1.y] = Tileset.LOCKED_DOOR;
            //Draw_grid(n1,Tileset.LOCKED_DOOR);
        }
        else if(world[n1.x][n1.y+1] == Tileset.WALL)
        {
            world[n1.x][n1.y+1] = Tileset.LOCKED_DOOR;
            //Draw_grid(n1,Tileset.LOCKED_DOOR);
        }
        else if(world[n1.x][n1.y-1] == Tileset.WALL)
        {
            world[n1.x][n1.y-1] = Tileset.LOCKED_DOOR;
            //Draw_grid(n1,Tileset.LOCKED_DOOR);
        }
        else GenerateDoor(seed1+1);*/
    }
    public void GenerateWorld()
    {
        InitWorld();
        Random r = new Random(seed);
        GenerateRoom_Randomly(RandomUtils.uniform(r,1,15));
        GenerateHallways_AllRoom();
        GeneratePlayer();
        GenerateDoor();
    }
}
