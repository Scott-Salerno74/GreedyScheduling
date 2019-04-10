package Scheduler;

public class Future<T> {

//    private T value;
//    private Exception exception;
//    private boolean isSet; // atomic? volatile? synchronized? what here?
//    private ActiveObject ao;
//
//    Future(ActiveObject a) {
//        value = null;
//        exception = null;
//        isSet = false;
//        ao = a;
//    }
//
//    public T get() throws Exception {
//        // if isSet is true, neither value nor isSet will ever change again
//        if (isSet) {
//            if (exception != null)
//                throw exception;
//            else
//                return value;
//        }
//        // if !isSet, then we need to run our AO's queue until isSet becomes true
//        ao.runUntilSet(this);
//
//        if (exception != null)
//            throw exception;
//        else
//            return value;
//    }
//
//    public boolean isComplete() {
//        return isSet;
//    }
//
//    void complete(T v) throws IllegalStateException {
//        // can't complete more than once.
//        if (isSet)
//            throw new IllegalStateException();
//
//        synchronized (this) {
//            value = v;
//            isSet = true;
//            notifyAll();
//        }
//    }
//
//    void completeExceptionally(Exception e) throws IllegalStateException {
//        if (isSet)
//            throw new IllegalStateException();
//
//        synchronized (this) {
//            exception = e;
//            isSet = true;
//            notifyAll();
//        }
//    }
}