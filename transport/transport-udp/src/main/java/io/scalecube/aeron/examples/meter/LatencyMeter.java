package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.LatencyReporter.Context;
import org.HdrHistogram.Histogram;
import org.agrona.CloseHelper;

public class LatencyMeter implements AutoCloseable {

  private final String name;
  private final LatencyListener listener;
  private final org.HdrHistogram.Recorder histogram;
  private final Context context;

  private Histogram accumulatedHistogram;

  LatencyMeter(String name, LatencyReporter.Context context, LatencyListener listener) {
    this.name = name;
    this.listener = listener;
    this.context = context;
    this.histogram =
        new org.HdrHistogram.Recorder(
            context.lowestTrackableValue(),
            context.highestTrackableValue(),
            context.numberOfSignificantValueDigits());
  }

  public String name() {
    return name;
  }

  public void record(long value) {
    histogram.recordValue(value);
  }

  void run() {
    Histogram intervalHistogram = histogram.getIntervalHistogram();
    if (accumulatedHistogram != null) {
      accumulatedHistogram.add(intervalHistogram);
    } else {
      accumulatedHistogram = intervalHistogram;
    }
    listener.onReport(context, intervalHistogram);
  }

  @Override
  public void close() {
    if (accumulatedHistogram != null) {
      listener.onTerminate(context, accumulatedHistogram);
    }
    histogram.reset();
    CloseHelper.quietClose(listener);
  }
}
