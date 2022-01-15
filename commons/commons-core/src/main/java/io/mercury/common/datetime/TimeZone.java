package io.mercury.common.datetime;

import static java.lang.System.out;
import static java.time.Instant.EPOCH;
import static java.time.ZoneId.getAvailableZoneIds;
import static java.time.ZoneId.of;
import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.ofInstant;
import static java.util.Comparator.naturalOrder;

import java.time.ZoneId;
import java.time.ZoneOffset;

public interface TimeZone {

	/**
	 * UTC ZoneOffset
	 */
	ZoneOffset UTC = ZoneOffset.UTC;

	/**
	 * ZoneOffset from runtime
	 */
	ZoneOffset SYS_DEFAULT = getZoneOffset(systemDefault());

	/**
	 * Chinese Standard Time ZoneOffset
	 */
	ZoneOffset CST = getZoneOffset(of("Asia/Shanghai"));

	/**
	 * Taiwan Standard Time ZoneOffset
	 */
	ZoneOffset TST = getZoneOffset(of("Asia/Taipei"));

	/**
	 * Japan Standard Time ZoneOffset
	 */
	ZoneOffset JST = getZoneOffset(of("Asia/Tokyo"));

	/**
	 * North America Chicago Time ZoneOffset
	 */
	ZoneOffset NA_CHICAGO = getZoneOffset(of("America/Chicago"));

	/**
	 * 
	 * @param zoneId
	 * @return
	 */
	public static ZoneOffset getZoneOffset(ZoneId zoneId) {
		if (zoneId instanceof ZoneOffset)
			return (ZoneOffset) zoneId;
		return ofInstant(EPOCH, zoneId).getOffset();
	}

	/**
	 * Show System Available ZoneIds
	 */
	public static void showAvailableZoneIds() {
		getAvailableZoneIds().stream().sorted(naturalOrder()).forEachOrdered(out::println);
	}

}
