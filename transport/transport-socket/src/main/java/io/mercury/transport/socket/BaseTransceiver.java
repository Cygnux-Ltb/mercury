package io.mercury.transport.socket;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.concurrent.queue.base.ScQueue;
import io.mercury.transport.core.api.Sender;

public abstract class BaseTransceiver<T> implements Transceiver<T> {

	private Sender<T> sender;

	private ScQueue<T> queue;

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
		public void send(T msg) {
			queue.enqueue(msg);
		}

		@Override
		public String name() {
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
	public void startSend() {
		queue.start();
	}

	@AbstractFunction
	protected abstract ScQueue<T> initSendQueue();

}
