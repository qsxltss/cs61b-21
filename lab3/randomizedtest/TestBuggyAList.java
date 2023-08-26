package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void testThreeAddThreeRemove()
  {
    AListNoResizing<Integer> lld1 = new AListNoResizing<>();
    BuggyAList<Integer> lld2 = new BuggyAList<>();
    for(int i=1;i<=3;++i)
    {
      lld1.addLast(i+3);
      lld2.addLast(i+3);
    }
    for(int i=1;i<=3;++i)
    {
      int x1 = lld1.removeLast();
      int x2 = lld2.removeLast();
      assertEquals(x1,x2);
    }
  }

  @Test
  public void randomizedTest()
  {
    AListNoResizing<Integer> L = new AListNoResizing<>();
    BuggyAList<Integer> B = new BuggyAList<>();
    int N = 5000;
    for (int i = 0; i < N; i += 1) {
      int operationNumber = StdRandom.uniform(0, 4);
      if (operationNumber == 0)
      {
        // addLast
        int randVal = StdRandom.uniform(0, 100);
        L.addLast(randVal);
        B.addLast(randVal);
        System.out.println("addLast(" + randVal + ")");
      }
      else if (operationNumber == 1)
      {
        // size
        int size1 = L.size();
        int size2 = B.size();
        assertEquals(size1,size2);
        System.out.println("size: " + size1);
      }
      else if (operationNumber == 2)
      {
        int size = L.size();
        if(size == 0) continue;
        int n1 = L.getLast();
        int n2 = B.getLast();
        assertEquals(n1,n2);
        System.out.println("getLast()");
      }
      else
      {
        int size = L.size();
        if(size == 0) continue;
        int n1 = L.removeLast();
        int n2 = B.removeLast();
        assertEquals(n1,n2);
        System.out.println("removeLast()");
      }
    }
  }
}
