package io.mercury.common.datetime;

import static io.mercury.common.datetime.EpochUnit.MICROS;
import static io.mercury.common.datetime.EpochUnit.MILLIS;
import static io.mercury.common.datetime.EpochUnit.NANOS;
import static io.mercury.common.datetime.EpochUnit.SECOND;
import static io.mercury.common.datetime.TimeConst.MICROS_PER_SECONDS;
import static io.mercury.common.datetime.TimeConst.NANOS_PER_SECOND;
import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;
import static java.lang.Math.floorDiv;
import static java.lang.Math.floorMod;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.ofEpochMilli;
import static java.time.Instant.ofEpochSecond;
import static java.time.ZonedDateTime.ofInstant;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;

import io.mercury.common.serialization.JsonSerializable;

/**
 * 
 * @author yellow013
 *
 */
public final class Timestamp implements Comparable<Timestamp>, JsonSerializable {

	/**
	 * Epoch
	 */
	private final long epoch;

	/**
	 * EpochUnit
	 */
	private final EpochUnit epochUnit;

	/**
	 * java.time.Instant
	 */
	private Instant instant;

	/**
	 * 
	 * @param epoch
	 * @param unit
	 */
	private Timestamp(long epoch, @Nonnull EpochUnit unit) {
		this.epoch = epoch;
		this.epochUnit = unit;
	}

	public static Timestamp nowWithSecond() {
		return new Timestamp(Epochs.getEpochSeconds(), SECOND);
	}

	public static Timestamp nowWithMillis() {
		return new Timestamp(currentTimeMillis(), MILLIS);
	}

	public static Timestamp nowWithMicros() {
		return new Timestamp(Epochs.getEpochMicros(), MICROS);
	}

	public static Timestamp nowWithNanos() {
		return new Timestamp(Epochs.getEpochNanos(), NANOS);
	}

	public static Timestamp withEpochSecond(long epochSecond) {
		return new Timestamp(epochSecond, SECOND);
	}

	public static Timestamp withEpochMillis(long epochMillis) {
		return new Timestamp(epochMillis, MILLIS);
	}

	public static Timestamp withEpochMicros(long epochMicros) {
		return new Timestamp(epochMicros, MICROS);
	}

	public static Timestamp withEpochNanos(long epochNanos) {
		return new Timestamp(epochNanos, NANOS);
	}

	public static Timestamp withDateTime(LocalDate date, LocalTime time) {
		return withDateTime(ZonedDateTime.of(date, time, SYS_DEFAULT));
	}

	public static Timestamp withDateTime(LocalDate date, LocalTime time, ZoneId zoneId) {
		return withDateTime(ZonedDateTime.of(date, time, zoneId));
	}

	public static Timestamp withDateTime(LocalDateTime datetime) {
		return withDateTime(ZonedDateTime.of(datetime, SYS_DEFAULT));
	}

	public static Timestamp withDateTime(LocalDateTime datetime, ZoneId zoneId) {
		return withDateTime(ZonedDateTime.of(datetime, zoneId));
	}

	public static Timestamp withDateTime(ZonedDateTime datetime) {
		return new Timestamp(datetime.toInstant().toEpochMilli(), MILLIS);
	}

	/**
	 * 
	 * @return
	 */
	public long getEpoch() {
		return epoch;
	}

	/**
	 * 
	 * @return
	 */
	public EpochUnit getEpochUnit() {
		return epochUnit;
	}

	/**
	 * 
	 * @return
	 */
	public Instant getInstant() {
		newInstantOfEpochMillis();
		return instant;
	}

	/**
	 * 根据指定时区获取时间
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime getDateTimeWith(ZoneId zoneId) {
		newInstantOfEpochMillis();
		return ofInstant(instant, zoneId);
	}

	/**
	 * 根据Epoch毫秒数生成Instant
	 */
	private void newInstantOfEpochMillis() {
		if (instant == null) {
			switch (epochUnit) {
			case SECOND:
				this.instant = ofEpochSecond(epoch);
				break;
			case MILLIS:
				this.instant = ofEpochMilli(epoch);
				break;
			case MICROS:
				this.instant = ofEpochSecond(floorDiv(epoch, MICROS_PER_SECONDS),
						floorMod(epoch, TimeConst.MICROS_PER_SECONDS));
				break;
			case NANOS:
				this.instant = ofEpochSecond(floorDiv(epoch, NANOS_PER_SECOND), floorMod(epoch, NANOS_PER_SECOND));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public int compareTo(Timestamp o) {
		return epoch < o.epoch ? -1 : epoch > o.epoch ? 1 : 0;
	}

	private static final String epochField = "{\"epoch\" : ";
	private static final String epochUnitField = ", \"epochUnit\" : ";
	private static final String instantField = ", \"instant\" : ";
	private static final String end = "}";

	@Override
	public String toString() {
		var builder = new StringBuilder(90);
		builder.append(epochField);
		builder.append(epoch);
		builder.append(epochUnitField);
		builder.append(epochUnit);
		if (instant != null) {
			builder.append(instantField);
			builder.append(instant);
		}
		builder.append(end);
		return builder.toString();
	}

	@Override
	public String toJson() {
		return toString();
	}

	public static void main(String[] args) {

		System.out.println(TimeZone.CST);

		Timestamp timestamp = Timestamp.nowWithMillis();
		System.out.println(timestamp);

		for (int i = 0; i < 100000; i++) {
			Epochs.getEpochMillis();
			Timestamp.nowWithMillis();
			Instant.now();
			i++;
			i--;
		}

		for (int i = 0; i < 10000; i++) {
			long l0_0 = System.nanoTime();
			// EpochTime.milliseconds();
			// EpochTimestamp.now();
			Instant.now();
			long l0_1 = System.nanoTime();
			long l0 = l0_1 - l0_0;
			System.out.println(l0);
		}

		long l1_0 = System.nanoTime();
		Timestamp.nowWithMillis();
		long l1_1 = System.nanoTime();

		long l2_0 = System.nanoTime();
		Instant.now();
		long l2_1 = System.nanoTime();

		long l1 = l1_1 - l1_0;
		long l2 = l2_1 - l2_0;

		System.out.println(l1);
		System.out.println(l2);

		Timestamp now = Timestamp.nowWithMillis();

		System.out.println(now.getEpoch());
		System.out.println(now.getInstant().getEpochSecond() * 1000000 + now.getInstant().getNano() / 1000);
		System.out.println(now.getInstant());
		System.out.println(now);

		System.out.println(Timestamp.nowWithMillis());
		System.out.println(Timestamp.withEpochMillis(47237547328L).getDateTimeWith(TimeZone.UTC));
		System.out.println(Timestamp.withDateTime(LocalDateTime.now(), TimeZone.CST));

	}

}
