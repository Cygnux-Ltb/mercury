package io.mercury.common.sequence;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;

public final class SysSequence {

	public static long milli() {
		return System.nanoTime() / 1000_000;
	}

	public static long micro() {
		return System.nanoTime() / 1000;
	}

	public static long nano() {
		return System.nanoTime();
	}

	public static void main(String[] args) {

		Logger log = CommonLoggerFactory.getLogger(SysSequence.class);

		for (int i = 0; i < 20; i++) {
			log.debug(String.valueOf(SysSequence.micro()));
		}

	}

}
