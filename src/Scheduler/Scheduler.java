package Scheduler;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import java.util.Random;
import java.util.concurrent.Callable;

public class Scheduler {

    private Worker[] aos;
    private int cur;

    //invoke in scheduler
    public Worker[] getAos(){
        return aos;
    }

    public Scheduler(int n) {
        aos = new Worker[n];
        for (int i = 0; i < n; ++i) {
            aos[i] = new Worker(i, this);
        }
        for(Worker w : aos){
            w.startAO();
        }

        System.out.println("Workers Set!");

    }

    public void shutDown() {
        for (Worker ao : aos)
            ao.stop();

        try {
            for (Worker ao : aos)
                ao.join();
        } catch (InterruptedException ignore) {

        }
    }

    public void launch(FJAction fja) throws InterruptedException {
        FJInitialAction fji = new FJInitialAction(fja, this);
        System.out.println("Launch Time BABY!!!");
        aos[0].pushFJAction(fji);
        for (Worker w : aos) {
            synchronized (w) {
//                System.out.println("W"+"("+(w.returnIndex() + 1) +")"+" Has Been Notified!");

                w.notify();
            }

        }
        fji.join();
        System.out.println("Action Joined!");
    }

    public FJAction stealRandom(Worker w) {
        Random rand = new Random();
        while (true) {

            int randomPic = rand.nextInt(aos.length);
            Worker randWorker = aos[randomPic];
            FJAction fja;

            if(randWorker != w){
//                System.out.println("Thread ("+w.returnIndex()+") just committed Grand Larceny!");
                fja = aos[randomPic].getTasks().pollFirst();
            }
            else {
                continue;
            }

            return fja;


        }
    }
}
