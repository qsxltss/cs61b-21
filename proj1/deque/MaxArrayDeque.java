package deque;

import java.util.Comparator;

public class MaxArrayDeque <T> extends ArrayDeque<T> {
    private Comparator<T> comp;

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
        T max1 = get(0);
        for(int i = 1; i<size(); ++i)
        {
            T now = get(i);
            if(c.compare(now,max1)>=0)
            {
                max1 = now;
            }
        }
        return max1;
    }
}
