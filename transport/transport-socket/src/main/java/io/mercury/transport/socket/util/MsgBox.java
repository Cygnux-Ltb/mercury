package io.mercury.transport.socket.util;

import java.util.concurrent.ArrayBlockingQueue;

public class MsgBox {

	private ArrayBlockingQueue<byte[]> box = new ArrayBlockingQueue<>(4096);

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

	public void blockingInMsg(byte[] msg) throws InterruptedException {
		box.put(msg);
	}

	public byte[] blockingOutMsg() throws InterruptedException {
		return box.take();
	}

}
