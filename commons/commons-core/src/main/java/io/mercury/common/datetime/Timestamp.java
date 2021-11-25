package io.mercury.common.datetime;

import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.mercury.common.serialization.JsonSerializable;

/**
 * 
 * @author yellow013
 *
 */
public final class Timestamp implements Comparable<Timestamp>, JsonSerializable {

	/**
	 * Epoch Milliseconds
	 */
	private final long epochMillis;

	/**
	 * sysNanoTime is use {@link System.nanoTime()}
	 */
	private final long sysNanoTime;

	/**
	 * java.time.ZoneOffset
	 */
	private final ZoneOffset offset;

	/**
	 * java.time.Instant
	 */
	private Instant instant;

	/**
	 * java.time.ZonedDateTime
	 */
	private ZonedDateTime datetime;

	/**
	 * 
	 * @param epochMillis
	 * @param zoneId
	 * @param instant
	 * @param datetime
	 */
	private Timestamp(long epochMillis, @Nonnull ZoneOffset offset, @Nullable Instant instant,
			@Nullable ZonedDateTime datetime) {
		this.sysNanoTime = nanoTime();
		this.epochMillis = epochMillis;
		this.offset = offset;
		this.instant = instant;
		this.datetime = datetime;
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp now() {
		return new Timestamp(currentTimeMillis(), SYS_DEFAULT, null, null);
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp now(@Nonnull ZoneOffset offset) {
		return new Timestamp(currentTimeMillis(), offset, null, null);
	}

	/**
	 * 
	 * @param epochMillis
	 * @return
	 */
	public static Timestamp withEpochMillis(long epochMillis) {
		return new Timestamp(epochMillis, SYS_DEFAULT, null, null);
	}

	/**
	 * 
	 * @param epochMillis
	 * @param zoneId
	 * @return
	 */
	public static Timestamp withEpochMillis(long epochMillis, @Nonnull ZoneOffset offset) {
		return new Timestamp(epochMillis, offset, null, null);
	}

	/**
	 * 
	 * @param instant
	 * @return
	 */
	public static Timestamp withInstant(@Nonnull Instant instant) {
		return new Timestamp(instant.toEpochMilli(), SYS_DEFAULT, instant, null);
	}

	/**
	 * 
	 * @param instant
	 * @param zoneId
	 * @return
	 */
	public static Timestamp withInstant(@Nonnull Instant instant, @Nonnull ZoneOffset offset) {
		return new Timestamp(instant.toEpochMilli(), offset, instant, null);
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Timestamp withDateTime(@Nonnull LocalDate date, LocalTime time) {
		return withDateTime(ZonedDateTime.of(LocalDateTime.of(date, time), SYS_DEFAULT));
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @param zoneId
	 * @return
	 */
	public static Timestamp withDateTime(@Nonnull LocalDate date, LocalTime time, @Nonnull ZoneId zoneId) {
		return withDateTime(ZonedDateTime.of(LocalDateTime.of(date, time), zoneId));
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static Timestamp withDateTime(@Nonnull LocalDateTime datetime) {
		return withDateTime(ZonedDateTime.of(datetime, SYS_DEFAULT));
	}

	/**
	 * 
	 * @param datetime
	 * @param zoneId
	 * @return
	 */
	public static Timestamp withDateTime(@Nonnull LocalDateTime datetime, @Nonnull ZoneId zoneId) {
		return withDateTime(ZonedDateTime.of(datetime, zoneId));
	}

	/**
	 * 
	 * @param zonedDateTime
	 * @return
	 */
	public static Timestamp withDateTime(@Nonnull ZonedDateTime zonedDateTime) {
		final Instant instant = zonedDateTime.toInstant();
		return new Timestamp(instant.toEpochMilli(), zonedDateTime.getOffset(), instant, zonedDateTime);
	}

	/**
	 * 
	 * @return
	 */
	public long getEpochMillis() {
		return epochMillis;
	}

	/**
	 * 
	 * @return
	 */
	public long getSysNanoTime() {
		return sysNanoTime;
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
	 * 
	 * @return
	 */
	public ZoneOffset getOffset() {
		return offset;
	}

	/**
	 * 
	 * @return
	 */
	public ZonedDateTime getDatetime() {
		if (datetime == null)
			this.datetime = getDatetimeWith(offset);
		return datetime;
	}

	/**
	 * 根据指定时区更新并获取时间
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime getDatetimeWith(ZoneId zoneId) {
		newInstantOfEpochMillis();
		return ZonedDateTime.ofInstant(instant, zoneId);
	}

	/**
	 * 根据Epoch毫秒数生成Instant
	 */
	private void newInstantOfEpochMillis() {
		if (instant == null)
			this.instant = Instant.ofEpochMilli(epochMillis);
	}

	@Override
	public int compareTo(Timestamp o) {
		return epochMillis < o.epochMillis ? -1
				: epochMillis > o.epochMillis ? 1
						: sysNanoTime < o.sysNanoTime ? -1 : sysNanoTime > sysNanoTime ? 1 : 0;
	}

	private static final String epochMillisField = "{\"epochMillis\" : ";
	private static final String offsetField = ", \"offset\" : ";
	private static final String instantField = ", \"instant\" : ";
	private static final String datetimeField = ", \"datetime\" : ";
	private static final String end = "}";

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(90);
		builder.append(epochMillisField);
		builder.append(epochMillis);
		builder.append(offsetField);
		builder.append(offset);
		if (instant != null) {
			builder.append(instantField);
			builder.append(instant);
		}
		if (datetime != null) {
			builder.append(datetimeField);
			builder.append(datetime);
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

		Timestamp timestamp = Timestamp.now();
		System.out.println(timestamp);

		for (int i = 0; i < 100000; i++) {
			EpochUtil.getEpochMillis();
			Timestamp.now();
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
		Timestamp.now();
		long l1_1 = System.nanoTime();

		long l2_0 = System.nanoTime();
		Instant.now();
		long l2_1 = System.nanoTime();

		long l1 = l1_1 - l1_0;
		long l2 = l2_1 - l2_0;

		System.out.println(l1);
		System.out.println(l2);

		Timestamp now = Timestamp.now();

		System.out.println(now.getEpochMillis());
		System.out.println(now.getInstant().getEpochSecond() * 1000000 + now.getInstant().getNano() / 1000);
		System.out.println(now.getInstant());
		System.out.println(now.getDatetime());
		System.out.println(now);

		System.out.println(Timestamp.now());
		System.out.println(Timestamp.withEpochMillis(47237547328L).getDatetimeWith(TimeZone.UTC));
		System.out.println(Timestamp.withDateTime(LocalDateTime.now(), TimeZone.CST));

	}

}
