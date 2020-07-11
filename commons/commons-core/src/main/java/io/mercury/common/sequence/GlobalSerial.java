package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public final class GlobalSerial {

	private static final AtomicLong Serial = new AtomicLong(0L);;

	private GlobalSerial() {
	}

	/**
	 * 
	 * @return
	 */
	public static final long incrementAndGet() {
		return Serial.incrementAndGet();
	}

	/**
	 * 
	 * @return
	 */
	public static final long getAndIncrement() {
		return Serial.getAndIncrement();
	}

}
