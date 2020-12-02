package io.mercury.transport.socket.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.mercury.common.thread.RuntimeInterruptedException;

public class MsgBox {

	private BlockingQueue<byte[]> box = new ArrayBlockingQueue<>(512);

	private static final MsgBox instance = new MsgBox();

	public static MsgBox instance() {
		return instance;
	}

	private MsgBox() {
	}

	public boolean inMsg(byte[] msg) {
		return box.offer(msg);
	}

	public byte[] outMsg() {
		return box.poll();
	}

	public void blockingInMsg(byte[] msg) {
		try {
			box.put(msg);
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException("blockingInMsg throw InterruptedException", e);
		}
	}

	public byte[] blockingOutMsg() {
		try {
			return box.take();
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException("blockingOutMsg throw InterruptedException", e);
		}
	}

}
