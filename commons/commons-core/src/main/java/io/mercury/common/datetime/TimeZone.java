package io.mercury.common.datetime;

import static java.time.ZonedDateTime.ofInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

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
	ZoneOffset SYS_DEFAULT_OFFSET = ofInstant(Instant.EPOCH, SYS_DEFAULT).getOffset();

	/**
	 * Chinese Standard Time ZoneId
	 */
	ZoneId CST = ZoneId.of("Asia/Shanghai");

	/**
	 * Chinese Standard Time ZoneOffset
	 */
	ZoneOffset CST_OFFSET = ofInstant(Instant.EPOCH, CST).getOffset();

	/**
	 * Japan Standard Time ZoneId
	 */
	ZoneId JST = ZoneId.of("Asia/Tokyo");

	/**
	 * Japan Standard Time ZoneOffset
	 */
	ZoneOffset JST_OFFSET = ofInstant(Instant.EPOCH, JST).getOffset();

	/**
	 * America Chicago Time ZoneId
	 */
	ZoneId AMERICA_CHICAGO = ZoneId.of("America/Chicago");

	/**
	 * America Chicago Time ZoneOffset
	 */
	ZoneOffset AMERICA_CHICAGO_OFFSET = ofInstant(Instant.EPOCH, AMERICA_CHICAGO).getOffset();

}
