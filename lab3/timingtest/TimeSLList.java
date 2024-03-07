package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        int OPERATIONNUM = 10000;
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        for (int power = 0; power <= 7; power += 1) {
            Ns.addLast((1 << power) * 1000);
            opCounts.addLast(OPERATIONNUM);
        }

        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            /* Create a SLList with size of N */
            SLList<Integer> lst = new SLList<>();
            for (int j = 0; j < N; j += 1) {
                lst.addFirst(1);
            }
            /* Compute the time that getLast() costs */
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < OPERATIONNUM; k += 1) {
                lst.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
