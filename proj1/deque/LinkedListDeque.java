package deque;

import java.util.Deque;
import java.util.Iterator;

public class LinkedListDeque <T>
{
    private node sentinel = new node();
    private int size;
    private node last_sentinel = new node();
    public class node<T>
    {
        public node prev;
        public T item;
        public node next;
        public node(T i, node prev1, node next1)
        {
            item = i;
            prev = prev1;
            next = next1;
        }
        public node()
        {
            item = null;
            prev = null;
            next = null;
        }
    }
    public LinkedListDeque()
    {
        size = 0;
        sentinel.next = last_sentinel;
        last_sentinel.prev = sentinel;
    }
    public LinkedListDeque(T m)
    {
        size = 1;
        node x = new node(m,sentinel,last_sentinel);
        sentinel.next = x;
        x.prev = sentinel;
        x.next = last_sentinel;
        last_sentinel.prev = x;
    }
    public void addFirst(T item)
    {
        node x = new node(item,sentinel,sentinel.next);
        sentinel.next.prev = x;
        sentinel.next = x;
        size += 1;
    }
    public void addLast(T item)
    {
        node x = new node(item,last_sentinel.prev,last_sentinel);
        last_sentinel.prev.next = x;
        last_sentinel.prev = x;
        size += 1;
    }
    public boolean isEmpty()
    {
        if(size == 0) return true;
        else return false;
    }
    public int size()
    {
        return size;
    }
    public void printDeque()
    {
        if(size == 0) return;
        int num = 1;
        node x = sentinel.next;
        while(num<size)
        {
            num += 1;
            System.out.print(x.item);
            System.out.print(" ");
        }
        System.out.println(x.item);
    }
    public T removeFirst()
    {
        if(size == 0) return null;
        size --;
        node ans = sentinel.next;
        node rest = sentinel.next.next;
        rest.prev = sentinel;
        sentinel.next = rest;
        return (T)(ans.item);
    }
    public T removeLast()
    {
        if(size == 0) return null;
        size --;
        node ans = last_sentinel.prev;
        node rest = last_sentinel.prev.prev;
        rest.next = last_sentinel;
        last_sentinel.prev = rest;
        return (T)(ans.item);
    }

    public T get(int index)
    {
        if(index >= size || index < 0) return null;
        int num = 0;
        node x = sentinel.next;
        while(num < index)
        {
            num++;
            x = x.next;
        }
        return (T)(x.item);
    }

    public T getRecursive_help(node x, int num)
    {
        if(num == 0) return (T)(x.item);
        else return (T)(getRecursive_help(x.next, num-1));

    }
    public T getRecursive(int index)
    {
        if(index >= size || index < 0) return null;
        return (T)(getRecursive_help(sentinel.next,index));
    }

    public boolean equals(Object o)
    {
        if(!(o instanceof Deque)) return false;
        if(((Deque<?>) o).size() != size) return false;
        for(int i = 0; i<size; ++i)
        {
            if(((LinkedListDeque<?>) o).get(i).equals(this.get(i))) continue;
            else return false;
        }
        return true;
    }

    public Iterator<T> iterator()
    {
        return null;
    }

}
