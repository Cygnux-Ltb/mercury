package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public final class JvmGlobalSerial {

	private static final AtomicLong GlobalSerial = new AtomicLong(0L);

	private JvmGlobalSerial() {
	}

	/**
	 * 
	 * @return
	 */
	public static final long next() {
		return GlobalSerial.incrementAndGet();
	}

}
