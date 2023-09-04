package hashmap;

import java.util.*;

import static java.lang.Math.abs;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private double loadFactor = 0.75;
    private int size = 16;
    private int num = 0;

    /** Constructors */
    public MyHashMap()
    {
        buckets = this.createTable(size);
        //if(buckets[0] != null) System.out.println("12345");
    }

    public MyHashMap(int initialSize)
    {
        buckets = this.createTable(initialSize);
        size = initialSize;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad)
    {
        buckets = this.createTable(initialSize);
        size = initialSize;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value)
    {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket()
    {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize)
    {
        Collection<Node>[] buckets = new Collection[tableSize];
        for(int i=0;i<tableSize;i++)
        {
            buckets[i] = this.createBucket();
        }
        return buckets;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear()
    {
        this.buckets = createTable(16);
        this.size = 16;
        this.num = 0;
    }

    @Override
    public boolean containsKey(K key)
    {
        int id = abs(key.hashCode()%size);
        //System.out.println("buckets.size "+buckets.length);
        Collection<Node> nodes = buckets[id];
        if(nodes.size() == 0) return false;
        for(Node n:nodes)
        {
            if(n.key.equals(key)) return true;
        }
        return false;
    }

    @Override
    public int size()
    {
        return this.num;
    }

    @Override
    public V get(K key)
    {
        int id = abs(key.hashCode()%size);
        Collection<Node> nodes = buckets[id];
        if(nodes.size() == 0) return null;
        for(Node n:nodes)
        {
            if(n.key.equals(key)) return n.value;
        }
        return null;
    }

    @Override
    public void put(K key, V value)
    {
        if(this.containsKey(key))
        {
            int id = abs(key.hashCode()%size);
            Collection<Node> nodes = buckets[id];
            for(Node n:nodes)
            {
                if(n.key.equals(key)) n.value = value;
            }
            return;
        }
        //如果超过了负载就更改size
        if( (num+1)/size > loadFactor)
        {
            Collection<Node>[] buckets_new = new Collection[2*size];
            for(int i=0;i<size;i++)
            {
                buckets_new[i] = this.buckets[i];
            }
            for(int i=size; i<size*2; i++)
            {
                buckets_new[i] = createBucket();
            }
            this.buckets = buckets_new;
            size = size*2;
        }
        num++;
        int id = abs(key.hashCode()%size);
        Collection<Node> nodes = buckets[id];
        nodes.add(createNode(key,value));
    }

    @Override
    public Set<K> keySet() {
        Set<K> s = new HashSet<>();
        for(int i=0;i<size;++i)
        {
            Collection<Node> nodes = buckets[i];
            if(nodes.size() == 0) continue;
            for(Node n:nodes)
            {
                s.add(n.key);
            }
        }
        return s;
    }

    @Override
    public V remove(K key) {
        if(!containsKey(key)) return null;
        int id = abs(key.hashCode()%size);
        Collection<Node> nodes = buckets[id];
        V ans = null;
        for(Node n:nodes)
        {
            if(n.key.equals(key))
            {
                ans = n.value;
                nodes.remove(n);
                return ans;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if(!containsKey(key)) return null;
        int id = abs(key.hashCode()%size);
        Collection<Node> nodes = buckets[id];
        for(Node n:nodes)
        {
            if(n.key.equals(key) && n.value.equals(value))
            {
                nodes.remove(n);
                return value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
