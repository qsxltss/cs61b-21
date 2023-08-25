package deque;

import java.util.Deque;
import java.util.Iterator;

public class ArrayDeque <T>
{
    double prop = 0.25;
    double mul = 4;

    private T[] items;
    int size;
    int max_len;
    int first;
    int end;

    public ArrayDeque()
    {
        size = 0;
        items = (T[]) new Object[8];
        first = 0;
        end = 1;
        max_len = 8;
    }
    private void resize(int len)
    {
        T[] a = (T[]) new Object[len];
        int first_now = (first+1)%max_len;
        int end_now = (end-1+max_len)%max_len;
        if(first_now < end_now)
        {
            System.arraycopy(items,first_now,a,0,size);
        }
        else
        {
            System.arraycopy(items,first_now,a,0,max_len-first_now);
            System.arraycopy(items,0,a,max_len-first_now,end_now+1);
        }
        items = a;
        first = len-1;
        end = size;
        max_len = len;
    }
    public void addFirst(T item)
    {
        size ++;
        if(size >= max_len)
        {
            size--;
            resize((int)(max_len*mul));
            size++;
        }
        items[first] = item;
        int num = (max_len+first-1)%max_len;
        first = num;
    }
    public void addLast(T item)
    {
        size +=1;
        if(size >= max_len)
        {
            resize((int)(max_len*mul));
        }
        items[end] = item;
        int x = end+1;
        int num = x%max_len;
        end = num;
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
        int first_now = (first+1)%max_len;
        int end_now = (end-1+max_len)%max_len;
        if(first_now <= end_now)
        {
            for(int i=first_now; i<end_now;++i)
            {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            System.out.println(items[end_now]);
        }
        else
        {
            for(int i=first_now; i<max_len;++i)
            {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            for(int i=0; i<end_now;++i)
            {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            System.out.println(items[end_now]);
        }
    }
    public T removeFirst()
    {
        if(size <= 0) return null;
        int first_now = (first+1)%max_len;
        T ans = items[first_now];
        size --;
        if(size < max_len*prop && size!=0)
        {
            size++;
            resize(size*2);
            size--;
            first = 0;
        }
        else
        {
            first = (first+1)%max_len;
        }
        return ans;
    }
    public T removeLast()
    {
        if(size <= 0) return null;
        int end_now = (end-1+max_len)%max_len;
        T ans = items[end_now];
        size --;
        if(size < max_len*prop && size!=0)
        {
            size++;
            resize(size*2);
            size--;
            end = size;
        }
        else
        {
            end = end_now;
        }
        return ans;
    }

    public T get(int index)
    {
        if(index >= size || index < 0) return null;
        return items[(first+index+1)%max_len];
    }

    public boolean equals(Object o)
    {
        if(!(o instanceof Deque)) return false;
        if(((Deque<?>) o).size() != size) return false;
        int i = first;
        int j = ((ArrayDeque<?>) o).first;
        int num = 0;
        while(num<size)
        {
            i = (i+1)%max_len;
            j = (j+1)%max_len;
            if(((ArrayDeque<Object>) o).get(i).equals(this.get(j))) continue;
            else return false;
        }
        return true;
    }

    public Iterator<T> iterator()
    {
        return null;
    }
}
