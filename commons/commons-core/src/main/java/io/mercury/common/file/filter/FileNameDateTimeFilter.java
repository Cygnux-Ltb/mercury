package io.mercury.common.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.datetime.pattern.DatePattern;
import io.mercury.common.datetime.pattern.TemporalPattern;
import io.mercury.common.log.Log4j2LoggerFactory;

public class FileNameDateTimeFilter implements FileFilter {

	private static final Logger log = Log4j2LoggerFactory.getLogger(FileNameDateTimeFilter.class);

	private TemporalPattern pattern;
	private ZonedDateTime cutoffDate;
	private ZoneId zoneId;

	public FileNameDateTimeFilter(LocalDateTime cutoffDate) {
		this(cutoffDate, null, null);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, ZoneId zoneId) {
		this(cutoffDate, null, zoneId);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, TemporalPattern pattern) {
		this(cutoffDate, pattern, null);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, TemporalPattern pattern, ZoneId zoneId) {
		if (pattern == null) {
			this.pattern = DatePattern.YYYY_MM_DD;
		} else {
			this.pattern = pattern;
		}
		if (zoneId == null) {
			this.zoneId = TimeZone.SYS_DEFAULT;
		} else {
			this.zoneId = zoneId;
		}
		this.cutoffDate = ZonedDateTime.of(cutoffDate, zoneId);
	}

	@Override
	public boolean accept(File file) {
		// parse out the date contained within the filename
		ZonedDateTime datetime = null;
		try {
			datetime = ZonedDateTime.of(LocalDateTime.parse(file.getName(), pattern.getFormatter()), zoneId);
		} catch (Exception e) {
			// ignore for matching
			return false;
		}
		log.debug("Filename '" + file.getName() + "' contained an embedded date of " + datetime);

		// does the cutoff date occurr before or equal
		if (datetime.isBefore(cutoffDate) || datetime.isEqual(cutoffDate)) {
			log.debug("Filename '" + file.getName() + "' embedded date of " + datetime + " occurred beforeOrEquals "
					+ datetime.isBefore(cutoffDate));
			return true;
		}

		// if we get here, then the date wasn't right
		log.debug("Skipping filename '" + file.getName() + "' since its embedded date of " + datetime
				+ " occurred after " + cutoffDate);
		return false;
	}

}
