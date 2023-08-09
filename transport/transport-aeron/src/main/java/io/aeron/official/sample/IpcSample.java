package io.aeron.official.sample;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class IpcSample {

    public static void main(String[] args) {
        var channel = "aeron:ipc";
        final String message = "my message";
        final IdleStrategy idle = new SleepingIdleStrategy();
        final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(ByteBuffer.allocate(128));
        try (MediaDriver driver = MediaDriver.launch();
             Aeron aeron = Aeron.connect();
             Subscription sub = aeron.addSubscription(channel, 10);
             Publication pub = aeron.addPublication(channel, 10)) {
            while (!pub.isConnected()) {
                idle.idle();
            }
            unsafeBuffer.putStringAscii(0, message);
            System.out.println("sending:" + message);
            while (pub.offer(unsafeBuffer) < 0) {
                idle.idle();
            }
            FragmentHandler handler = (buffer, offset, length, header) ->
                    System.out.println("received:" + buffer.getStringAscii(offset));
            while (sub.poll(handler, 1) <= 0) {
                idle.idle();
            }
        }

    }

}
