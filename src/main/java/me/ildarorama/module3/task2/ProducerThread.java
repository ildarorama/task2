package me.ildarorama.module3.task2;

import java.util.Random;

public class ProducerThread extends Thread {
    private final DataPool pool;
    private final Random random = new Random();
    public ProducerThread(DataPool pool) {
        super("Producer thread");
        this.pool = pool;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                pool.addData(random.nextInt(10));
            } catch (Exception e) {
                System.out.println("Add thread is interrupted");
            }
        }
    }
}
