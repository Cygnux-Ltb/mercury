package io.mercury.transport.socket;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.collections.queue.Queue;
import io.mercury.transport.TransportComponent;
import io.mercury.transport.api.Sender;

public abstract class BaseTransceiver<T> extends TransportComponent implements Transceiver<T> {

    private final Sender<T> sender;

    protected BaseTransceiver() {
        this.sender = new InnerSender(initSendQueue());
    }

    private class InnerSender extends TransportComponent implements Sender<T> {

        private final Queue<T> queue;

        private InnerSender(Queue<T> queue) {
            this.queue = queue;
        }

        @Override
        public void send(T msg) {
            queue.enqueue(msg);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean closeIgnoreException() {
            return false;
        }

    }

    @Override
    public Sender<T> getSender() {
        return sender;
    }

    @Override
    public boolean startSend() {
        try {
            //queue.start();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("start queue exception : " + e.getMessage(), e);
        }
    }

    @AbstractFunction
    protected abstract Queue<T> initSendQueue();

}
