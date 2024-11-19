package me.ildarorama.module3.task4;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingObjectPool {
    private final int maxSize;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition readable = lock.newCondition();
    private final Condition writable = lock.newCondition();
    private final LinkedList<Object> list;

    /**
     * Creates filled pool of passed size
     *
     * @param size of pool
     */
    public BlockingObjectPool(int size) {
        this.list = new LinkedList<>();
        this.maxSize = size;
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public Object get() throws InterruptedException {
        try {
            lock.lock();
            while(list.isEmpty()) {
                readable.await();
            }
            Object value = list.removeLast();
            writable.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public void take(Object object) throws InterruptedException {
        try {
            lock.lock();
            while(list.size() == maxSize) {
                writable.await();
            }
            list.addLast(object);
            readable.signal();
        } finally {
            lock.unlock();
        }
    }

}
