package me.ildarorama.module3.task4;

import java.util.Random;
import java.util.logging.Logger;

public class Task4Runner {
    private static final Logger log = Logger.getLogger(BlockingObjectPool.class.getName());

    public static void main(String[] args) {
        var pool = new BlockingObjectPool(10);
        new Thread(() -> Task4Runner.consumer(pool), "Consumer1").start();
        new Thread(() -> Task4Runner.consumer(pool), "Consumer2").start();

        new Thread(() -> Task4Runner.producer(pool), "Producer1").start();
        new Thread(() -> Task4Runner.producer(pool), "Producer2").start();
        new Thread(() -> Task4Runner.producer(pool), "Producer3").start();
    }

    public static void consumer(BlockingObjectPool pool) {
        while(!Thread.interrupted()) {
            try {
                Object value = pool.get();
                if (value != null) {
                    log.info(Thread.currentThread().getName() + ": gets " + value);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void producer(BlockingObjectPool pool) {
        Random random = new Random();
        while(!Thread.interrupted()) {
            try {
                int value = random.nextInt(1000);
                pool.take(value);
                log.info(Thread.currentThread().getName() + ": takes " + value);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
