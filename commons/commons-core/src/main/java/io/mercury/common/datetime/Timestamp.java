package io.mercury.common.datetime;

import static io.mercury.common.util.StringUtil.toText;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 
 * @author yellow013
 *
 */
public final class Timestamp {

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
	 * @return
	 */
	public static Timestamp newWithEpochMillis(long epochMillis) {
		return new Timestamp(epochMillis);
	}

	/**
	 * 
	 */
	private void newInstantOfEpochMilli() {
		this.instant = Instant.ofEpochMilli(epochMillis);
	}

	/**
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime updateDateTimeOf() {
		if (instant == null)
			newInstantOfEpochMilli();
		this.zonedDateTime = ZonedDateTime.ofInstant(instant, TimeZone.SYS_DEFAULT);
		return zonedDateTime;
	}

	/**
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime updateDateTimeOf(ZoneId zoneId) {
		if (instant == null)
			newInstantOfEpochMilli();
		this.zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
		return zonedDateTime;
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
	public Instant getInstant() {
		if (instant == null)
			newInstantOfEpochMilli();
		return instant;
	}

	/**
	 * 
	 * @return
	 */
	public ZonedDateTime getZonedDateTime() {
		if (zonedDateTime == null)
			return updateDateTimeOf(TimeZone.SYS_DEFAULT);
		return zonedDateTime;
	}

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

	}

}
