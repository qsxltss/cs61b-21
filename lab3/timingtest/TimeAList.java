package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            //double timePerOp = time / opCount;
            System.out.printf("%12d %12.4f %12d %12.4f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        Stopwatch sw = new Stopwatch();
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int i = 0;
        int tick = 0;
        while(i < 12800000)
        {
            i++;
            if(i == Math.pow(2,tick) * 1000)
            {
                tick++;
                Ns.addLast(i);
                opCounts.addLast(i);
                double time = sw.elapsedTime();
                times.addLast(time);
            }
        }
        printTimingTable(Ns,times,opCounts);
    }
}
