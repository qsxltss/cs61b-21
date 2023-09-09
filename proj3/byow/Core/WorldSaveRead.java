package byow.Core;

import org.antlr.v4.runtime.misc.Utils;

import java.io.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static byow.Core.MyUtils.*;

public class WorldSaveRead {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void saveWorld(WorldGenerator w)
    {
        CWD.setWritable(true);
        CWD.setReadable(true);
        CWD.setExecutable(true);
        File f = join(CWD,"SaveWorld.dat");
        if(!f.exists())
        {
            try {
                f.createNewFile();
                f.setWritable(true);
                f.setExecutable(true);
                f.setReadable(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeObject(f,w);
        /* 这也是可以的
        try {
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream("SaveWorld.dat");

            // 创建对象输出流
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // 将数组写入文件
            oos.writeObject(w);

            // 关闭对象输出流和文件输出流
            oos.close();
            fos.close();

            System.out.println("数组已序列化并写入文件 " + "SaveWorld.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static WorldGenerator readWorld(String filename)
    {
        File f = join(CWD,filename);
        if(!f.exists())
        {
            return null;
        }
        WorldGenerator w = readObject(f, WorldGenerator.class);
        return w;
        /*try {
            // 创建文件输入流
            FileInputStream fis = new FileInputStream(filename);
            // 创建对象输入流
            ObjectInputStream ois = new ObjectInputStream(fis);

            // 从文件中读取数组
            WorldGenerator myWorld = (WorldGenerator) ois.readObject();

            // 关闭对象输入流和文件输入流
            ois.close();
            fis.close();
            return myWorld;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;*/
    }
}
