package me.ildarorama.module3.task6;

import java.util.LinkedList;

public class ClassicQueue {
    private final LinkedList<Integer> list = new LinkedList<>();
    private volatile boolean stopped = false;

    public Integer get() throws InterruptedException {
        synchronized (list) {
            if (list.isEmpty()) {
                if (stopped) {
                    list.notify();
                    return null;
                } else {
                    list.wait();
                }
            }
            return list.removeLast();
        }
    }

    public boolean put(Integer value) {
        synchronized (list) {
            if (stopped) {
                return false;
            }
            list.addFirst(value);
            list.notify();
        }
        return true;
    }

    public void gracefullyShutdown() throws InterruptedException {
        synchronized (list) {
            stopped = true;
            if (!list.isEmpty()) {
                list.wait();
            }
        }
    }
}
