package io.mercury.common.sequence;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.log.LogConfigurator;

/**
 * 
 * Use the System.nanoTime() native function
 * 
 * @author yellow013
 *
 */
@ThreadSafe
public final class NanoSequence {

	/**
	 * 
	 * @return
	 */
	public static long milli() {
		return System.nanoTime() / 1000_000;
	}

	/**
	 * 
	 * @return
	 */
	public static long micro() {
		return System.nanoTime() / 1000;
	}

	/**
	 * 
	 * @return
	 */
	public static long nano() {
		return System.nanoTime();
	}

	public static void main(String[] args) {

		LogConfigurator.filename("test-log");
		Logger log = CommonLoggerFactory.getLogger(NanoSequence.class);

		for (int i = 0; i < 20; i++) {
			log.debug(String.valueOf(NanoSequence.micro()));
		}

	}

}
