package io.scalecube.aeron.examples.mdc.recording;

import io.aeron.Aeron;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.Publication;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchiveThreadingMode;
import io.aeron.archive.client.AeronArchive;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.MediaDriver.Context;
import io.aeron.driver.ThreadingMode;
import org.agrona.CloseHelper;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.SystemUtil;

import java.io.File;
import java.util.function.Supplier;

import static io.aeron.CommonContext.MDC_CONTROL_MODE_DYNAMIC;
import static io.aeron.CommonContext.UDP_MEDIA;
import static io.aeron.archive.codecs.SourceLocation.LOCAL;

public final class MdcRecordingPublisher {

    private static final String AERON_DIR = SystemUtil.tmpDirName() + "aeron-mdc-rec-pub";

    private static final String AERON_ARCHIVE_DIR = SystemUtil.tmpDirName() + "aeron-mdc-rec-archive";

    private static final Supplier<String> MESSAGE_PREFIX = () -> "Message|";

    public static final String CONTROL_ENDPOINT = "localhost:30888";
    @SuppressWarnings("unused")
    private static final String RECORDING_ENDPOINT = "localhost:30999";

    private static final long DEFAULT_PUBLISH_PERIOD_NS = 1_000_000_000L;

    public static final int STREAM_ID = 10000;

    private static final ChannelUriStringBuilder publicationChannel = new ChannelUriStringBuilder()
            .alias("mdc-publication")
            .spiesSimulateConnection(true)
            .media(UDP_MEDIA)
            .controlEndpoint(CONTROL_ENDPOINT)
            .controlMode(MDC_CONTROL_MODE_DYNAMIC);

    private static final ChannelUriStringBuilder recordingChannel = new ChannelUriStringBuilder()
            .alias("recording")
            .media(UDP_MEDIA)
            // .endpoint(RECORDING_ENDPOINT)
            .controlEndpoint(CONTROL_ENDPOINT);

    private static final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer();

    private static MediaDriver mediaDriver;
    private static Archive archive;
    private static AeronArchive aeronArchive;
    private static Aeron aeron;
    private static int messageId;
    private static int messagesPublished;

    public static void main(String[] args) throws InterruptedException {
        String publishPeriodNs = System.getenv("PUBLISH_PERIOD_NS");

        System.out.println("PUBLISH_PERIOD_NS = " + publishPeriodNs);

        setup(true);

        Publication publication = aeron
                .addExclusivePublication(publicationChannel.build(), STREAM_ID);

        aeronArchive.startRecording(recordingChannel
                .sessionId(publication.sessionId()).build(), STREAM_ID, LOCAL, true);

        publishMessages(publication, publishPeriodNs != null
                ? Long.parseLong(publishPeriodNs) : DEFAULT_PUBLISH_PERIOD_NS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown...");
            CloseHelper.quietCloseAll(aeron, aeronArchive, archive, mediaDriver);
        }));
    }

    private static void setup(boolean cleanStart) {
        Context mediaDriverContext = new Context()
                .aeronDirectoryName(AERON_DIR)
                .threadingMode(ThreadingMode.DEDICATED)
                .dirDeleteOnStart(cleanStart);

        mediaDriver = MediaDriver.launch(mediaDriverContext);

        File archiveDir = new File(AERON_ARCHIVE_DIR);

        archive = Archive.launch(new Archive.Context()
                .aeronDirectoryName(mediaDriverContext.aeronDirectoryName())
                .archiveDir(archiveDir)
                .threadingMode(ArchiveThreadingMode.DEDICATED)
                .deleteArchiveOnStart(cleanStart));

        aeron = Aeron.connect(new Aeron.Context()
                .aeronDirectoryName(mediaDriverContext.aeronDirectoryName()));

        aeronArchive = AeronArchive.connect(new AeronArchive.Context().aeron(aeron));
    }

    private static void publishMessages(Publication publication, long publishPeriodNs) {
        Thread publisherThread = new Thread(() -> {
            long deadline = 0;
            while (true) {
                long now = System.nanoTime();
                if (now >= deadline) {
                    String message = MESSAGE_PREFIX.get() + ++messageId;
                    publishMessage(publication, message);
                    System.out.println("messagesPublished = " + messagesPublished);
                    deadline = now + publishPeriodNs;
                }
            }
        });
        publisherThread.setDaemon(true);
        publisherThread.start();
    }

    private static void publishMessage(Publication publication, String message) {
        int length = buffer.putStringWithoutLengthAscii(0, message);
        long result;
        long lastResult = 0;
        while ((result = publication.offer(buffer, 0, length)) <= 0) {
            if (result != lastResult) {
                lastResult = result;
                System.out.println("result = " + result);
            }
            MdcRecordingPublisher.yield();
        }

        messagesPublished++;
    }

    private static void yield() {
        Thread.yield();
        if (Thread.currentThread().isInterrupted()) {
            fail("Unexpected interrupt");
        }
    }

    public static void fail(String reason) {
        throw new IllegalStateException(reason);
    }
}
