package me.ildarorama.module3.task2;

public class Task2Runner {
    public static void main(String[] args) {
        DataPool pool = new DataPool();
        ProducerThread producerThread = new ProducerThread(pool);
        SumCalculatorThread sumThread = new SumCalculatorThread(pool);
        SquareCalculatorThread squareThread = new SquareCalculatorThread(pool);

        producerThread.start();
        sumThread.start();
        squareThread.start();
    }
}