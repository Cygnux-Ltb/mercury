package io.mercury.transport.socket;

import io.mercury.common.collections.queue.Queue;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.transport.api.Sender;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.socket.configurator.SocketConfigurator;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SocketSender extends TransportComponent implements Sender<byte[]> {

    private final SocketConfigurator configurator;

    private Socket socket;

    private final AtomicBoolean isRun = new AtomicBoolean(true);

    private static final Logger log = Log4j2LoggerFactory.getLogger(SocketSender.class);

    private final Queue<byte[]> innerQueue;


    public SocketSender(SocketConfigurator configurator, Queue<byte[]> queue) {
        Asserter.nonNull(configurator, "configurator");
        this.configurator = configurator;
        this.innerQueue = queue;
        init();
    }

    private void init() {
        try {
            this.socket = new Socket(configurator.getHost(), configurator.getPort());
        } catch (IOException e) {
            log.error("Throw IOException -> {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    @Override
    public boolean closeIgnoreException() {
        this.isRun.set(false);
        try {
            outputStream.close();
            if (socket != null)
                socket.close();
        } catch (IOException _) {
        }
        return true;
    }

    @Override
    public String getName() {
        return STR."SocketSender -> \{socket.hashCode()}";
    }

    @Override
    public void send(byte[] msg) {
        innerQueue.enqueue(msg);
    }

    private DataOutputStream outputStream;

    private void processSendQueue(byte[] msg) {
        try {
            if (isRun.get()) {
                if (outputStream == null)
                    outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.write(msg);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            closeIgnoreException();
        }
    }


    public static void main(String[] args) throws IOException {
        SocketConfigurator configurator = SocketConfigurator.builder().host("192.168.1.138").port(7901).build();
        try (SocketSender sender = new SocketSender(configurator, null)) {
            sender.send("hello".getBytes());
        }
    }

}
