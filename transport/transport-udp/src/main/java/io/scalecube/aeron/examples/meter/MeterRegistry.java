package io.scalecube.aeron.examples.meter;

import org.agrona.CloseHelper;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.DynamicCompositeAgent;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.function.Function;

public class MeterRegistry implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeterRegistry.class);

    private final Context context;

    private LatencyReporter latencyReporter;
    private ThroughputReporter throughputReporter;
    private Function<String, LatencyListener> latencyListenerFactory;
    private Function<String, ThroughputListener> throughputListenerFactory;

    private AgentRunner agentRunner;

    private MeterRegistry() {
        this(new Context());
    }

    private MeterRegistry(Context context) {
        this.context = context;
    }

    public static MeterRegistry create() {
        return create(new Context());
    }

    public static MeterRegistry create(MeterRegistry.Context context) {
        final MeterRegistry meterRegistry = new MeterRegistry(context);
        meterRegistry.start();
        return meterRegistry;
    }

    private void start() {
        this.latencyReporter = new LatencyReporter(context.latencyContext);
        this.throughputReporter = new ThroughputReporter(context.throughputContext);

        this.latencyListenerFactory = FileReportingLatencyListener::new;
        this.throughputListenerFactory = FileReportingThroughputListener::new;

        final DynamicCompositeAgent compositeAgent =
                new DynamicCompositeAgent("meter-registry", latencyReporter, throughputReporter);

        agentRunner =
                new AgentRunner(
                        context.idleStrategy,
                        throwable -> LOGGER.error("[{}] Error:", compositeAgent.roleName(), throwable),
                        null,
                        compositeAgent);

        AgentRunner.startOnThread(agentRunner);
    }

    public LatencyMeter latency(String name) {
        return latencyReporter.meter(name, latencyListenerFactory.apply(name));
    }

    public ThroughputMeter tps(String name) {
        return throughputReporter.meter(name, throughputListenerFactory.apply(name));
    }

    public void remove(LatencyMeter meter) {
        latencyReporter.remove(meter);
    }

    public void remove(ThroughputMeter meter) {
        throughputReporter.remove(meter);
    }

    @Override
    public void close() {
        CloseHelper.quietCloseAll(agentRunner, latencyReporter, throughputReporter);
    }

    public static class Context implements Cloneable {

        private IdleStrategy idleStrategy = new SleepingMillisIdleStrategy(100);
        private LatencyReporter.Context latencyContext = new LatencyReporter.Context();
        private ThroughputReporter.Context throughputContext = new ThroughputReporter.Context();

        public Context() {
        }

        public IdleStrategy idleStrategy() {
            return idleStrategy;
        }

        public Context idleStrategy(IdleStrategy idleStrategy) {
            final Context c = clone();
            c.idleStrategy = idleStrategy;
            return c;
        }

        public LatencyReporter.Context latencyContext() {
            return latencyContext;
        }

        public Context latencyContext(LatencyReporter.Context latencyContext) {
            final Context c = clone();
            c.latencyContext = latencyContext;
            return c;
        }

        public ThroughputReporter.Context throughputContext() {
            return throughputContext;
        }

        public Context throughputContext(ThroughputReporter.Context throughputContext) {
            final Context c = clone();
            c.throughputContext = throughputContext;
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
                    .add("idleStrategy=" + idleStrategy)
                    .add("latencyContext=" + latencyContext)
                    .add("throughputContext=" + throughputContext)
                    .toString();
        }
    }
}
