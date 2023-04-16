package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.ThroughputReporter.Context;

public interface ThroughputListener extends AutoCloseable {

    /**
     * Called for a rate report.
     *
     * @param context  reporter context
     * @param messages number of messages
     */
    void onReport(Context context, double messages);

}
