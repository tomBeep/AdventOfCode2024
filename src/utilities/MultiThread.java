package utilities;

import java.util.ArrayList;
import java.util.List;

public class MultiThread {
    static List<Thread> runningThreads = new ArrayList<Thread>();

    /**
     * Call me repeatedly, perhaps from within a loop, call #waitForMultiThread() to wait for all running threads.
     */
    public static void multiThread(Runnable r){
        Thread t = new Thread(r);
        t.start();
        runningThreads.add(t);
    }

    public static void waitForCompletion() {
        for (Thread runningThread : runningThreads) {
            try {
                runningThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
