package io.mercury.common.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;

public final class SleepSupport {

	private SleepSupport() {
	}

	private static final Logger log = Log4j2LoggerFactory.getLogger(SleepSupport.class);

	/**
	 * 
	 */
	public static final void parkNano() {
		LockSupport.parkNanos(1);
	}

	/**
	 * 
	 * @param nanos
	 */
	public static final void parkNanos(long nanos) {
		LockSupport.parkNanos(nanos);
	}

	/**
	 * 
	 * @param millis
	 */
	public static final void sleepIgnoreInterrupts(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}", millis,
					e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @param nanos
	 */
	public static final void sleepIgnoreInterrupts(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}", millis,
					e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param timeUnit
	 * @param time
	 */
	public static final void sleepIgnoreInterrupts(@Nonnull TimeUnit timeUnit, long time) {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleepIgnoreInterrupts(timeUnit==[{}], time==[{}]) throw InterruptedException -> {}",
					timeUnit, time, e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(long millis) throws RuntimeInterruptedException {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleep(millis==[{}]) throw InterruptedException -> {}", millis, e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @param nanos
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(long millis, int nanos) throws RuntimeInterruptedException {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleep(millis==[{}], nanos==[{}]) throw InterruptedException -> {}", millis, nanos,
					e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

	/**
	 * 
	 * @param timeUnit
	 * @param time
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(@Nonnull TimeUnit timeUnit, long time) throws RuntimeInterruptedException {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			log.error("SleepSupport::sleep(timeUnit==[{}], time==[{}]) throw InterruptedException -> {}", timeUnit,
					time, e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

}
