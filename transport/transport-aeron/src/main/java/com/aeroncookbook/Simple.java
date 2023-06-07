package com.aeroncookbook;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class Simple {

    public static void main(String[] args) {
        final String channel = "aeron:ipc";
        final String message = "my message";
        final IdleStrategy idleStrategy = new SleepingIdleStrategy();
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocate(256));
        try (MediaDriver driver = MediaDriver.launch();
             Aeron aeron = Aeron.connect();
             Publication pub = aeron.addPublication(channel, 10);
             Subscription sub = aeron.addSubscription(channel, 10)) {

            while (!pub.isConnected()) {
                idleStrategy.idle();
            }

            unsafeBuffer.putStringAscii(0, message);
            System.out.println("sending:" + message);

            while (pub.offer(unsafeBuffer) < 0) {
                idleStrategy.idle();
            }

            FragmentHandler handler = (buffer, offset, length, header) ->
                    System.out.println("received:" + buffer.getStringAscii(offset));

            while (sub.poll(handler, 1) <= 0) {
                idleStrategy.idle();
            }
        }
    }

}
