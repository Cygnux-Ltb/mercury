package io.mercury.transport.zmq;

import io.mercury.common.log4j2.Log4j2Configurator;
import io.mercury.common.thread.Sleep;
import io.mercury.common.thread.ThreadSupport;
import io.mercury.transport.attr.Topics;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class ZmqTcpPubSubTest {

    static {
        Log4j2Configurator.useInfoLogLevel();
    }

    @Test
    public void test() {
        System.out.println("ZmqTcpPubSubTest Test Start");

        String topic = "tcp-test";

        ThreadSupport.startNewThread(() -> {
            try (ZmqSubscriber subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(1)
                    .createSubscriber(Topics.with(topic), this::handleMag)) {
                subscriber.subscribe();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Sleep.millis(3000);

        try (ZmqPublisher<String> publisher = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(1)
                .createPublisherWithString(topic)) {
            Sleep.millis(2000);
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                publisher.publish(String.valueOf(random.nextInt()));
                Sleep.millis(200);
            }
            publisher.publish("end");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Sleep.millis(5000);

    }

    private void handleMag(byte[] topic, byte[] msg) {
        String str = new String(msg);
        System.out.println(new String(topic) + " -> " + str);
        if (str.equalsIgnoreCase("end"))
            System.exit(0);
    }

}
