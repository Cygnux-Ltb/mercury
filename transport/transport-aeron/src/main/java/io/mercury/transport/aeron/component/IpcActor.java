package io.mercury.transport.aeron.component;

import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import org.agrona.concurrent.IdleStrategy;

public abstract class IpcActor {

    private Publication pub;

    private Subscription sub;


    static {
        MediaDriver.launchEmbedded();
        MediaDriver launch = MediaDriver.launch();
    }

    abstract protected String channel();

    abstract protected int streamId();

    abstract protected IdleStrategy idleStrategy();

}
