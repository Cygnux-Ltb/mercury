package io.mercury.transport.aeron.component;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;

public class IpcAllocator {

    private final Aeron aeron;

    private static final IpcAllocator INSTANCE = new IpcAllocator();

    private IpcAllocator() {
        MediaDriver.launchEmbedded();
        this.aeron = Aeron.connect();
    }


    public static Publication acquirePub(final String channel, final int streamId) {
        return INSTANCE.aeron.addPublication(channel, streamId);
    }

    public static Subscription acquireSub(final String channel, final int streamId) {
        return INSTANCE.aeron.addSubscription(channel, streamId);
    }

}
