package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.LatencyReporter.Context;
import org.HdrHistogram.Histogram;
import org.agrona.CloseHelper;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileReportingLatencyListener implements LatencyListener {

    private final String fileName;

    private PrintStream printStream;

    public FileReportingLatencyListener(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onReport(Context context, Histogram histogram) {
        ensurePrintStream();
        histogram.outputPercentileDistribution(printStream,
                context.percentileTicksPerHalfDistance(), context.scalingRatio(), false);
    }

    @Override
    public void onTerminate(Context context, Histogram histogram) {
        ensurePrintStream();
        histogram.outputPercentileDistribution(printStream,
                context.percentileTicksPerHalfDistance(), context.scalingRatio(), false);
    }

    @Override
    public void close() {
        CloseHelper.quietClose(printStream);
    }

    private void ensurePrintStream() {
        if (printStream == null) {
            try {
                printStream = new PrintStream(
                        String.join(".", fileName, currentDateTime(), "log"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String currentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss.SSS"));
    }
}
