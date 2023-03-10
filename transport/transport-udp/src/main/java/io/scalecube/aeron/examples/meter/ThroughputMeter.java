package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.ThroughputReporter.Context;
import org.agrona.CloseHelper;

import java.util.concurrent.atomic.LongAdder;

public class ThroughputMeter implements AutoCloseable {

    private final String name;
    private final Context context;
    private final ThroughputListener listener;

    private final LongAdder totalMessages = new LongAdder();

    private long lastTotalMessages;
    private long lastTimestamp;

    ThroughputMeter(String name, Context context, ThroughputListener listener) {
        this.name = name;
        this.context = context;
        this.listener = listener;
    }

    public String name() {
        return name;
    }

    public void record() {
        record(1);
    }

    public void record(long messages) {
        totalMessages.add(messages);
    }

    void run() {
        long currentTotalMessages = totalMessages.longValue();
        long currentTimestamp = context.nanoClock().nanoTime();

        long timeSpanNs = currentTimestamp - lastTimestamp;
        double messagesPerInterval =
                ((currentTotalMessages - lastTotalMessages) * (double) context.reportInterval().toNanos())
                        / (double) timeSpanNs;

        lastTotalMessages = currentTotalMessages;
        lastTimestamp = currentTimestamp;

        listener.onReport(context, messagesPerInterval);
    }

    @Override
    public void close() {
        CloseHelper.quietClose(listener);
    }
}
