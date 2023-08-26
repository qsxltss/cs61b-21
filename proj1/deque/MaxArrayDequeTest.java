package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

class MyComparator implements Comparator<Integer>
{
    public int compare(Integer a, Integer b)
    {
        return a.compareTo(b);
    }
}

public class MaxArrayDequeTest {

    @Test
    public void SimpleMaxArrayTest() {

        MyComparator myComparator = new MyComparator();

        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<Integer>(myComparator);

        for(int i=0;i<15;i++)
        {
            lld1.addLast(i);
        }

        System.out.println("Printing out deque: ");
        lld1.printDeque();

        int d = lld1.max();
        assertEquals(d,14);
    }
}