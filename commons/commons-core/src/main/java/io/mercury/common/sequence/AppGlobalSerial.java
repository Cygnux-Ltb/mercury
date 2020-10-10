package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public final class ApplicationGlobalSerial {

	private static final AtomicLong Serial = new AtomicLong(0L);;

	private ApplicationGlobalSerial() {
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
