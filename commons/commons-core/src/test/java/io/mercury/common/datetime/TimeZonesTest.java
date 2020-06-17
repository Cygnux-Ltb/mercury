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

		System.out.println(TimeZone.SYS_DEFAULT_OFFSET);
		System.out.println(TimeZone.CST_OFFSET);
		System.out.println(TimeZone.JST_OFFSET);

		ZoneOffset ofHours = ZoneOffset.ofHours(8);

		System.out.println(TimeZone.SYS_DEFAULT_OFFSET.equals(ofHours));

		System.out.println(TimeZone.SYS_DEFAULT_OFFSET.equals(standardOffset));

		ZoneId.getAvailableZoneIds().forEach(System.out::println);

	}

}
