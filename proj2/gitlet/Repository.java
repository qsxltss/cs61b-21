package gitlet;



import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author wpl
 */
public class Repository implements Serializable {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File DIR_stage_addition = join(GITLET_DIR,"Staged_for_addition");
    public static final File DIR_stage_removal = join(GITLET_DIR,"Staged_for_removal");
    public static final File DIR_Commits = join(GITLET_DIR,"Commits");
    public static final File DIR_Blobs = join(GITLET_DIR,"Blobs");
    public static final File DIR_Branches = join(GITLET_DIR,"Branches");
    //Head存储当前commits的id
    //private static String Head = null;
    //private static HashMap<String,String> branchs = new HashMap<>();
    /* TODO: fill in the rest of this class. */

    /*Creates a new Gitlet version-control system in the current
    directory. This system will automatically start with one commit:
     a commit that contains no files and has the commit message
     initial commit (just like that, with no punctuation).
     It will have a single branch: master, which initially points
     to this initial commit, and master will be the current branch.
      The timestamp for this initial commit will be
      00:00:00 UTC, Thursday, 1 January 1970
      in whatever format you choose for dates (this is called “The (Unix) Epoch”, represented internally by the time 0.)
      Since the initial commit in all repositories created by Gitlet
      will have exactly the same content, it follows that all
      repositories will automatically share this commit (they will all
       have the same UID) and all commits in all repositories will
       trace back to it.
     */
    public void initTask()
    {
        //创建.gitlet的目录
        File git_dir = GITLET_DIR;
        if(git_dir.exists())
        {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        git_dir.mkdir();
        //创建最早的commit init
        Commit init = new Commit("initial commit");
        //创建保存commits的目录
        Methods_myself.mkdir(DIR_Commits);
        //创建保存blobs的目录
        Methods_myself.mkdir(DIR_Blobs);
        //创建保存stage_add的目录
        Methods_myself.mkdir(DIR_stage_addition);
        //创建保存stage_remove的目录
        Methods_myself.mkdir(DIR_stage_removal);
        //创建保存Branches的目录
        Methods_myself.mkdir(DIR_Branches);
        //把init存下来
        File init_save = Utils.join(DIR_Commits,init.getUID());
        Utils.writeObject(init_save,init);
        //在branches的目录中创建master和head，并将Commit init的id存在里面
        File master = Methods_myself.make_file(DIR_Branches,"master");
        File Head = Methods_myself.make_file(GITLET_DIR,"HEAD");
        Utils.writeContents(master,init.getUID());
        Utils.writeContents(Head,init.getUID());
        //在.Gitlet的目录中创建cur_branch,将“master”写入其中
        File cur_branch = Methods_myself.make_file(GITLET_DIR,"cur_branch");
        writeContents(cur_branch,"master");
    }
    /*Adds a copy of the file as it currently exists to the staging
    area (see the description of the commit command). For this reason,
     adding a file is also called staging the file for addition.
     Staging an already-staged file overwrites the previous entry in
     the staging area with the new contents. The staging area should
     be somewhere in .gitlet. If the current working version of the file
      is identical to the version in the current commit, do not stage
      it to be added, and remove it from the staging area if it is already
       there (as can happen when a file is changed, added, and then
       changed back to it’s original version). The file will no longer
       be staged for removal (see gitlet rm), if it was at the time of
       the command.
     */
    public void addTask(String name)
    {
        File f = Utils.join(CWD,name);
        if(!f.exists())
        {
            //不存在这个文件就报错
            System.out.println("File does not exist.");
            System.exit(0);
        }
        String cont = readContentsAsString(f);
        //如果跟现在commit的内容一样，就不更新在stage_add中
        Commit head1 = Methods_myself.head_commit();
        if(head1.check_contents_equal(name,cont))
        {
            //如果已经在stage_add中，就去除它
            if(Methods_myself.check_exist_name(DIR_stage_addition,name))
            {
               File f1 = Utils.join(DIR_stage_addition,name);
               f1.delete();
            }
        }
        //不一样就创建一个Blob来记录这个文件的内容，然后创建一个stage_Add来存储Blob的编号
        else
        {
            //创建Blob
            String id = sha1(name,cont);
            File f1 = Methods_myself.make_file(DIR_Blobs,id);
            Blob b = new Blob(name,cont);
            writeObject(f1,b);
            //创建stage_add
            File f2 = Methods_myself.make_file(DIR_stage_addition,name);
            writeContents(f2,id);
        }
        if(Methods_myself.check_exist_name(DIR_stage_removal,name))
        {
            File f1 = Utils.join(DIR_stage_removal,name);
            f1.delete();
        }
    }
    public void commitTask(String message)
    {
        if(message == "")
        {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        //调用第三种构造方式：以HEAD中的commit为基础
        Commit new_commit = new Commit(Methods_myself.head_commit(),message);
        //遍历stage_add目录，把其中的Blobs加入
        if(DIR_stage_addition.listFiles().length == 0)
        {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        for(File f: DIR_stage_addition.listFiles())
        {
            String name = f.getName();
            int m = new_commit.find_Blob_name_return_i(name);
            if(m >=0)
            {
                new_commit.removeBlobid(m);
            }
            String id = readContentsAsString(f);
            new_commit.add_Blob(id);
            f.delete();
        }
        //遍历stage_remove目录，把其中的Blobs删去
        for(File f: DIR_stage_removal.listFiles())
        {
            String id = readContentsAsString(f);
            new_commit.remove_Blob(id);
            f.delete();
        }
        //将commit储存在Dir_commit中
        File save = Utils.join(DIR_Commits,new_commit.getUID());
        Utils.writeObject(save,new_commit);
        //将Head和cur_branch更新
        String cur_branch_name = readContentsAsString(Utils.join(GITLET_DIR,"cur_branch"));
        File cur_branch = Methods_myself.find_name(DIR_Branches,cur_branch_name);
        File Head = Methods_myself.find_name(GITLET_DIR,"HEAD");
        Utils.writeContents(cur_branch,new_commit.getUID());
        Utils.writeContents(Head,new_commit.getUID());
    }
    public void rmTask(String name)
    {
        //找到对应文件的Blob_id
        File f = Utils.join(CWD,name);
        if(!f.exists())
        {
            //不存在这个文件就退出
            System.exit(0);
        }
        String cont = readContentsAsString(f);
        String Blob_id = sha1(name,cont);
        //in_stage判断是否在stage_add中
        boolean in_stage = Methods_myself.check_exist_name(DIR_stage_addition,name);
        Commit c = Methods_myself.head_commit();
        //in_commit判断是否在cur_commit的关注中
        boolean in_commit = c.check_Blob(Blob_id);
        //如果都不在的话，就退出
        if(!in_stage && !in_commit)
        {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        //在stage_add中就删去
        if(in_stage)
        {
            File f1 = Utils.join(DIR_stage_addition,name);
            f1.delete();
        }
        //在commit中就把这个文件添加到stage_removal中并把文件删除
        if(in_commit)
        {
            File f2 = Methods_myself.make_file(DIR_stage_removal,name);
            writeContents(f2,Blob_id);
            f.delete();
        }
    }
    public void logTask()
    {
        Commit c = Methods_myself.head_commit();
        while(c!=null)
        {
            System.out.println("===");
            System.out.println("commit "+ c.getUID());
            System.out.println("Date: "+ c.getTimestamp());
            System.out.println(c.getMessage());
            System.out.println();
            if(c.getParent() == null) c =null;
            else c = Methods_myself.find_commit(c.getParent());
        }
    }
    public void globallogTask()
    {
       for (File f:DIR_Commits.listFiles())
       {
           Commit c = readObject(f,Commit.class);
           System.out.println("===");
           System.out.println("commit "+ c.getUID());
           System.out.println("Date: "+ c.getTimestamp());
           System.out.println(c.getMessage());
           System.out.println();
       }
    }
    public void findTask(String message)
    {
        int count = 0;
        for (File f:DIR_Commits.listFiles())
        {
            Commit c = readObject(f,Commit.class);
            if(c.getMessage().equals(message))
            {
                System.out.println(c.getUID());
                count++;
            }
        }
        if(count == 0)
        {
            System.out.println("Found no commit with that message");
        }
    }
    public void staticTask()
    {
        //先输出Branches
        System.out.println("=== Branches ===");
        //找到当前的branch，因为前面要加星号
        File f1 = Utils.join(GITLET_DIR,"cur_branch");
        String name = readContentsAsString(f1);
        //遍历所有DIR_branches中的文件并将它们的名字存放到list中
        List<String> l = Methods_myself.list_sort(DIR_Branches);
        //输出
        for(int i=0; i<l.size();++i)
        {
            String name1 = l.get(i);
            if(!name1.equals(name))
            {
                System.out.println(name1);
            }
            else System.out.println("*"+name1);
        }
        System.out.println();
        //再输出Staged Files
        System.out.println("=== Staged Files ===");
        List<String> l1 = Methods_myself.list_sort(DIR_stage_addition);
        for(int i=0; i<l1.size();++i)
        {
            System.out.println(l1.get(i));
        }
        System.out.println();
        //再输出Removed Files
        System.out.println("=== Removed Files ===");
        List<String> l2 = Methods_myself.list_sort(DIR_stage_removal);
        for(int i=0; i<l2.size();++i)
        {
            System.out.println(l2.get(i));
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        //TODO EXTRA POINT//
        System.out.println();
        System.out.println("=== Untracked Files ===");
        //TODO EXTRA POINT//
        System.out.println();
    }
    public void checkout1Task(String name)
    {
        //找到head指向的commit
        Commit head = Methods_myself.head_commit();
        //找到commit中与name对应的Blob
        Blob b = head.find_Blob_name(name);
        if(b == null)
        {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        //看现在有没有这个文件，有的话就给删除掉
        File f = Utils.join(CWD,name);
        if(f.exists()) f.delete();
        //新创建这个文件，并把commit中的Blob的内容写进去
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(f,b.getContent());
    }
    public void checkout2Task(String id,String name)
    {
        //通过部分id索引来找到对应的commit
        Commit head = Methods_myself.find_commit_part(id);
        if(head == null)
        {
            System.out.println("No commit with that id exists.");
        }
        //后续与checkout1相同
        Blob b = head.find_Blob_name(name);
        if(b == null)
        {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File f = Utils.join(CWD,name);
        if(f.exists()) f.delete();
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(f,b.getContent());
    }
    public void checkout3Task(String name)
    {
        //找到这个name对应的branch的commit
        File f = Utils.join(DIR_Branches,name);
        if(!f.exists())
        {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        //和cur_branch比较一下是否相同
        File cur = Utils.join(GITLET_DIR,"cur_branch");
        String n = readContentsAsString(cur);
        if(n.equals(name))
        {
            System.out.println("o need to checkout the current branch.");
        }
    }
}
