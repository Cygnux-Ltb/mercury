package io.scalecube.aeron.examples.perftest;

import io.aeron.Aeron;
import io.aeron.ChannelUri;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.CommonContext;
import io.aeron.Image;
import io.aeron.ImageFragmentAssembler;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchiveThreadingMode;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.client.RecordingDescriptorConsumer;
import io.aeron.archive.codecs.SourceLocation;
import io.aeron.archive.status.RecordingPos;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.MediaDriver.Context;
import io.aeron.driver.status.SystemCounterDescriptor;
import io.aeron.logbuffer.Header;
import io.scalecube.aeron.examples.AeronHelper;
import org.agrona.CloseHelper;
import org.agrona.DirectBuffer;
import org.agrona.collections.MutableLong;
import org.agrona.concurrent.BusySpinIdleStrategy;
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
import static io.scalecube.aeron.examples.AeronHelper.FRAGMENT_LIMIT;
import static io.scalecube.aeron.examples.AeronHelper.NOOP_FRAGMENT_HANDLER;
import static io.scalecube.aeron.examples.AeronHelper.NUMBER_OF_MESSAGES;
import static io.scalecube.aeron.examples.AeronHelper.STREAM_ID;
import static io.scalecube.aeron.examples.AeronHelper.printArchiveContext;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;
import static org.agrona.BufferUtil.allocateDirectAligned;

public class BasicUdpReplayThroughput implements AutoCloseable {

    private static final String RECORDING_CHANNEL =
            new ChannelUriStringBuilder().media(UDP_MEDIA).endpoint("localhost:20121").build();

    private static final int REPLAY_STREAM_ID = 101;
    private static final String REPLAY_CHANNEL =
            new ChannelUriStringBuilder().media(UDP_MEDIA).endpoint("localhost:30211").build();

    public static final String RECORDING_EVENTS_CHANNEL_ENDPOINT = "localhost:8030";
    public static final String CONTROL_CHANNEL_ENDPOINT = "localhost:8010";
    public static final String REPLICATION_CHANNEL_ENDPOINT = "localhost:0";

    private long messageCount;

    private final Aeron aeron;
    private final AeronArchive aeronArchive;
    private final MediaDriver mediaDriver;
    private final Archive archive;
    private int publicationSessionId;
    private final UnsafeBuffer buffer =
            new UnsafeBuffer(allocateDirectAligned(AeronHelper.MESSAGE_LENGTH, CACHE_LINE_LENGTH));

    /**
     * Main method for launching the process.
     *
     * @param args passed to the process.
     */
    public static void main(final String[] args) {
        System.setProperty("aeron.term.buffer.sparse.file", "false");
        System.setProperty("aeron.mtu.length", "8k");
        System.setProperty("aeron.ipc.mtu.length", "8k");
        System.setProperty("aeron.socket.so_sndbuf", "2m");
        System.setProperty("aeron.socket.so_rcvbuf", "2m");
        System.setProperty("aeron.rcv.initial.window.length", "2m");
        System.setProperty("agrona.disable.bounds.checks", "true");

        try (BasicUdpReplayThroughput test = new BasicUdpReplayThroughput()) {
            System.out.println("Making a recording for playback...");
            final long recordingLength = test.makeRecording();

            System.out.println("Finding the recording...");
            final long recordingId =
                    test.findRecordingId(
                            ChannelUri.addSessionId(RECORDING_CHANNEL, test.publicationSessionId));

            System.out.printf("Replaying %,d messages%n", NUMBER_OF_MESSAGES);
            for (int i = 0; i < AeronHelper.RUNS; i++) {
                test.replayRecording(recordingLength, recordingId);
            }
        }
    }

