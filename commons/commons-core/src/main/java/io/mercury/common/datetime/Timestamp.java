package io.mercury.common.datetime;

import static io.mercury.common.util.StringUtil.toText;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;

import io.mercury.common.serialization.JsonSerializable;
import io.mercury.common.util.Assertor;

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
	 * java.time.Instant
	 */
	private Instant instant;

	/**
	 * java.time.ZonedDateTime
	 */
	private ZonedDateTime zonedDateTime;

	/**
	 * 
	 * @param epochMillis
	 */
	private Timestamp(long epochMillis) {
		this.epochMillis = epochMillis;
		this.sysNanoTime = nanoTime();
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp newWithNow() {
		return new Timestamp(currentTimeMillis());
	}

	/**
	 * 
	 * @param epochMillis
	 * @return
	 */
	public static Timestamp newWithEpochMillis(long epochMillis) {
		return new Timestamp(epochMillis);
	}

	/**
	 * 
	 * @param instant
	 * @return
	 */
	public static Timestamp newWithInstant(@Nonnull Instant instant) {
		Assertor.nonNull(instant, "instant");
		Timestamp timestamp = new Timestamp(instant.toEpochMilli());
		timestamp.instant = instant;
		return timestamp;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static Timestamp newWithDateTime(@Nonnull LocalDateTime datetime) {
		Assertor.nonNull(datetime, "datetime");
		return newWithDateTime(ZonedDateTime.of(datetime, TimeZone.SYS_DEFAULT));
	}

	/**
	 * 
	 * @param datetime
	 * @param zoneId
	 * @return
	 */
	public static Timestamp newWithDateTime(@Nonnull LocalDateTime datetime, @Nonnull ZoneId zoneId) {
		Assertor.nonNull(datetime, "datetime");
		Assertor.nonNull(zoneId, "zoneId");
		return newWithDateTime(ZonedDateTime.of(datetime, zoneId));
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp newWithDateTime(@Nonnull ZonedDateTime zonedDateTime) {
		Assertor.nonNull(zonedDateTime, "zonedDateTime");
		final Instant instant = zonedDateTime.toInstant();
		Timestamp timestamp = new Timestamp(instant.toEpochMilli());
		timestamp.instant = instant;
		timestamp.zonedDateTime = zonedDateTime;
		return timestamp;
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
	public ZonedDateTime getZonedDateTime() {
		if (zonedDateTime == null)
			return resetAndGetDateTimeOf(TimeZone.SYS_DEFAULT);
		return zonedDateTime;
	}

	/**
	 * 根据Epoch毫秒数生成Instant
	 */
	private void newInstantOfEpochMillis() {
		if (instant == null)
			this.instant = Instant.ofEpochMilli(epochMillis);
	}

	/**
	 * 根据指定时区更新并获取时间
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime resetAndGetDateTimeOf(ZoneId zoneId) {
		newInstantOfEpochMillis();
		this.zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		return zonedDateTime;
	}

	@Override
	public int compareTo(Timestamp o) {
		return epochMillis < o.epochMillis ? -1
				: epochMillis > o.epochMillis ? 1
						: sysNanoTime < o.sysNanoTime ? -1 : sysNanoTime > sysNanoTime ? 1 : 0;
	}

	private static final String epochMillisField = "{\"epochMillis\" : ";
	private static final String instantField = ", \"instant\" : ";
	private static final String zonedDateTimeField = ", \"zonedDateTime\" : ";
	private static final String end = "}";

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(90);
		builder.append(epochMillisField);
		builder.append(epochMillis);
		if (instant != null) {
			builder.append(instantField);
			builder.append(toText(instant));
		}
		if (zonedDateTime != null) {
			builder.append(zonedDateTimeField);
			builder.append(toText(zonedDateTime));
		}
		builder.append(end);
		return builder.toString();
	}

	@Override
	public String toJson() {
		return toString();
	}

	public static void main(String[] args) {

		Timestamp timestamp = Timestamp.newWithNow();
		timestamp.getInstant();
		timestamp.getZonedDateTime();
		System.out.println(timestamp);

		for (int i = 0; i < 100000; i++) {
			EpochTime.millis();
			Timestamp.newWithNow();
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
		Timestamp.newWithNow();
		long l1_1 = System.nanoTime();

		long l2_0 = System.nanoTime();
		Instant.now();
		long l2_1 = System.nanoTime();

		long l1 = l1_1 - l1_0;
		long l2 = l2_1 - l2_0;

		System.out.println(l1);
		System.out.println(l2);

		Timestamp now = Timestamp.newWithNow();

		System.out.println(now.getEpochMillis());
		System.out.println(now.getInstant().getEpochSecond() * 1000000 + now.getInstant().getNano() / 1000);
		System.out.println(now.getInstant());
		System.out.println(now.getZonedDateTime());
		System.out.println(now);

		System.out.println(Timestamp.newWithNow());
		System.out.println(Timestamp.newWithEpochMillis(47237547328L).resetAndGetDateTimeOf(TimeZone.UTC));
		System.out.println(Timestamp.newWithDateTime(LocalDateTime.now(), TimeZone.CST));

	}

}
