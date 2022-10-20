package io.mercury.common.concurrent.disruptor.example;

import com.lmax.disruptor.EventHandler;

import java.nio.ByteBuffer;

public class LongEvent {

    private long value;

    public LongEvent set(long value) {
        this.value = value;
        return this;
    }

    public long get() {
        return value;
    }

    public static final EventHandler<LongEvent> EventHandler =
            (event, sequence, endOfBatch) -> System.out.println("event value -> " + event.get());

    public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(event);
    }

    public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }

}