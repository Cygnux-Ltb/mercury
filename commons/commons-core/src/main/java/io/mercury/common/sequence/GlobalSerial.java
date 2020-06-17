package io.mercury.common.sequence;

import java.util.concurrent.atomic.AtomicLong;

public final class GlobalSerial {

	public final static AtomicLong InternalSerial = new AtomicLong(0L);;

	private GlobalSerial() {
	}

	public static long incrementAndGet() {
		return InternalSerial.incrementAndGet();
	}

}
