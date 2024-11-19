package me.ildarorama.module3.task3;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageBusQueue<T> {
    private final int size;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition readable = lock.newCondition();
    private final Condition writable = lock.newCondition();
    private final Queue<T> queue;

    MessageBusQueue(int size) {
        this.queue = new ArrayDeque<>(size);
        this.size = size;
    }

    public void put(T i) throws InterruptedException {
        Objects.requireNonNull(i);
        try {
            lock.lock();
            while (size() == size) {
                writable.await();
            }
            queue.add(i);
            readable.signal();
        } finally {
            lock.unlock();
        }
    }

    public T poll() throws InterruptedException {
        try {
            lock.lock();
            while (size() == 0) {
                readable.await();
            }
            T value = queue.poll();
            writable.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        try {
            lock.lock();
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

}
