package io.scalecube.aeron.examples;

import io.aeron.Aeron;
import io.aeron.Aeron.Context;
import io.aeron.ChannelUriStringBuilder;
import io.aeron.FragmentAssembler;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.CloseHelper;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.SigInt;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.aeron.CommonContext.UDP_MEDIA;
import static io.scalecube.aeron.examples.AeronHelper.FRAGMENT_LIMIT;
import static io.scalecube.aeron.examples.AeronHelper.STREAM_ID;
import static io.scalecube.aeron.examples.AeronHelper.printAsciiMessage;
import static io.scalecube.aeron.examples.AeronHelper.printSubscription;

public class MultiStreamSubscriber {

    public static final String ENDPOINT = "localhost:20121";

    private static Aeron aeron;
    private static MediaDriver mediaDriver;
    private static final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Main runner.
     *
     * @param args args
     */
    public static void main(String[] args) {
        SigInt.register(MultiStreamSubscriber::close);

        mediaDriver = MediaDriver.launchEmbedded();
        String aeronDirectoryName = mediaDriver.aeronDirectoryName();

        Context context = new Context()
                .aeronDirectoryName(aeronDirectoryName)
                .availableImageHandler(AeronHelper::printAvailableImage)
                .unavailableImageHandler(AeronHelper::printUnavailableImage);

        aeron = Aeron.connect(context);
        System.out.println("hello, " + context.aeronDirectoryName());

        String channel = new ChannelUriStringBuilder().media(UDP_MEDIA).endpoint(ENDPOINT).build();
        Subscription subscription = aeron
                .addSubscription(channel, STREAM_ID); // conn: 20121 / logbuffer: 48M
        Subscription subscription2 = aeron
                .addSubscription(channel, STREAM_ID + 1); // conn: 20121 / logbuffer: 48M

        printSubscription(subscription);
        printSubscription(subscription2);

        final FragmentHandler fragmentHandler = printAsciiMessage(STREAM_ID);
        FragmentAssembler fragmentAssembler = new FragmentAssembler(fragmentHandler);
        FragmentAssembler fragmentAssembler2 = new FragmentAssembler(fragmentHandler);
        BackoffIdleStrategy idleStrategy = new BackoffIdleStrategy();

        while (running.get()) {
            idleStrategy.idle(subscription.poll(fragmentAssembler, FRAGMENT_LIMIT));
            idleStrategy.idle(subscription2.poll(fragmentAssembler2, FRAGMENT_LIMIT));
        }

        System.out.println("Shutting down...");

        close();
    }

    private static void close() {
        running.set(false);
        CloseHelper.close(aeron);
        CloseHelper.close(mediaDriver);
    }
}
