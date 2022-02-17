package io.scalecube.aeron.examples.meter;

import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.agrona.CloseHelper;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.NanoClock;
import org.agrona.concurrent.SystemNanoClock;

public class LatencyReporter implements Agent, AutoCloseable {

  private static final int NULL_VALUE = -1;

  private final Context context;

  private long reportDeadline = NULL_VALUE;

  private final Map<String, LatencyMeter> meters = new ConcurrentHashMap<>();

  public LatencyReporter() {
    this(new Context());
  }

  public LatencyReporter(Context context) {
    this.context = context;
  }

  public Context context() {
    return context;
  }

  public LatencyMeter meter(String name, LatencyListener... listeners) {
    return meters.computeIfAbsent(
        name, s -> new LatencyMeter(name, context, new CompositeLatencyListener(listeners)));
  }

  public void remove(LatencyMeter meter) {
    if (meter != null) {
      final LatencyMeter r = meters.remove(meter.name());
      CloseHelper.quietClose(r);
    }
  }

  @Override
  public void close() {
    meters.values().forEach(LatencyMeter::close);
  }

  @Override
  public int doWork() {
    final long nanoTime = context.nanoClock.nanoTime();
    if (reportDeadline == NULL_VALUE) {
      reportDeadline = nanoTime + context.reportInterval.toNanos();
    }

    if (nanoTime >= reportDeadline) {
      meters.values().forEach(LatencyMeter::run);
      reportDeadline = NULL_VALUE;
      return 0;
    }

    return 1;
  }

  @Override
  public String roleName() {
    return "latency-reporter";
  }

  public static class Context implements Cloneable {

    private long lowestTrackableValue = 1;
    private long highestTrackableValue = TimeUnit.SECONDS.toNanos(3600);
    private double scalingRatio = 1000;
    private Duration reportInterval = Duration.ofSeconds(1);
    private int percentileTicksPerHalfDistance = 5;
    private int numberOfSignificantValueDigits = 3;
    private NanoClock nanoClock = new SystemNanoClock();

    public Context() {}

    public long lowestTrackableValue() {
      return lowestTrackableValue;
    }

    public Context lowestTrackableValue(long lowestTrackableValue) {
      final Context c = clone();
      c.lowestTrackableValue = lowestTrackableValue;
      return c;
    }

    public long highestTrackableValue() {
      return highestTrackableValue;
    }

    public Context highestTrackableValue(long highestTrackableValue) {
      final Context c = clone();
      c.highestTrackableValue = highestTrackableValue;
      return c;
    }

    public double scalingRatio() {
      return scalingRatio;
    }

    public Context scalingRatio(double scalingRatio) {
      final Context c = clone();
      c.scalingRatio = scalingRatio;
      return c;
    }

    public Duration reportInterval() {
      return reportInterval;
    }

    public Context reportInterval(Duration reportInterval) {
      final Context c = clone();
      c.reportInterval = reportInterval;
      return c;
    }

    public int percentileTicksPerHalfDistance() {
      return percentileTicksPerHalfDistance;
    }

    public Context percentileTicksPerHalfDistance(int percentileTicksPerHalfDistance) {
      final Context c = clone();
      c.percentileTicksPerHalfDistance = percentileTicksPerHalfDistance;
      return c;
    }

    public int numberOfSignificantValueDigits() {
      return numberOfSignificantValueDigits;
    }

    public Context numberOfSignificantValueDigits(int numberOfSignificantValueDigits) {
      final Context c = clone();
      c.numberOfSignificantValueDigits = numberOfSignificantValueDigits;
      return c;
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
          .add("lowestTrackableValue=" + lowestTrackableValue)
          .add("highestTrackableValue=" + highestTrackableValue)
          .add("scalingRatio=" + scalingRatio)
          .add("reportInterval=" + reportInterval)
          .add("percentileTicksPerHalfDistance=" + percentileTicksPerHalfDistance)
          .add("numberOfSignificantValueDigits=" + numberOfSignificantValueDigits)
          .add("nanoClock=" + nanoClock)
          .toString();
    }
  }
}
