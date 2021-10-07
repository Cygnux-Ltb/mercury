package io.mercury.common.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

public interface TimeZone {

	/**
	 * UTC ZoneOffset
	 */
	ZoneOffset UTC = ZoneOffset.UTC;

	/**
	 * ZoneOffset from runtime
	 */
	ZoneOffset SYS_DEFAULT = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()).getOffset();

	/**
	 * Chinese Standard Time ZoneOffset
	 */
	ZoneOffset CST = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Asia/Shanghai")).getOffset();

	/**
	 * Taiwan Standard Time ZoneOffset
	 */
	ZoneOffset TST = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Asia/Taipei")).getOffset();

	/**
	 * Japan Standard Time ZoneOffset
	 */
	ZoneOffset JST = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Asia/Tokyo")).getOffset();

	/**
	 * North America Chicago Time ZoneOffset
	 */
	ZoneOffset NA_CHICAGO = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("America/Chicago")).getOffset();

	/**
	 * Show System Available ZoneIds
	 */
	public static void showAvailableZoneIds() {
		ZoneId.getAvailableZoneIds().stream().sorted(Comparator.naturalOrder()).forEachOrdered(System.out::println);
	}

}
