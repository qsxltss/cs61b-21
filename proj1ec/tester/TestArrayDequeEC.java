package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.introcs.StdRandom;

public class TestArrayDequeEC {
    @Test
    public void Test()
    {
        StudentArrayDeque<Integer> lld1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> lld2 = new ArrayDequeSolution<>();
        String str = "";
        for(int i=0; i<500; ++i) {
            int num = StdRandom.uniform(0, 4);
            if (num == 0) {
                int x = StdRandom.uniform(0, 100);
                lld1.addFirst(x);
                lld2.addFirst(x);
                str += "addFirst("+x+")\n";
            }
            if (num == 1) {
                int x = StdRandom.uniform(0, 100);
                lld1.addLast(x);
                lld2.addLast(x);
                str += "addLast("+x+")\n";
            }
            if (num == 2) {
                if (lld1.size() == 0) continue;
                Integer d1 = lld1.removeFirst();
                Integer d2 = lld2.removeFirst();
                str += "removeFirst()\n";
                assertEquals(str,d1,d2);
            }
            if (num == 3) {
                if (lld1.size() == 0) continue;
                Integer d1 = lld1.removeLast();
                Integer d2 = lld2.removeLast();
                str += "removeLast()\n";
                assertEquals(str,d1,d2);
            }
        }
    }
}
