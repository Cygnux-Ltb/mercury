package io.scalecube.aeron.examples.meter;

import org.agrona.CloseHelper;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.NanoClock;
import org.agrona.concurrent.SystemNanoClock;

import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

public class ThroughputReporter implements Agent, AutoCloseable {

    private static final int NULL_VALUE = -1;

    private final Context context;

    private final Map<String, ThroughputMeter> meters = new ConcurrentHashMap<>();

    private long reportDeadline = NULL_VALUE;

    public ThroughputReporter() {
        this(new Context());
    }

    public ThroughputReporter(Context context) {
        this.context = context;
    }

    public Context context() {
        return context;
    }

    public ThroughputMeter meter(String name, ThroughputListener... listeners) {
        return meters.computeIfAbsent(name,
                s -> new ThroughputMeter(name, context, new CompositeThroughputListener(listeners)));
    }

    public void remove(ThroughputMeter meter) {
        if (meter != null) {
            final ThroughputMeter r = meters.remove(meter.name());
            CloseHelper.quietClose(r);
        }
    }

    @Override
    public void close() {
        meters.values().forEach(ThroughputMeter::close);
    }

    @Override
    public int doWork() {
        final long nanoTime = context.nanoClock.nanoTime();
        if (reportDeadline == NULL_VALUE) {
            reportDeadline = nanoTime + context.reportInterval.toNanos();
        }

        if (nanoTime >= reportDeadline) {
            meters.values().forEach(ThroughputMeter::run);
            reportDeadline = NULL_VALUE;
            return 0;
        }

        return 1;
    }

    @Override
    public String roleName() {
        return "throughput-reporter";
    }

    public static class Context implements Cloneable {

        private final Duration reportInterval = Duration.ofSeconds(1);

        private NanoClock nanoClock = new SystemNanoClock();

        public Context() {
        }

        public Duration reportInterval() {
            return reportInterval;
        }

        public NanoClock nanoClock() {
            return nanoClock;
        }

        public Context nanoClock(NanoClock nanoClock) {
            final Context c = clone();
            c.nanoClock = nanoClock;
            return c;
        }

        @Override
        public Context clone() {
            try {
                return (Context) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Context.class.getSimpleName() + "[", "]")
                    .add("reportInterval=" + reportInterval)
                    .add("nanoClock=" + nanoClock)
                    .toString();
        }
    }
}
