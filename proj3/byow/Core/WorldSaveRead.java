package byow.Core;

import java.io.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WorldSaveRead {
    public static void saveWorld(WorldGenerator w,String filename)
    {
        try {
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(filename);

            // 创建对象输出流
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // 将数组写入文件
            oos.writeObject(w);

            // 关闭对象输出流和文件输出流
            oos.close();
            fos.close();

            System.out.println("数组已序列化并写入文件 " + filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static WorldGenerator readWorld(String filename)
    {
        try {
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
        return null;
    }
}
