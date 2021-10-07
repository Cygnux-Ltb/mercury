package io.mercury.common.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Test;

public class TimeZonesTest {

	@Test
	public void test() {

		ZoneOffset standardOffset = ZoneId.systemDefault().getRules().getStandardOffset(Instant.EPOCH);

		System.out.println(standardOffset);

		System.out.println(TimeZone.SYS_DEFAULT);
		System.out.println(TimeZone.CST);
		System.out.println(TimeZone.JST);

		ZoneOffset ofHours = ZoneOffset.ofHours(8);

		System.out.println(TimeZone.SYS_DEFAULT.equals(ofHours));

		System.out.println(TimeZone.SYS_DEFAULT.equals(standardOffset));

		ZoneId.getAvailableZoneIds().forEach(System.out::println);

	}

}
