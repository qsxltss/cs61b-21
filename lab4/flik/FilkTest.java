package flik;
import org.junit.Test;
import static org.junit.Assert.*;
public class FilkTest {
    @Test
    public void IssameTest()
    {
        for(int i = 0; i<200; ++i)
        {
            boolean ans = Flik.isSameNumber(i,i);
            assertTrue(i+" is wrong!",ans);
        }
    }
}