    BasicUdpReplayThroughput() {
        Path aeronPath = Paths.get(CommonContext.generateRandomDirName());
        String instanceName = aeronPath.getFileName().toString();
        Path archivePath =
                AeronHelper.archivePath()
                        .orElseGet(() -> Paths.get(String.join("-", instanceName, "archive")));

        mediaDriver =
                MediaDriver.launch(
                        new Context()
                                .ipcTermBufferLength(64 * 1024 * 1024)
                                .publicationTermBufferLength(64 * 1024 * 1024)
                                .aeronDirectoryName(aeronPath.toString())
                                .spiesSimulateConnection(true));

        aeron =
                Aeron.connect(
                        new Aeron.Context()
                                .aeronDirectoryName(aeronPath.toString())
                                .availableImageHandler(AeronHelper::printAvailableImage)
                                .unavailableImageHandler(AeronHelper::printUnavailableImage));

        archive =
                Archive.launch(
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

    private static boolean isImageNotUsable(Image image) {
        return image.isClosed() || image.isEndOfStream();
    }

    void onMessage(
            final DirectBuffer buffer, final int offset, final int length, final Header header) {
        final long count = buffer.getLong(offset);
        if (count != messageCount) {
            throw new IllegalStateException("invalid message count=" + count + " @ " + messageCount);
        }

        messageCount++;
    }

    private long makeRecording() {
        try (Publication publication = aeron.addExclusivePublication(RECORDING_CHANNEL, STREAM_ID)) {
            publicationSessionId = publication.sessionId();
            final String channel = ChannelUri.addSessionId(RECORDING_CHANNEL, publicationSessionId);
            final long subscriptionId =
                    aeronArchive.startRecording(channel, STREAM_ID, SourceLocation.LOCAL);
            final IdleStrategy idleStrategy = YieldingIdleStrategy.INSTANCE;

            try (Subscription subscription = aeron.addSubscription(channel, STREAM_ID)) {
                idleStrategy.reset();
                while (!subscription.isConnected()) {
                    idleStrategy.idle();
                }

                final Image image = subscription.imageBySessionId(publicationSessionId);

                long i = 0;
                while (i < NUMBER_OF_MESSAGES) {
                    int workCount = 0;
                    buffer.putLong(0, i);

                    if (publication.offer(buffer, 0, AeronHelper.MESSAGE_LENGTH) > 0) {
                        i++;
                        workCount += 1;
                    }

                    final int fragments = image.poll(NOOP_FRAGMENT_HANDLER, 10);
                    if (0 == fragments && isImageNotUsable(image)) {
                        throw new IllegalStateException("image closed unexpectedly");
                    }

                    workCount += fragments;
                    idleStrategy.idle(workCount);
                }

                final long position = publication.position();
                while (image.position() < position) {
                    final int fragments = image.poll(NOOP_FRAGMENT_HANDLER, 10);
                    if (0 == fragments && isImageNotUsable(image)) {
                        throw new IllegalStateException("image closed unexpectedly");
                    }
                    idleStrategy.idle(fragments);
                }

                awaitRecordingComplete(position, idleStrategy);

                return position;
            } finally {
                aeronArchive.stopRecording(subscriptionId);
            }
        }
    }

    private void awaitRecordingComplete(final long position, final IdleStrategy idleStrategy) {
        final CountersReader counters = aeron.countersReader();
        final int counterId = RecordingPos.findCounterIdBySession(counters, publicationSessionId);

        idleStrategy.reset();
        while (counters.getCounterValue(counterId) < position) {
            idleStrategy.idle();
        }
    }

    private void replayRecording(final long recordingLength, final long recordingId) {
        try (Subscription subscription =
                     aeronArchive.replay(recordingId, 0L, recordingLength, REPLAY_CHANNEL, REPLAY_STREAM_ID)) {

            final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
            while (!subscription.isConnected()) {
                idleStrategy.idle();
            }

            long startNs = System.nanoTime();
            messageCount = 0;
            final Image image = subscription.imageAtIndex(0);
            final ImageFragmentAssembler fragmentAssembler = new ImageFragmentAssembler(this::onMessage);

            while (messageCount < NUMBER_OF_MESSAGES) {
                final int fragments = image.poll(fragmentAssembler, FRAGMENT_LIMIT);
                if (0 == fragments && isImageNotUsable(image)) {
                    System.out.println("\n*** unexpected end of stream at message count: " + messageCount);
                    break;
                }

                idleStrategy.idle(fragments);
            }

            final long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            final double dataRate = (recordingLength * 1000.0d / durationMs) / AeronHelper.MEGABYTE;
            final double recordingMb = recordingLength / AeronHelper.MEGABYTE;
            final long msgRate = (NUMBER_OF_MESSAGES / durationMs) * 1000L;

            System.err.printf(
                    "Replayed %.02f MB @ %.02f MB/s - %,d msg/sec - %d byte payload + 32 byte header%n",
                    recordingMb, dataRate, msgRate, AeronHelper.MESSAGE_LENGTH);
        }
    }

    private long findRecordingId(final String expectedChannel) {
        final MutableLong foundRecordingId = new MutableLong();

        final RecordingDescriptorConsumer consumer =
                (controlSessionId,
                 correlationId,
                 recordingId,
                 startTimestamp,
                 stopTimestamp,
                 startPosition,
                 stopPosition,
                 initialTermId,
                 segmentFileLength,
                 termBufferLength,
                 mtuLength,
                 sessionId,
                 streamId,
                 strippedChannel,
                 originalChannel,
                 sourceIdentity) -> foundRecordingId.set(recordingId);

        final int recordingsFound =
                aeronArchive.listRecordingsForUri(0L, 10, expectedChannel, STREAM_ID, consumer);

        if (1 != recordingsFound) {
            throw new IllegalStateException("should have been only one recording");
        }

        return foundRecordingId.get();
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
