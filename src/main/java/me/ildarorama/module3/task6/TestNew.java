package me.ildarorama.module3.task6;

import java.io.IOException;
import java.util.Random;

public class TestNew {
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final NewQueue queue = new NewQueue();

    public static void main(String[] args) throws InterruptedException, IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(TestNew::hook, "Hook"));
        Thread consumer = new Thread(TestNew::consumer, "Consumer");
        Thread producer = new Thread(TestNew::producer, "Producer");

        consumer.start();
        producer.start();
        Thread.sleep(1000);

        long pid = ProcessHandle.current().pid();
        var cmd = OS.contains("win") ? "taskkill /PID " : "kill -TERM ";
        Runtime.getRuntime().exec(cmd + pid);

        consumer.join();
        producer.join();
    }

    private static void hook() {
        try {
            System.out.println("Hook");
            queue.gracefullyShutdown();
        } catch (InterruptedException e) {
            System.out.println("Hook interrupted");
        }
    }

    public static void consumer() {
        while (!Thread.interrupted()) {
            try {
                Integer value = queue.get();
                if (value == null) {
                    System.out.println("Queue is closed. Exit");
                    return;
                }
                System.out.println(value);
                Thread.sleep(11);
            } catch (InterruptedException e) {
                System.out.println("Consumer terminated");
            }
        }
    }

    public static void producer() {
        var rnd = new Random(1000);
        while (!Thread.interrupted()) {
            try {
                if (!queue.put(rnd.nextInt())) {
                    System.out.println("Queue is stopped. Exit.");
                    return;
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
