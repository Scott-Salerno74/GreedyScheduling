import Scheduler.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Random;

public class FJTest {
    private final static int SIZE = 1_000_000;
    private final static int THRESHOLD = SIZE / 32;
    private long result;

    static class DotProd extends FJAction {

        int low;
        int hi;
        double[] x;
        double[] y;
        static long dotResult;


        public DotProd(int low, int hi, double[] x, double[] y) {
            this.low = low;
            this.hi = hi;
            this.x = x;
            this.y = y;
        }


        public void compute() {
            if ((hi - low) < THRESHOLD) {
                for (int i = low; i < hi; i++) {
                    dotResult = (long) (x[i]* y[i]);
                }
            }


            int mid = (x.length) / 2;
            DotProd left = new DotProd(low, mid, x, y);
            left.fork();
            DotProd right = new DotProd(mid, hi, x, y);
            right.compute();
            try {
                left.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] args) throws InterruptedException {
            Scheduler s = new Scheduler(4);
            double[] x = new double[SIZE];
            double[] y = new double[SIZE];
            for (int i = 0; i < x.length; i++) {
                x[i] = (Math.random());
                y[i] = (Math.random());
            }

            s.launch(new DotProd(0, x.length, x, y));
            System.out.println(" The Result Is: " + dotResult);
        }
    }


}



