package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static gitlet.Utils.readObject;
import static gitlet.Utils.writeContents;

public class Methods_myself {
    public static void mkdir(File f)
    {
        if(!f.exists())
        {
            f.mkdir();
        }
    }
    //在某个目录下找某个id的文件
    public static File find_id(File dir,String id)
    {
        File f = Utils.join(dir,id);
        if(!f.exists()) return null;
        return f;
    }
    //在某个目录下找某个名字的文件
    public static File find_name(File dir,String name)
    {
        File f = Utils.join(dir,name);
        if(!f.exists()) return null;
        return f;
    }
    //检查某个文件是否存在
    public static boolean check_file_exist(File dir, String name)
    {
        File f = Utils.join(dir,name);
        if(!f.exists()) return false;
        return true;
    }
    //读出某个文件中的String
    public static String read(File dir,String name)
    {
        File f = Utils.join(dir,name);
        if(!f.exists()) return null;
        return Utils.readContentsAsString(f);
    }
    //创建一个文件
    public static File make_file(File dir, String name)
    {
        File f = Utils.join(dir,name);
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return f;
    }
    //找到head中存放的commit
    public static Commit head_commit()
    {
        String Head_id = Methods_myself.read(Repository.GITLET_DIR,"HEAD");
        File head = Methods_myself.find_id(Repository.DIR_Commits,Head_id);
        Commit head1 = readObject(head,Commit.class);
        return head1;
    }
    //利用id找到对应的commit
    public static Commit find_commit(String id)
    {
        File head = Methods_myself.find_id(Repository.DIR_Commits,id);
        if(head == null) return null;
        Commit head1 = readObject(head, Commit.class);
        return head1;
    }
    //利用部分id找到对应的commit
    public static Commit find_commit_part(String id)
    {
        File dir = Repository.DIR_Commits;
        for(File f:dir.listFiles())
        {
            String name = f.getName();
            if(id.length() > name.length()) continue;
            if(id.equals(name.substring(0,id.length())))
            {
                Commit head1 = readObject(f, Commit.class);
                return head1;
            }
        }
        return null;
    }
    //对dir的文件名字进行排序
    public static List<String> list_sort(File dir)
    {
        List<String> l = new ArrayList<>();
        for(File f: dir.listFiles())
        {
            l.add(f.getName());
        }
        //从小到大排序
        l.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return l;
    }
    //将cont的内容写到dir+name的位置处
    public static void write_cont(File dir,String name,String cont)
    {
        File f = Utils.join(dir,name);
        if(f.exists()) f.delete();
        //新创建这个文件，并把commit中的Blob的内容写进去
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(f,cont);
    }
}
