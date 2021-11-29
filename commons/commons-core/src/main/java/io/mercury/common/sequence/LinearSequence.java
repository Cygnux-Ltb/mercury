package io.mercury.common.sequence;

import static io.mercury.common.datetime.TimeConst.NANOS_PER_MICROS;
import static io.mercury.common.datetime.TimeConst.NANOS_PER_MILLIS;
import static java.lang.System.nanoTime;

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
public final class LinearSequence {

	/**
	 * 
	 * 
	 * 
	 * @return System.nanoTime() / NANOS_PER_MILLIS
	 */
	public static long getMillis() {
		return nanoTime() / NANOS_PER_MILLIS;
	}

	/**
	 * 
	 * @return System.nanoTime() / NANOS_PER_MICROS
	 */
	public static long getMicros() {
		return nanoTime() / NANOS_PER_MICROS;
	}

	/**
	 * 
	 * @return System.nanoTime()
	 */
	public static long getNanos() {
		return nanoTime();
	}

	public static void main(String[] args) {

		LogConfigurator.setLogFilename("test-log");
		Logger log = CommonLoggerFactory.getLogger(LinearSequence.class);

		for (int i = 0; i < 20; i++) {
			log.debug(String.valueOf(LinearSequence.getMicros()));
		}

	}

}
