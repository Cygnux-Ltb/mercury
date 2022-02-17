package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.LatencyReporter.Context;
import org.HdrHistogram.Histogram;
import org.agrona.CloseHelper;

public class CompositeLatencyListener implements LatencyListener {

  private final LatencyListener[] listeners;

  public CompositeLatencyListener(LatencyListener... listeners) {
    this.listeners = listeners;
  }

  @Override
  public void onReport(Context context, Histogram intervalHistogram) {
    for (LatencyListener latencyListener : listeners) {
      latencyListener.onReport(context, intervalHistogram);
    }
  }

  @Override
  public void close() {
    CloseHelper.quietCloseAll(listeners);
  }

  @Override
  public void onTerminate(Context context, Histogram accumulatedHistogram) {
    for (LatencyListener latencyListener : listeners) {
      latencyListener.onTerminate(context, accumulatedHistogram);
    }
  }
}
