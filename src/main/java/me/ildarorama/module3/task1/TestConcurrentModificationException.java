package me.ildarorama.module3.task1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestConcurrentModificationException {
    //private static final Map<Integer, Integer> map = new HashMap<>();
    private static final Map<Integer, Integer> map = Collections.synchronizedMap(new HashMap<>());
    //private static final Map<Integer, Integer> map = new ThreadSafeMap<>();
    //private static final Map<Integer, Integer> map = new ConcurrentHashMap<>();
    private static long startSumThreadTime = 0;
    private static long startAddThreadTime = 0;
    private static long sumThreadCount = 0;
    private static long addThreadCount = 0;

    public static void main(String[] args) throws InterruptedException {
        var t1 = new Thread(() -> {
            long count =  1_000_0;
            startSumThreadTime = System.nanoTime();
            while (count > 0) {
                map.values().stream().mapToInt(Integer::intValue).sum();
                sumThreadCount++;
                count--;
            }
            startSumThreadTime = System.nanoTime() - startSumThreadTime;
        }, "Sum");
        t1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        t1.start();

        var t2 = new Thread(() -> {
            int i = 0;
            long count = 1_000_0;
            startAddThreadTime = System.nanoTime();
            while (count > 0) {
                i++;
                map.put(i, i);
                addThreadCount++;
                count--;
            }
            startAddThreadTime = System.nanoTime() - startAddThreadTime;
        }, "Add");
        t2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        t2.start();

        t1.join();
        t2.join();
        showInfo();
    }

    private static final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
        System.err.println("Thread " + t.getName() + " died");
        System.err.println(e.toString());
        System.exit(0);
    };

    private static void showInfo() {
        System.err.println("Map size " + map.size());
        System.err.println("Sum thread work time: " + startSumThreadTime + " count: " + sumThreadCount);
        System.err.println("Add thread work time: " + startAddThreadTime + " count: " + addThreadCount);
        System.err.println("Add performance: " + (startAddThreadTime/addThreadCount));
    }
}
