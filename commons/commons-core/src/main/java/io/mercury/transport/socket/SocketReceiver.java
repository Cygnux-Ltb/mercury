package io.mercury.transport.socket;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.Sleep;
import io.mercury.common.thread.Threads;
import io.mercury.transport.TransportComponent;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.socket.configurator.SocketConfigurator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SocketReceiver extends TransportComponent implements Receiver {

    private final SocketConfigurator configurator;

    private final Consumer<byte[]> callback;

    private Socket socket;

    private final AtomicBoolean isReceiving = new AtomicBoolean(false);
    private final AtomicBoolean isRun = new AtomicBoolean(false);

    protected static final Logger log = Log4j2LoggerFactory.getLogger(SocketReceiver.class);

    /**
     * @param configurator SocketConfigurator
     * @param callback     Consumer<byte[]>
     */
    public SocketReceiver(SocketConfigurator configurator, Consumer<byte[]> callback) {
        Asserter.nonNull(configurator, "configurator");
        Asserter.nonNull(callback, "callback");
        this.configurator = configurator;
        this.callback = callback;
        init();
    }

    private void init() {
        try {
            this.socket = new Socket(configurator.getHost(), configurator.getPort());
        } catch (Exception e) {
            log.error("new Socket({}, {}) throw Exception -> {}", configurator.getHost(), configurator.getPort(),
                    e.getMessage(), e);
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
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            log.error("socket.close() throw IOException -> {}", e.getMessage(), e);
        }
        return true;
    }

    @Override
    public String getName() {
        return "SocketReceiver -> " + socket.hashCode();
    }

    @Override
    public void receive() {
        if (!isRun.get())
            isRun.set(true);
        if (!isReceiving.get())
            startReceiveThread();
    }

    private synchronized void startReceiveThread() {
        if (isReceiving.get())
            return;
        isReceiving.set(true);
        Threads.startNewThread(() -> {
            InputStream inputStream;
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            while (isRun.get()) {
                try {
                    int available = inputStream.available();
                    if (available == 0) {
                        Sleep.millis(configurator.receiveInterval());
                        continue;
                    }
                    byte[] bytes = new byte[available];
                    IOUtils.read(inputStream, bytes);
                    callback.accept(bytes);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {
                    }
                    closeIgnoreException();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        });
    }

    public static void main(String[] args) {

        SocketConfigurator configurator = SocketConfigurator.builder().host("192.168.1.138").port(7901).build();

        SocketReceiver receiver = new SocketReceiver(configurator, bytes -> System.out.println(new String(bytes)));

        receiver.receive();

        try {
            receiver.close();
        } catch (IOException ignored) {
        }

    }

    @Override
    public void reconnect() {

    }

    @Override
    public void close() throws IOException {
        closeIgnoreException();
    }

}
