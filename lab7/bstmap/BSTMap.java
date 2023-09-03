package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable<K>,V>implements Map61B<K,V>
{
    private K key;
    private V value;
    private BSTMap<K,V> left;
    private BSTMap<K,V> right;

    public BSTMap(K key,V value,BSTMap<K,V> l, BSTMap<K,V> r)
    {
        this.key = key;
        this.value = value;
        this.left = l;
        this.right = r;
    }

    public BSTMap()
    {
        this.key = null;
        this.value = null;
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
            this.left = new BSTMap<>(null,null,null,null);
            this.right = new BSTMap<>(null,null,null,null);
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
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
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
