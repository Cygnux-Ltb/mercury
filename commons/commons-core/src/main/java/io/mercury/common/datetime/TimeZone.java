package io.mercury.common.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public interface TimeZone {

	/**
	 * UTC ZoneOffset
	 */
	ZoneOffset UTC = ZoneOffset.UTC;

	/**
	 * ZoneId from runtime
	 */
	ZoneId SYS_DEFAULT = ZoneId.systemDefault();

	/**
	 * ZoneOffset from runtime
	 */
	ZoneOffset SYS_DEFAULT_OFFSET = ZonedDateTime.ofInstant(Instant.EPOCH, SYS_DEFAULT).getOffset();

	/**
	 * Chinese Standard Time ZoneId
	 */
	ZoneId CST = ZoneId.of("Asia/Shanghai");

	/**
	 * Chinese Standard Time ZoneOffset
	 */
	ZoneOffset CST_OFFSET = ZonedDateTime.ofInstant(Instant.EPOCH, CST).getOffset();

	/**
	 * Taiwan Standard Time ZoneId
	 */
	ZoneId TST = ZoneId.of("Asia/Taipei");

	/**
	 * Taiwan Standard Time ZoneOffset
	 */
	ZoneOffset TST_OFFSET = ZonedDateTime.ofInstant(Instant.EPOCH, TST).getOffset();

	/**
	 * Japan Standard Time ZoneId
	 */
	ZoneId JST = ZoneId.of("Asia/Tokyo");

	/**
	 * Japan Standard Time ZoneOffset
	 */
	ZoneOffset JST_OFFSET = ZonedDateTime.ofInstant(Instant.EPOCH, JST).getOffset();

	/**
	 * North America Chicago Time ZoneId
	 */
	ZoneId NA_CHICAGO = ZoneId.of("America/Chicago");

	/**
	 * North America Chicago Time ZoneOffset
	 */
	ZoneOffset NA_CHICAGO_OFFSET = ZonedDateTime.ofInstant(Instant.EPOCH, NA_CHICAGO).getOffset();

}
