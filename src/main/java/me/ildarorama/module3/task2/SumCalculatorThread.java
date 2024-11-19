package me.ildarorama.module3.task2;

public class SumCalculatorThread extends Thread {
    private final DataPool pool;

    public SumCalculatorThread(DataPool pool) {
        super("Sum calculator");
        this.pool = pool;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println(pool.getSum());
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
