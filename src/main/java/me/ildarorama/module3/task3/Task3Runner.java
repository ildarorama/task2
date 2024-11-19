package me.ildarorama.module3.task3;

import java.util.Random;
import java.util.logging.Logger;

public class Task3Runner {
    private static final Logger log = Logger.getLogger(Task3Runner.class.getName());
    private static final MessageBus BUS = new MessageBus();

    public static void main(String[] args) {
        new Thread(() -> Task3Runner.consumer("topic1"), "Consumer1").start();
        new Thread(() -> Task3Runner.consumer("topic1"), "Consumer2").start();

        new Thread(() -> Task3Runner.producer("topic1"), "Producer1").start();
        new Thread(() -> Task3Runner.producer("topic1"), "Producer2").start();
        new Thread(() -> Task3Runner.producer("topic1"), "Producer3").start();
    }

    public static void consumer(String topic) {
        MessageBusQueue<Integer> connector = BUS.connect(topic);
        while(!Thread.interrupted()) {
            try {
                Integer value = connector.poll();
                if (value != null) {
                    log.info(Thread.currentThread().getName() + ": consumes " + value);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void producer(String topic) {
        MessageBusQueue<Integer> connector = BUS.connect(topic);
        Random random = new Random();
        while(!Thread.interrupted()) {
            try {
                int value = random.nextInt(1000);
                connector.put(value);
                log.info(Thread.currentThread().getName() + ": produces " + value);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}