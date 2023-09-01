package gitlet;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;
    private String timestamp;
    private String parent;
    private String UID;
    private List<String> BlobIDs;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",Locale.ENGLISH);
    //init中调用的
    public Commit(String message)
    {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));
        this.message = message;
        /*TODO 怎么得到时间？ get!*/
        Date d = new Date(0);
        this.timestamp = sdf.format(d);
        this.parent = null;
        this.BlobIDs = new ArrayList<>();
        this.UID = Utils.sha1(this.message,this.timestamp);
    }
    //之后调用的
    public Commit (Commit c,String new_message)
    {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));
        this.message = new_message;
        this.parent = c.getUID();
        Date d = new Date();
        this.timestamp = sdf.format(d);
        this.BlobIDs = new ArrayList<>();
        for(int i=0; i<c.BlobIDs.size();++i)
        {
            BlobIDs.add(c.BlobIDs.get(i));
        }
        this.UID = Utils.sha1(this.message,this.timestamp);
    }
    public String getMessage()
    {
        return this.message;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }

    public String getParent()
    {
        return this.parent;
    }

    public String getUID()
    {
        return this.UID;
    }
    //返回Blobid长度
    public int len_Blog()
    {
        return this.BlobIDs.size();
    }
    public String getBlobid(int i)
    {
        return this.BlobIDs.get(i);
    }
    //删除Blob中的第i项
    public void removeBlobid(int i)
    {
        BlobIDs.remove(i);
    }
    //检验相同的name和cont是不是已经被这个commit记录了，
    //记录了的话add就不用再记录了
    public boolean check_contents_equal(String name, String cont)
    {
        for(int i=0; i<BlobIDs.size(); ++i)
        {
            String id = BlobIDs.get(i);
            File f= Methods_myself.find_id(Repository.DIR_Blobs,id);
            Blob b = Utils.readObject(f, Blob.class);
            if(b.getContent().equals(cont) && b.getName().equals(name)) return true;
        }
        return false;
    }
    //向Blobid中添加内容，如果重复则不添加
    public boolean add_Blob(String new_id)
    {
        if(!this.BlobIDs.contains(new_id))
        {
            this.BlobIDs.add(new_id);
            return true;
        }
        return false;
    }
    //从Blobid中删除内容
    public boolean remove_Blob(String new_id)
    {
        if(this.BlobIDs.contains(new_id))
        {
            this.BlobIDs.remove(new_id);
            return true;
        }
        return false;
    }
    //在Blobid中判断是否存在某Id
    public boolean check_Blob(String new_id)
    {
        if(this.BlobIDs.contains(new_id))
        {
            return true;
        }
        return false;
    }
    //在Blobid中搜索某个名字对应的Blob,返回Blob
    public Blob find_Blob_name(String name)
    {
        for(int i=0; i<BlobIDs.size(); ++i)
        {
            String id = BlobIDs.get(i);
            File f = Methods_myself.find_id(Repository.DIR_Blobs,id);
            Blob b = Utils.readObject(f, Blob.class);
            if(b.getName().equals(name))
            {
                return b;
            }
        }
        return null;
    }
    //在Blobid中搜索某个名字对应的Blob,返回i
    public int find_Blob_name_return_i(String name)
    {
        for(int i=0; i<BlobIDs.size(); ++i)
        {
            String id = BlobIDs.get(i);
            File f = Methods_myself.find_id(Repository.DIR_Blobs,id);
            Blob b = Utils.readObject(f, Blob.class);
            if(b.getName().equals(name))
            {
                return i;
            }
        }
        return -1;
    }
    //找到Blobid中的第i项
    public Blob find_Blob(int i)
    {
        String id = BlobIDs.get(i);
        File f = Methods_myself.find_id(Repository.DIR_Blobs,id);
        Blob b = Utils.readObject(f, Blob.class);
        return b;
    }
    /* TODO: fill in the rest of this class. */
}
