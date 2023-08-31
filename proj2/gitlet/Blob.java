package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    private String name;
    private String content;

    public Blob(String name,String content)
    {
        this.name = name;
        this.content = content;
    }
    public String getContent()
    {
        return this.content;
    }
    public String getName()
    {
        return this.name;
    }
}
