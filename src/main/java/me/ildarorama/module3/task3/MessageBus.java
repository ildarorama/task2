package me.ildarorama.module3.task3;

import java.util.HashMap;
import java.util.Map;

public class MessageBus {
    private static final int MAX_SIZE = 16;
    private final Map<String, MessageBusQueue> queueMap = new HashMap<>();

    public <T> MessageBusQueue<T> connect(String topic) {
        synchronized (queueMap) {
            return queueMap.computeIfAbsent(topic, k -> new MessageBusQueue<T>(MAX_SIZE));
        }
    }
}
