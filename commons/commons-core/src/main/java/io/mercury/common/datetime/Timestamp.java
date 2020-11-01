package io.mercury.common.datetime;

import static io.mercury.common.util.StringUtil.toText;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import io.mercury.common.util.Assertor;

/**
 * 
 * @author yellow013
 *
 */
public final class Timestamp implements Comparable<Timestamp> {

	/**
	 * Epoch Milliseconds
	 */
	private final long epochMillis;

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
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp newWithNow() {
		return new Timestamp(System.currentTimeMillis());
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
	 * @param localDateTime
	 * @return
	 */
	public static Timestamp newWithDateTime(LocalDateTime localDateTime) {
		Assertor.nonNull(localDateTime, "localDateTime");
		return newWithZonedDateTime(
				ZonedDateTime.ofLocal(localDateTime, TimeZone.SYS_DEFAULT, TimeZone.SYS_DEFAULT_OFFSET));
	}

	/**
	 * 
	 * @param localDateTime
	 * @param zoneId
	 * @return
	 */
	public static Timestamp newWithDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
		Assertor.nonNull(localDateTime, "localDateTime");
		Assertor.nonNull(zoneId, "zoneId");
		return newWithZonedDateTime(ZonedDateTime.of(localDateTime, zoneId));
	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp newWithZonedDateTime(ZonedDateTime zonedDateTime) {
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
	 * 根据Epoch毫秒数生成Instant
	 */
	private void newInstantOfEpochMillis() {
		this.instant = Instant.ofEpochMilli(epochMillis);
	}

	/**
	 * 
	 * @return
	 */
	public Instant getInstant() {
		if (instant == null)
			newInstantOfEpochMillis();
		return instant;
	}

	/**
	 * 根据指定时区更新并获取时间
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime updateAndGetDateTimeOf(ZoneId zoneId) {
		if (instant == null)
			newInstantOfEpochMillis();
		this.zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		return zonedDateTime;
	}

	/**
	 * 
	 * @return
	 */
	public ZonedDateTime getZonedDateTime() {
		if (zonedDateTime == null)
			return updateAndGetDateTimeOf(TimeZone.SYS_DEFAULT);
		return zonedDateTime;
	}

	@Override
	public int compareTo(Timestamp o) {
		return epochMillis < o.epochMillis ? -1 : epochMillis > o.epochMillis ? 1 : 0;
	}

	/**
	 * To String constant
	 */
	private static final String str0 = "{\"epochMillis\" : ";
	private static final String str1 = ", \"instant\" : ";
	private static final String str2 = ", \"zonedDateTime\" : ";
	private static final String str3 = "}";

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(90);
		builder.append(str0);
		builder.append(epochMillis);
		if (instant != null) {
			builder.append(str1);
			builder.append(toText(instant));
		}
		if (zonedDateTime != null) {
			builder.append(str2);
			builder.append(toText(zonedDateTime));
		}
		builder.append(str3);
		return builder.toString();
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
		System.out.println(Timestamp.newWithEpochMillis(47237547328L).updateAndGetDateTimeOf(TimeZone.UTC));
		System.out.println(Timestamp.newWithDateTime(LocalDateTime.now(), TimeZone.CST));

	}

}
