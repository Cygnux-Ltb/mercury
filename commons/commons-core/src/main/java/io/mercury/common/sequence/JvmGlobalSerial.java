package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public enum JvmGlobalSerial {

	;

	private static final AtomicLong GlobalSerial = new AtomicLong(0L);

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
