package io.mercury.common.sequence;

import static java.lang.System.currentTimeMillis;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.util.BitFormatter;

/**
 * 
 * @author yellow013
 *
 */
@ThreadSafe
public final class EpochCycle {

	private final long cycleMillis;

	/**
	 * 
	 * @param cycle
	 */
	public EpochCycle(Duration cycle) {
		this(cycle.toMillis());
	}

	/**
	 * 
	 * @param cycleMillis
	 */
	public EpochCycle(long cycleMillis) {
		this.cycleMillis = cycleMillis < 1L ? 1L : cycleMillis;
	}

	/**
	 * 
	 * @return
	 */
	public final long toCycle() {
		return toCycle(currentTimeMillis());
	}

	/**
	 * 
	 * @param epochMillis
	 * @return
	 */
	public final long toCycle(long epochMillis) {
		return epochMillis < 0 ? 0 : (epochMillis / cycleMillis);
	}

	/**
	 * 
	 * @param cycle
	 * @return
	 */
	public final long toEpochMillis(long cycle) {
		return cycle * cycleMillis;
	}

	public static void main(String[] args) {

		long l = -1L >>> 16;

		System.out.println(BitFormatter.longBinaryFormat(l));

		System.out.println(ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.UTC));

		System.out.println(BitFormatter.longBinaryFormat(
				ZonedDateTime.of(LocalDate.of(3000, 1, 1), LocalTime.MIN, ZoneOffset.UTC).toEpochSecond() * 1000));

	}

}
