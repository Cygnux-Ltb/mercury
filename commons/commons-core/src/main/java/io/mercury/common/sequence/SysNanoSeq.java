package io.mercury.common.sequence;

import static java.lang.System.nanoTime;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLogConfigurator;
import io.mercury.common.log.CommonLoggerFactory;

/**
 * 
 * Use the System.nanoTime() native function
 * 
 * @author yellow013
 *
 */
@ThreadSafe
public final class SysNanoSeq {

	/**
	 * 
	 * @return
	 */
	public static long millis() {
		return nanoTime() / 1000_000;
	}

	/**
	 * 
	 * @return
	 */
	public static long micros() {
		return nanoTime() / 1000;
	}

	/**
	 * 
	 * @return
	 */
	public static long nanos() {
		return nanoTime();
	}

	public static void main(String[] args) {

		CommonLogConfigurator.setFilename("test-log");
		Logger log = CommonLoggerFactory.getLogger(SysNanoSeq.class);

		for (int i = 0; i < 20; i++) {
			log.debug(String.valueOf(SysNanoSeq.micros()));
		}

	}

}
