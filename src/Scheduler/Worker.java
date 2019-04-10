package Scheduler;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static sun.java2d.opengl.OGLRenderQueue.sync;

/**
 * Book Keeping:
 * How many tasks each worker executes
 * Time spent doing Task
 * Time spent Not Doing Tasks
 * Calculate OverHead
 * How many Steals Committed
 */
public class Worker extends Thread {

    private AtomicBoolean running;
    private ConcurrentLinkedDeque<FJAction> deck; // I know its spelled Deque, but Deck is how it sounds.[Deck of Cards]
    private Scheduler sched;
    private int index;
    private int numSteals;
    private int count;
    public int Threshold = 20;

    public int getNumSteals(){
        return this.numSteals;
    }


    public int returnIndex(){
        return this.index;
    }


    public boolean idle() {
//        System.out.println(" Thread in IDLE State");
        boolean isIdle = true;
        if (count < Threshold) {
            return false;
        }
        synchronized (this) {
            while (isIdle) {
//                System.out.println("Went night Night");
                try {
//                    System.out.println("Waiting");
                    this.wait(); // Don't Wait forever, Wait for a while, wake up try a steal, if steal works try again
                } catch (InterruptedException e) {
                }
            }
            return false; // in the event that idle is true means we will keep waiting
        }
    }


    /**
     * create a thread that will run tasks we supply to it via the add() method.
     */
    public Worker(int index, Scheduler sched) {
        this.sched = sched;
        this.index = index;
        deck = new ConcurrentLinkedDeque<FJAction>();
        count = 0;
        running = new AtomicBoolean(false);
        running.set(true);

    }

    public void incrementCount(){
        this.count++;
    }


    public void pushFJAction(FJAction action) {
        // push the FJAction onto the deck
        deck.addFirst(action);

    }

    /**
     * start the object's thread running
     */
    public void startAO() {
        running.set(true);
        this.start();
    }

    /**
     * is the thread running right now?
     *
     * @return true if so
     */
    public boolean isRunning() {
        return running.get();
    }


    protected FJAction addAO(FJAction fja) {
        if (fja == null)
            return null;

        ConcurrentLinkedDeque<FJAction> qe = new ConcurrentLinkedDeque<FJAction>();
        qe = deck;
        System.out.println("Notifying ALL!!!!!!!!!!!!!!!!");
        qe.notifyAll();

        return qe.element();
    }


    public void run() {
        while(isRunning() && !idle()){
//            System.out.println("Running and Idle: Hell of a Time "  + "W (" + index+")");
            FJAction fja = deck.poll();

        if (fja == null) {
            numSteals++;
            fja = sched.stealRandom(this);
        }
        if(fja != null){
            resetCount();
            try {
                fja.compute();
            } catch (InterruptedException e) {
            }
            fja.setIsDone();

        }
        else {
            incrementCount();
        }

        }
    }

    public void resetCount(){
        this.count = 0;
    }


    public FJAction pop() {
        return deck.pop();
    }

    /**
     * Steals a task from the top of the DEQUE and Runs it, while other Object is still running
     * #GREEDY
     *
     * @return null
     */
    protected FJAction StealFromMe() {
        return deck.pollFirst();
    }
    protected FJAction stealFromWorkers(){
        return sched.stealRandom(this);
    }
    public ConcurrentLinkedDeque<FJAction> getTasks() {
        return deck;
    }
}

