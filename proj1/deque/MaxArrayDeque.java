package deque;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;

public class MaxArrayDeque <T> extends ArrayDeque {
    Comparator<T> comp;
    public MaxArrayDeque(Comparator<T> c)
    {
        super();
        comp = c;
    }
    public T max()
    {
        return max(comp);
    }
    public T max(Comparator<T> c)
    {
        if(size() == 0) return null;
        int first_now = (first+1)%max_len;
        int end_now = (end-1+max_len)%max_len;
        T max1 = (T)(get(0));
        for(int i = 1; i<size(); ++i)
        {
            T now = (T)(get(i));
            if(c.compare(now,max1)>=0)
            {
                max1 = now;
            }
        }
        return max1;
    }
}
