package gitlet;



//import net.sf.saxon.expr.Component;

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
            if(Methods_myself.check_file_exist(DIR_stage_addition,name))
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
        if(Methods_myself.check_file_exist(DIR_stage_removal,name))
        {
            File f1 = Utils.join(DIR_stage_removal,name);
            f1.delete();
        }
    }
    public void commitTask(String message)
    {
        if(message.equals(""))
        {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        //调用第三种构造方式：以HEAD中的commit为基础
        Commit new_commit = new Commit(Methods_myself.head_commit(),message);
        //遍历stage_add目录，把其中的Blobs加入
        if((DIR_stage_addition.listFiles().length + DIR_stage_removal.listFiles().length)== 0)
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
        File f = Utils.join(CWD,name);
        //in_stage判断是否在stage_add中
        boolean in_stage = Methods_myself.check_file_exist(DIR_stage_addition,name);
        //in_commit判断是否在cur_commit的关注中
        //同时记录一下这个文件的id
        Commit c = Methods_myself.head_commit();
        boolean in_commit = false;
        int i = c.find_Blob_name_return_i(name);
        String Blob_id = null;
        if(i >= 0)
        {
            Blob_id = c.getBlobid(i);
            in_commit = true;
        }
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
            if(f.exists()) f.delete();
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
    public void statusTask()
    {
        if(!Methods_myself.check_file_exist(CWD, ".gitlet"))
        {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
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
        Methods_myself.write_cont(CWD,name,b.getContent());
    }
    public void checkout2Task(String id,String name)
    {
        //通过部分id索引来找到对应的commit
        Commit head = Methods_myself.find_commit_part(id);
        if(head == null)
        {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        //后续与checkout1相同
        Blob b = head.find_Blob_name(name);
        if(b == null)
        {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Methods_myself.write_cont(CWD,name,b.getContent());
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
        String id = readContentsAsString(f);
        Commit now = readObject(Utils.join(DIR_Commits,id),Commit.class);
        //和cur_branch比较一下是否相同
        File cur = Utils.join(GITLET_DIR,"cur_branch");
        String n = readContentsAsString(cur);
        if(n.equals(name))
        {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        //动态数组记录now跟踪的文件名,方便之后和旧head的比较
        List<String> l_new = new ArrayList<>();
        //动态数组记录旧head跟踪的文件名
        List<String> l_old = new ArrayList<>();
        //记录旧head跟踪的文件名
        Commit head = Methods_myself.head_commit();
        for(int i=0; i<head.len_Blog(); i++)
        {
            Blob b = head.find_Blob(i);
            l_old.add(b.getName());
        }
        //将now跟踪的Blob内容更新到working directory
        for(int i=0; i<now.len_Blog(); i++)
        {
            Blob b = now.find_Blob(i);
            l_new.add(b.getName());
            File f1 = Utils.join(CWD,b.getName());
            //如果f1存在并且old_head中没有记录它，那就报错退出
            if(f1.exists() && !l_old.contains(b.getName()))
            {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            Methods_myself.write_cont(CWD,b.getName(),b.getContent());
        }
        //将stage清空
        for(File f2: DIR_stage_removal.listFiles())
        {
            f2.delete();
        }
        for(File f2: DIR_stage_addition.listFiles())
        {
            f2.delete();
        }
        //与旧head跟踪的内容进行比较，如果旧head有而now没有，则把它删去
        for(int i=0; i<l_old.size(); ++i)
        {
            String name1 = l_old.get(i);
            File f2 = Utils.join(CWD,name1);
            if(!l_new.contains(name1) && f2.exists())
            {
                //System.out.println(name1);
                f2.delete();
            }
        }
        //更新cur_branch与HEAD
        Methods_myself.write_cont(GITLET_DIR,"cur_branch",name);
        Methods_myself.write_cont(GITLET_DIR,"HEAD",now.getUID());
    }
    public void branchTask(String branch_name)
    {
        //已存在则报错
        if(Methods_myself.check_file_exist(DIR_Branches,branch_name))
        {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        //没有存在就创建并把head的内容写进去
        File f = Methods_myself.make_file(DIR_Branches,branch_name);
        String id = readContentsAsString(Utils.join(GITLET_DIR,"HEAD"));
        writeContents(f,id);
    }
    public void rmbranchTask(String branch_name)
    {
        //没有存在则报错
        if(!Methods_myself.check_file_exist(DIR_Branches,branch_name))
        {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        //如果是删除cur_branch则报错
        String cur_branch = Methods_myself.read(GITLET_DIR,"cur_branch");
        if(cur_branch.equals(branch_name))
        {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        //把这个文件删除
        File f = Methods_myself.find_name(DIR_Branches,branch_name);
        f.delete();
    }
    public void resetTask(String id)
    {
        Commit now = Methods_myself.find_commit_part(id);
        if(now == null)
        {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        //动态数组记录now跟踪的文件名,方便之后和旧head的比较
        List<String> l_new = new ArrayList<>();
        //动态数组记录旧head跟踪的文件名
        List<String> l_old = new ArrayList<>();
        //记录旧head跟踪的文件名
        Commit head = Methods_myself.head_commit();
        for(int i=0; i<head.len_Blog(); i++)
        {
            Blob b = head.find_Blob(i);
            l_old.add(b.getName());
        }
        //将now跟踪的Blob内容更新到working directory
        for(int i=0; i<now.len_Blog(); i++)
        {
            Blob b = now.find_Blob(i);
            l_new.add(b.getName());
            File f1 = Utils.join(CWD,b.getName());
            //如果f1存在并且old_head中没有记录它，那就报错退出
            if(f1.exists() && !l_old.contains(b.getName()))
            {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            Methods_myself.write_cont(CWD,b.getName(),b.getContent());
        }
        //将stage清空
        for(File f2: DIR_stage_removal.listFiles())
        {
            f2.delete();
        }
        for(File f2: DIR_stage_addition.listFiles())
        {
            f2.delete();
        }
        //与旧head跟踪的内容进行比较，如果旧head有而now没有，则把它删去
        for(int i=0; i<l_old.size(); ++i)
        {
            String name1 = l_old.get(i);
            File f2 = Utils.join(CWD,name1);
            if(!l_new.contains(name1) && f2.exists())
            {
                //System.out.println(name1);
                f2.delete();
            }
        }
        //更新HEAD
        Methods_myself.write_cont(GITLET_DIR,"HEAD",now.getUID());
        //更新当前branch的head
        String cur_branch = readContentsAsString(Utils.join(GITLET_DIR,"cur_branch"));
        File f = Utils.join(DIR_Branches,cur_branch);
        writeContents(f,now.getUID());
    }
    public void mergeTask(String branch_name)
    {
        if(DIR_stage_addition.listFiles().length+DIR_stage_removal.listFiles().length >0)
        {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        Commit head_commit = Methods_myself.head_commit();
        //找到branch_name对应的Commit
        String id = readContentsAsString(Utils.join(DIR_Branches,branch_name));
        Commit branch = Methods_myself.find_commit(id);
        //如果不存在这个branch，报错退出
        if(branch == null)
        {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        //如果branch就是head，报错退出
        if(branch.getTimestamp().equals(head_commit.getTimestamp()) && branch.getMessage().equals(head_commit.getMessage()))
        {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        //找到split node(共同的祖先)
        Commit ancestor = Methods_myself.find_common_ancestor(branch,head_commit);
        if(ancestor.getTimestamp().equals(branch.getTimestamp()) && ancestor.getMessage().equals(branch.getMessage()))
        {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        else if(ancestor.getMessage().equals(head_commit.getMessage()) && ancestor.getTimestamp().equals(head_commit.getTimestamp()))
        {
            System.out.println("Current branch fast-forwarded.");
            checkout3Task(branch_name);
            return;
        }
        //
        HashMap<String,String> head_hash = head_commit.blob_name_content();
        HashMap<String,String> branch_hash = branch.blob_name_content();
        HashMap<String,String> ancestor_hash = ancestor.blob_name_content();
        for(String name:ancestor_hash.keySet())
        {
            //如果head中有这个文件
            if(head_hash.containsKey(name))
            {
                if(branch_hash.containsKey(name))
                {
                    //对应情况1
                    if(ancestor_hash.get(name).equals(head_hash.get(name))
                    && (!branch_hash.get(name).equals(ancestor_hash.get(name))))
                    {
                        //将head中原有的这个blobid删取，加上新的id
                        String cont = branch_hash.get(name);
                        String new_id = sha1(name,cont);
                        int i = head_commit.find_Blob_name_return_i(name);
                        head_commit.removeBlobid(i);
                        head_commit.add_Blob(new_id);
                    }
                    //对应情况8
                    if((!branch_hash.get(name).equals(ancestor_hash.get(name)))
                    && (!head_hash.get(name).equals(ancestor_hash.get(name)))
                    && (!head_hash.get(name).equals(branch_hash.get(name)))
                    )
                    {
                        Methods_myself.merge_func_8(name,head_hash.get(name),branch_hash.get(name));
                    }
                }
                else
                {
                    //对应情况6
                    if(ancestor_hash.get(name).equals(head_hash.get(name)))
                    {
                        Methods_myself.remove_file(CWD,name);
                        String id1 = sha1(name,head_hash.get(name));
                        head_commit.remove_Blob(id1);
                    }
                    //对应情况8
                    else
                    {
                        Methods_myself.merge_func_8(name,head_hash.get(name),"");
                    }
                }
            }
            if(branch_hash.containsKey(name))
            {
                if(!head_hash.containsKey(name))
                {
                    //对应情况8
                    if(!ancestor_hash.get(name).equals(branch_hash.get(name)))
                    {
                        Methods_myself.merge_func_8(name,"",branch_hash.get(name));
                    }
                }
            }
        }
        for(String name:branch_hash.keySet())
        {
            //对应情况5：
            if((!ancestor_hash.containsKey(name)) && (!head_hash.containsKey(name)))
            {
                //找到branch中与name对应的Blob
                Blob b = branch.find_Blob_name(name);
                Methods_myself.write_cont(CWD,name,b.getContent());

                String id1 = sha1(name,branch_hash.get(name));
                Methods_myself.write_cont(DIR_stage_addition,name,id1);
            }
            //对应情况8
            if((!ancestor_hash.containsKey(name)) && head_hash.containsKey(name)
                    && (!head_hash.get(name).equals(branch_hash.get(name)))
            )
            {
                Methods_myself.merge_func_8(name,head_hash.get(name),branch_hash.get(name));
            }
        }
        //情况2 3 4 7都是啥也不干
    }
}
