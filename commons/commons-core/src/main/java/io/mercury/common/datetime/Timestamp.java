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
public final class EpochTimestamp {

	/**
	 * 
	 */
	private long epochMillis;
	/**
	 * 
	 */
	private Instant instant;
	/**
	 * 
	 */
	private ZonedDateTime dateTime;

	private EpochTimestamp() {
		this.epochMillis = System.currentTimeMillis();
	}

	public static EpochTimestamp now() {
		return new EpochTimestamp();
	}

	private void newInstant() {
		this.instant = Instant.ofEpochMilli(epochMillis);
	}

	/**
	 * 
	 * @param zoneId
	 * @return
	 */
	public ZonedDateTime updateDateTimeOf(ZoneId zoneId) {
		if (instant == null)
			newInstant();
		this.dateTime = ZonedDateTime.ofInstant(instant, zoneId);
		return dateTime;
	}

	/**
	 * 
	 * @return
	 */
	public long epochMillis() {
		return epochMillis;
	}

	/**
	 * 
	 * @return
	 */
	public Instant instant() {
		if (instant == null)
			newInstant();
		return instant;
	}

	/**
	 * 
	 * @return
	 */
	public ZonedDateTime dateTime() {
		if (dateTime == null)
			return updateDateTimeOf(TimeZone.SYS_DEFAULT);
		return dateTime;
	}

	private static final String str0 = "{\"epochMillis\" : ";
	private static final String str1 = ", \"instant\" : ";
	private static final String str2 = ", \"dateTime\" : ";
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
		if (dateTime != null) {
			builder.append(str2);
			builder.append(toText(dateTime));
		}
		builder.append(str3);
		return builder.toString();
	}

	public static void main(String[] args) {

		EpochTimestamp timestamp = EpochTimestamp.now();
		timestamp.instant();
		timestamp.dateTime();
		System.out.println(timestamp);

		for (int i = 0; i < 100000; i++) {
			EpochTime.millis();
			EpochTimestamp.now();
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
		EpochTimestamp.now();
		long l1_1 = System.nanoTime();

		long l2_0 = System.nanoTime();
		Instant.now();
		long l2_1 = System.nanoTime();

		long l1 = l1_1 - l1_0;
		long l2 = l2_1 - l2_0;

		System.out.println(l1);
		System.out.println(l2);

		EpochTimestamp now = EpochTimestamp.now();

		System.out.println(now.epochMillis());
		System.out.println(now.instant().getEpochSecond() * 1000000 + now.instant().getNano() / 1000);
		System.out.println(now.instant());
		System.out.println(now.dateTime());

		System.out.println(now);

	}

}
