package io.scalecube.aeron.examples.perftest;

import io.aeron.Aeron;
import io.aeron.ChannelUri;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.CommonContext;
import io.aeron.ExclusivePublication;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchiveThreadingMode;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.codecs.SourceLocation;
import io.aeron.archive.status.RecordingPos;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.MediaDriver.Context;
import io.aeron.driver.status.SystemCounterDescriptor;
import io.scalecube.aeron.examples.AeronHelper;
import io.scalecube.aeron.examples.archive.AeronArchiveUtil;
import io.scalecube.aeron.examples.archive.RecordingDescriptor;
import org.agrona.CloseHelper;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.YieldingIdleStrategy;
import org.agrona.concurrent.status.AtomicCounter;
import org.agrona.concurrent.status.CountersReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static io.aeron.CommonContext.IPC_MEDIA;
import static io.aeron.CommonContext.MDC_CONTROL_MODE_DYNAMIC;
import static io.aeron.CommonContext.UDP_MEDIA;
import static io.scalecube.aeron.examples.AeronHelper.MEGABYTE;
import static io.scalecube.aeron.examples.AeronHelper.MESSAGE_LENGTH;
import static io.scalecube.aeron.examples.AeronHelper.NUMBER_OF_MESSAGES;
import static io.scalecube.aeron.examples.AeronHelper.RUNS;
import static io.scalecube.aeron.examples.AeronHelper.STREAM_ID;
import static io.scalecube.aeron.examples.AeronHelper.printArchiveContext;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;
import static org.agrona.BufferUtil.allocateDirectAligned;

public class BasicIpcRecordingThroughput implements AutoCloseable {

    private static final String RECORDING_CHANNEL = CommonContext.IPC_CHANNEL;

    public static final String RECORDING_EVENTS_CHANNEL_ENDPOINT = "localhost:8030";
    public static final String CONTROL_CHANNEL_ENDPOINT = "localhost:8010";
    public static final String REPLICATION_CHANNEL_ENDPOINT = "localhost:0";

    private final Aeron aeron;
    private final AeronArchive aeronArchive;
    private final MediaDriver mediaDriver;
    private final Archive archive;
    private final UnsafeBuffer buffer =
            new UnsafeBuffer(allocateDirectAligned(MESSAGE_LENGTH, CACHE_LINE_LENGTH));

    /**
     * Main method for launching the process.
     *
     * @param args passed to the process.
     */
    public static void main(final String[] args) {
        try (BasicIpcRecordingThroughput test = new BasicIpcRecordingThroughput()) {
            for (int i = 0; i < RUNS; i++) {
                test.streamMessagesForRecording();
            }
        }
    }

    BasicIpcRecordingThroughput() {
        Path aeronPath = Paths.get(CommonContext.generateRandomDirName());
        String instanceName = aeronPath.getFileName().toString();
        Path archivePath = AeronHelper.archivePath()
                .orElseGet(() -> Paths.get(String.join("-", instanceName, "archive")));

        mediaDriver = MediaDriver.launch(
                new Context().aeronDirectoryName(aeronPath.toString()).spiesSimulateConnection(true));

        aeron = Aeron.connect(
                new Aeron.Context()
                        .aeronDirectoryName(aeronPath.toString())
                        .availableImageHandler(AeronHelper::printAvailableImage)
                        .unavailableImageHandler(AeronHelper::printUnavailableImage));

        archive = Archive.launch(
                new Archive.Context()
                        .aeron(aeron)
                        .mediaDriverAgentInvoker(mediaDriver.sharedAgentInvoker())
                        .errorCounter(
                                new AtomicCounter(
                                        mediaDriver.context().countersValuesBuffer(),
                                        SystemCounterDescriptor.ERRORS.id()))
                        .errorHandler(mediaDriver.context().errorHandler())
                        .localControlChannel(localControlChannel(instanceName))
                        .controlChannel(controlChannel())
                        .recordingEventsEnabled(false)
                        .recordingEventsChannel(recordingEventsChannel())
                        .replicationChannel(replicationChannel())
                        .aeronDirectoryName(aeronPath.toString())
                        .archiveDirectoryName(archivePath.toString())
                        .threadingMode(ArchiveThreadingMode.SHARED));

        printArchiveContext(archive.context());

        aeronArchive = AeronArchive.connect(new AeronArchive.Context().aeron(aeron));
    }

    @Override
    public void close() {
        CloseHelper.close(aeronArchive);
        CloseHelper.close(aeron);
        CloseHelper.close(archive);
        CloseHelper.close(mediaDriver);
    }

    private void streamMessagesForRecording() {
        try (ExclusivePublication publication = aeron
                .addExclusivePublication(RECORDING_CHANNEL, STREAM_ID)) {

            aeronArchive.startRecording(
                    ChannelUri.addSessionId(RECORDING_CHANNEL, publication.sessionId()),
                    STREAM_ID,
                    SourceLocation.LOCAL,
                    true);

            final IdleStrategy idleStrategy = YieldingIdleStrategy.INSTANCE;
            while (!publication.isConnected()) {
                idleStrategy.idle();
            }

            final long startNs = System.nanoTime();
            final UnsafeBuffer buffer = this.buffer;

            for (long i = 0; i < NUMBER_OF_MESSAGES; i++) {
                buffer.putLong(0, i);

                idleStrategy.reset();
                while (publication.offer(buffer, 0, MESSAGE_LENGTH) < 0) {
                    idleStrategy.idle();
                }
            }

            final long stopPosition = publication.position();
            final CountersReader counters = aeron.countersReader();
            final int counterId = RecordingPos.findCounterIdBySession(counters, publication.sessionId());

            idleStrategy.reset();
            while (counters.getCounterValue(counterId) < stopPosition) {
                idleStrategy.idle();
            }

            final long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            final double dataRate = (stopPosition * 1000.0d / durationMs) / MEGABYTE;
            final double recordingMb = stopPosition / MEGABYTE;
            final long msgRate = (NUMBER_OF_MESSAGES / durationMs) * 1000L;

            System.err.printf(
                    "Recorded %.02f MB @ %.02f MB/s - %,d msg/sec - %d byte payload + 32 byte header%n",
                    recordingMb, dataRate, msgRate, MESSAGE_LENGTH);

            RecordingDescriptor rd = AeronArchiveUtil
                    .findLastRecording(aeronArchive, rd1 -> rd1.streamId() == STREAM_ID);

            aeronArchive.stopRecording(publication);
            aeronArchive.truncateRecording(rd.recordingId(), 0);
        }
    }

    static String localControlChannel(String instanceName) {
        return new ChannelUriStringBuilder().media(IPC_MEDIA).endpoint(instanceName).build();
    }

    private static String replicationChannel() {
        return new ChannelUriStringBuilder()
                .media(UDP_MEDIA)
                .endpoint(REPLICATION_CHANNEL_ENDPOINT)
                .build();
    }

    private static String recordingEventsChannel() {
        return new ChannelUriStringBuilder()
                .media(UDP_MEDIA)
                .controlMode(MDC_CONTROL_MODE_DYNAMIC)
                .controlEndpoint(RECORDING_EVENTS_CHANNEL_ENDPOINT)
                .build();
    }

    private static String controlChannel() {
        return new ChannelUriStringBuilder()
                .media(UDP_MEDIA)
                .endpoint(CONTROL_CHANNEL_ENDPOINT)
                .build();
    }
}
