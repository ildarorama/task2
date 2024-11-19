package me.ildarorama.module3.task2;

import java.util.ArrayList;
import java.util.List;

public class DataPool {
    private final List<Integer> pool = new ArrayList<>();
    private final Object lockAdd = new Object();
    public void addData(int data) {
        synchronized (lockAdd) {
                pool.add(data);
                lockAdd.notifyAll();
        }
    }


    public long getSum() throws InterruptedException {
        synchronized (lockAdd) {
            lockAdd.wait();
            return pool.stream().mapToInt(Integer::intValue).sum();
        }
    }

    public double getSquare() throws InterruptedException {
        synchronized (lockAdd) {
            lockAdd.wait();
            return Math.sqrt(
                    pool.stream().mapToInt(Integer::intValue).map(i -> i * i).sum()
            );
        }
    }

}
