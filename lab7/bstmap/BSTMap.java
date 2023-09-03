package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable<K>,V>implements Map61B<K,V>
{
    private K key;
    private V value;
    private BSTMap<K,V> left;
    private BSTMap<K,V> right;

    public BSTMap()
    {
        this.key = null;
        this.value = null;
        this.left = null;
        this.right =null;
        //this.left = new BSTMap<>(null,null,null,null);
        //this.right = new BSTMap<>(null,null,null,null);
    }

    @Override
    public void clear()
    {
        this.key = null;
        this.value = null;
        //this.left = new BSTMap<>(null,null,null,null);
        //this.right = new BSTMap<>(null,null,null,null);
    }

    @Override
    public boolean containsKey(K key) {
        if(this.key == null) return false;
        if(this.key.equals(key)) return true;
        else
        {
            return (left.containsKey(key)|| right.containsKey(key));
        }
    }

    @Override
    public V get(K key) {
        if(containsKey(key) == false) return null;
        //说明一定会有
        if(this.key.equals(key)) return this.value;
        else if (this.key.compareTo(key) > 0)
        {
            return this.left.get(key);
        }
        else return this.right.get(key);
    }

    @Override
    public int size() {
        if(this.key == null) return 0;
        return 1+ left.size()+right.size();
    }

    @Override
    public void put(K key, V value)
    {
        if(this.key == null)
        {
            this.key = key;
            this.value = value;
            this.left = new BSTMap<>();
            this.right = new BSTMap<>();
            return;
        }
        //小的话就加在左边
        if(this.key.compareTo(key) > 0)
        {
            this.left.put(key,value);
        }
        else this.right.put(key,value);
    }

    @Override
    public Set<K> keySet() {
        if(this.key == null) return null;
        Set<K> s = new HashSet<>();
        s.add(this.key);
        Set<K> s1 = left.keySet();
        Set<K> s2 = right.keySet();
        if(s1 != null)
        {
            for(K k:s1)
            {
                s.add(k);
            }
        }
        if(s2 != null)
        {
            for(K k:s2)
            {
                s.add(k);
            }
        }
        return s;
    }

    @Override
    public V remove(K key)
    {
        if(!containsKey(key)) return null;
        if(this.key.equals(key))
        {
            V val = this.value;
            //没有子节点：直接删去
            if(left.key == null && right.key == null)
            {
                clear();
            }
            //只有一个子节点
            else if(left.key == null)
            {
                this.key = right.key;
                this.value = right.value;
                this.left =  right.left;
                this.right = right.right;
            }
            else if(right.key == null)
            {
                this.key = left.key;
                this.value = left.value;
                this.right = left.right;
                this.left =  left.left;
            }
            //有两个子节点：替换
            else
            {
                this.key = find_right1(this);
                this.value = find_right2(this);
            }
            return val;
        }
        else if(this.key.compareTo(key) > 0)
        {
            return this.left.remove(key);
        }
        else return this.right.remove(key);
    }

    private K find_right1(BSTMap<K,V> b)
    {
        if(b.right.value == null)
        {
            return b.key;
        }
        return find_right1(b.right);
    }

    private V find_right2(BSTMap<K,V> b)
    {
        if(b.right.value == null)
        {
            return b.value;
        }
        return find_right2(b.right);
    }

    @Override
    public V remove(K key, V value)
    {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public void printInOrder()
    {
        if(this.key == null) return;
        this.left.printInOrder();
        System.out.println(this.value);
        this.right.printInOrder();
    }

}
