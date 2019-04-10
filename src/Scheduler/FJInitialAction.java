package Scheduler;

import Scheduler.FJAction;
import Scheduler.Scheduler;

public class FJInitialAction extends FJAction {
    private FJAction action;
    private Scheduler s;

    public FJInitialAction(FJAction action, Scheduler s) {
        this.action = action;
        this.s = s;
    }


    public void compute() throws InterruptedException {
        action.compute();

        synchronized (s) {
            isDone = true;
            s.notifyAll();
//        s.notifyAll();
        }
    }


    public void join() throws InterruptedException {
        synchronized (s) {
            while (!isDone) {
                try {
                    s.wait();
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
}
