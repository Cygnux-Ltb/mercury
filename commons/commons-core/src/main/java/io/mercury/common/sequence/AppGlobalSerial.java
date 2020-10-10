package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public final class AppGlobalSerial {

	private static final AtomicLong GlobalSerial = new AtomicLong(0L);;

	private AppGlobalSerial() {
	}

	/**
	 * 
	 * @return
	 */
	public static final long incrementAndGet() {
		return GlobalSerial.incrementAndGet();
	}

	/**
	 * 
	 * @return
	 */
	public static final long getAndIncrement() {
		return GlobalSerial.getAndIncrement();
	}

}
