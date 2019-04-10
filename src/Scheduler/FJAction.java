package Scheduler;

public abstract class FJAction {
    protected volatile boolean isDone;

    public FJAction() {
        isDone = false;
    }

    public void setIsDone() {
        isDone = true;
    }

    public boolean getIsDone(){
        return isDone;
    }

    public void fork() {
        Worker aO = (Worker) Thread.currentThread();
        aO.pushFJAction(this);

    }

    /**
     * Wait until this action is completed. Allow the AO that calls join to continue executing (other) tasks while we wait
     */

    public void join() throws InterruptedException {
        Worker worker = (Worker) Thread.currentThread();
        FJAction action;
        while (!this.isDone) {
            if (worker.getTasks().isEmpty()) {
                action = worker.stealFromWorkers();


                if(action == null){
                    worker.incrementCount();
                }
                else {
                    worker.resetCount();
                }
            }
            else {
                action = worker.pop();
            }
            if(action != null){
                if(!action.getIsDone()){
                    action.compute();
                    action.setIsDone();
                    if(action == this){
                        return;
                    }
                }
            }

        }
    }





    /**
     * compute the Active Object
     * Should be Overridden and Created by User
     */
    public abstract void compute() throws InterruptedException;

//
//    /**
//     * Pass Parameters to the Queue / can't pass them to compute function because it is not a thing
//     * pass Them to a constructor instead
//     */
//
//    public void passParam() {
//
//
//    }
}
