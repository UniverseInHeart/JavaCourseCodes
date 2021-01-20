package io.kimmking.kmq.core;

import java.util.HashMap;
import java.util.Map;

public final class Kmq {

    private static final Map<String, Integer> topicToIndex = new HashMap<>();
    private static Integer pIndex = 0;

    public Kmq(String topic, String consumer, int capacity) {
        this.topic = topic;
        this.consumer = consumer;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity];
    }

    private String topic;

    private String consumer;

    private int capacity;

    private KmqMessage[] queue;

    public void send(KmqMessage message) {
        queue[pIndex] = message;
        pIndex++;
    }

    public KmqMessage poll() {
        boolean containsKey = topicToIndex.containsKey(consumer);
        if (containsKey) {
            int index = topicToIndex.get(consumer);
            if (index > pIndex) {
                return null;
            }
            KmqMessage kmqMessage = queue[index];
            topicToIndex.put(consumer, index++);
            return kmqMessage;
        }
        if (pIndex == 0) {
            return null;
        }
        topicToIndex.put(consumer, 1);
        return queue[0];
    }

}
