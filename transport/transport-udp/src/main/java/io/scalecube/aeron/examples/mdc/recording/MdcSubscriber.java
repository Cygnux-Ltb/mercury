package io.scalecube.aeron.examples.mdc.recording;

import io.aeron.Aeron;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.CloseHelper;
import org.agrona.SystemUtil;

import static io.aeron.CommonContext.UDP_MEDIA;
import static io.scalecube.aeron.examples.AeronHelper.FRAGMENT_LIMIT;
import static io.scalecube.aeron.examples.mdc.recording.MdcRecordingPublisher.CONTROL_ENDPOINT;
import static io.scalecube.aeron.examples.mdc.recording.MdcRecordingPublisher.STREAM_ID;

public final class MdcSubscriber {

    private static final String AERON_DIR = SystemUtil.tmpDirName() + "aeron-sub";

    private static final String LIVE_ENDPOINT = "localhost:0";

    private static final long DEFAULT_POLLING_PERIOD_NS = 1_000_000_000L;

    private static final ChannelUriStringBuilder liveDestinationBuilder = new ChannelUriStringBuilder()
            .alias("events-liveDestination")
            .media(UDP_MEDIA)
            .endpoint(LIVE_ENDPOINT)
            .controlEndpoint(CONTROL_ENDPOINT);

    private static MediaDriver mediaDriver;
    private static Aeron aeron;

    public static void main(String[] args) throws InterruptedException {
        String pollingPeriodNs = System.getenv("POLLING_PERIOD_NS");

        System.out.println("POLLING_PERIOD_NS = " + pollingPeriodNs);

        setup(true);

        Subscription subscription = aeron.addSubscription(liveDestinationBuilder.build(), STREAM_ID);

        runPoller(subscription, pollingPeriodNs != null
                ? Long.parseLong(pollingPeriodNs) : DEFAULT_POLLING_PERIOD_NS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown...");
            CloseHelper.quietCloseAll(aeron, mediaDriver);
        }));
    }

    private static void setup(boolean cleanStart) {
        MediaDriver.Context mediaDriverContext = new MediaDriver.Context()
                .aeronDirectoryName(AERON_DIR)
                .threadingMode(ThreadingMode.DEDICATED)
                .dirDeleteOnStart(cleanStart)
                .dirDeleteOnShutdown(cleanStart);

        mediaDriver = MediaDriver.launch(mediaDriverContext);

        aeron = Aeron.connect(new Aeron.Context()
                .aeronDirectoryName(mediaDriverContext.aeronDirectoryName()));
    }

    private static void runPoller(Subscription subscription, long pollingPeriodNs) {
        Thread pollingThread = new Thread(() -> {
            long deadline = 0;
            while (true) {
                long now = System.nanoTime();
                if (now >= deadline) {
                    subscription.poll((buffer, offset, length, header) -> {
                        String message = buffer.getStringWithoutLengthAscii(offset, length);
                        System.out.println(message);
                    }, FRAGMENT_LIMIT);
                    deadline = now + pollingPeriodNs;
                }
            }
        },
                "subscription-poller");
        pollingThread.setDaemon(true);
        pollingThread.start();
    }
}
