package me.ildarorama.module3.task6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class NewQueue {
    private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(100);
    private volatile boolean stopped = false;
    private Thread waitForStopThread;

    public Integer get() throws InterruptedException {
        Integer value = queue.take();
        if (waitForStopThread != null && queue.isEmpty()) {
            LockSupport.unpark(waitForStopThread);
        }
        return value;
    }

    public boolean put(Integer value) throws InterruptedException {
        if (!stopped) {
            queue.put(value);
            return true;
        } else {
            return false;
        }
    }

    public void gracefullyShutdown() throws InterruptedException {
        stopped = true;
        waitForStopThread = Thread.currentThread();
        while (!queue.isEmpty()) {
            LockSupport.park();
        }
    }
}
