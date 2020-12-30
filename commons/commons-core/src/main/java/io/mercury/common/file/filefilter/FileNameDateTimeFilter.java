package io.mercury.common.file.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;

import io.mercury.common.datetime.Pattern;
import io.mercury.common.datetime.Pattern.DatePattern;
import io.mercury.common.datetime.TimeZone;
import io.mercury.common.log.CommonLoggerFactory;

public class FileNameDateTimeFilter implements FileFilter {

	private static final Logger logger = CommonLoggerFactory.getLogger(FileNameDateTimeFilter.class);

	private Pattern pattern;
	private ZonedDateTime cutoffDate;
	private ZoneId zone;

	public FileNameDateTimeFilter(LocalDateTime cutoffDate) {
		this(cutoffDate, null, null);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, ZoneId zone) {
		this(cutoffDate, null, zone);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, Pattern pattern) {
		this(cutoffDate, pattern, null);
	}

	public FileNameDateTimeFilter(LocalDateTime cutoffDate, Pattern pattern, ZoneId zone) {
		if (pattern == null) {
			this.pattern = DatePattern.YYYY_MM_DD;
		} else {
			this.pattern = pattern;
		}
		if (zone == null) {
			this.zone = TimeZone.SYS_DEFAULT;
		} else {
			this.zone = zone;
		}
		this.cutoffDate = ZonedDateTime.of(cutoffDate, zone);
	}

	@Override
	public boolean accept(File file) {
		// parse out the date contained within the filename
		ZonedDateTime d = null;
		try {
			d = ZonedDateTime.of(LocalDateTime.parse(file.getName(), pattern.getFormatter()), zone);
		} catch (Exception e) {
			// ignore for matching
			return false;
		}

		logger.trace("Filename '" + file.getName() + "' contained an embedded date of " + d);

		// does the cutoff date occurr before or equal
		if (d.isBefore(cutoffDate) || d.isEqual(cutoffDate)) {
			logger.trace("Filename '" + file.getName() + "' embedded date of " + d + " occurred beforeOrEquals "
					+ d.isBefore(cutoffDate));
			return true;
		}

		// if we get here, then the date wasn't right
		logger.trace("Skipping filename '" + file.getName() + "' since its embedded date of " + d + " occurred after "
				+ cutoffDate);
		return false;
	}

}
