package io.scalecube.aeron.examples.meter;

import io.scalecube.aeron.examples.meter.ThroughputReporter.Context;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileReportingThroughputListener implements ThroughputListener {

    private final String fileName;

    private PrintStream printStream;

    private long totalMessages;
    private long seconds;

    public FileReportingThroughputListener(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onReport(Context context, double messages) {
        ensurePrintStream();
        totalMessages += messages;
        seconds++;
        printStream.println(messages + " msgs/sec, " + totalMessages + " messages");
    }

    @Override
    public void close() {
        ensurePrintStream();
        printStream.println("Throughput average: ");
        printStream.println(
                (double) totalMessages / seconds + " msgs/sec, " + totalMessages + " messages");
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
