package io.mercury.transport.socket;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.concurrent.queue.ScQueue;
import io.mercury.transport.api.Sender;

public abstract class BaseTransceiver<T> implements Transceiver<T> {

	private final Sender<T> sender;

	private final ScQueue<T> queue;

	protected BaseTransceiver() {
		this.queue = initSendQueue();
		this.sender = new InnerSender(queue);
	}

	private class InnerSender implements Sender<T> {

		private ScQueue<T> queue;

		private InnerSender(ScQueue<T> queue) {
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
		public boolean destroy() {
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
	protected abstract ScQueue<T> initSendQueue();

}
