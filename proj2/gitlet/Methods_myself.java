package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    public static boolean remove_file(File dir, String name)
    {
        File f = Utils.join(dir,name);
        if(f.exists())
        {
            f.delete();
            return true;
        }
        else return false;
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
    //找到两个commit的共同祖先
    public static Commit find_common_ancestor(Commit a,Commit b)
    {
        List<String> l11 = new ArrayList<>();
        List<String> l12 = new ArrayList<>();
        List<String> l21 = new ArrayList<>();
        List<String> l22 = new ArrayList<>();
        Commit c = a;
        while(c!=null)
        {
            l11.add(c.getUID());
            if(c.getParent() == null) c =null;
            else c = Methods_myself.find_commit(c.getParent());
        }
        c = a;
        while(c!=null)
        {;
            l12.add(c.getUID());
            if(c.getParent2() == null) c =null;
            else c = Methods_myself.find_commit(c.getParent2());
        }
        c = b;
        while(c!=null)
        {
            l21.add(c.getUID());
            if(c.getParent() == null) c =null;
            else c = Methods_myself.find_commit(c.getParent());
        }
        c = b;
        while(c!=null)
        {
            l22.add(c.getUID());
            if(c.getParent2() == null) c =null;
            else c = Methods_myself.find_commit(c.getParent2());
        }
        String l11_id = null;
        String l12_id = null;
        int l11_len = 10000;
        int l12_len = 10000;
        for(int i=0; i<l11.size(); ++i)
        {
            String id = l11.get(i);
            if(l21.contains(id) || l22.contains(id))
            {l11_id = id; l11_len = i+1; break;}
        }
        for(int i=0; i<l12.size(); ++i)
        {
            String id = l11.get(i);
            if(l21.contains(id) || l22.contains(id))
            {l12_id = id; l12_len = i+1; break;}
        }
        if(l11_len <= l12_len && l11_len!=10000)
        {
            return Methods_myself.find_commit(l11_id);
        }
        else if(l12_len < l11_len && l12_len!= 10000)
        {
            return Methods_myself.find_commit(l12_id);
        }
        return null;
    }
    public static void merge_func_8(String name,String head,String branch)
    {
        System.out.println("Encountered a merge conflict.");
        String str = "<<<<<<< HEAD\n"+head+"=======\n"+branch+">>>>>>>\n";
        write_cont(Repository.CWD,name,str);
    }
    //
    public static boolean check_commit_equal(Commit a,Commit b)
    {
        if(a.getTimestamp().equals(b.getTimestamp()) && a.getMessage().equals(b.getMessage()))
        {
            return true;
        }
        return false;
    }
}
