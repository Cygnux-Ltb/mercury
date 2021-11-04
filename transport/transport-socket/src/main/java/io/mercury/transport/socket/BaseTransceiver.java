package io.mercury.transport.socket;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.transport.api.Sender;
import io.mercury.transport.api.TransportComponent;

public abstract class BaseTransceiver<T> extends TransportComponent implements Transceiver<T> {

	private final Sender<T> sender;

	private final AbstractSingleConsumerQueue<T> queue;

	protected BaseTransceiver() {
		this.queue = initSendQueue();
		this.sender = new InnerSender(queue);
	}

	private class InnerSender extends TransportComponent implements Sender<T> {

		private AbstractSingleConsumerQueue<T> queue;

		private InnerSender(AbstractSingleConsumerQueue<T> queue) {
			this.queue = queue;
		}

		@Override
		public void sent(T msg) {
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
			queue.start();
			return true;
		} catch (Exception e) {
			throw new RuntimeException("start queue exception : " + e.getMessage(), e);
		}
	}

	@AbstractFunction
	protected abstract AbstractSingleConsumerQueue<T> initSendQueue();

}
