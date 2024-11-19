package me.ildarorama.module3.task2;

public class SquareCalculatorThread extends Thread {
    private final DataPool pool;

    public SquareCalculatorThread(DataPool pool) {
        super("Square calculator");
        this.pool = pool;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println(pool.getSquare());
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
