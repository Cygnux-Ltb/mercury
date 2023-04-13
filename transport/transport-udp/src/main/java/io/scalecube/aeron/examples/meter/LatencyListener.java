package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.LatencyReporter.Context;
import org.HdrHistogram.Histogram;

public interface LatencyListener extends AutoCloseable {

    /**
     * Called for a latency report.
     *
     * @param context           reporter context
     * @param intervalHistogram the histogram
     */
    void onReport(Context context, Histogram intervalHistogram);

    /**
     * Called for an accumulated result.
     *
     * @param context              reporter context
     * @param accumulatedHistogram the histogram
     */
    void onTerminate(Context context, Histogram accumulatedHistogram);

}
