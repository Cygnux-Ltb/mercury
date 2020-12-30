package io.mercury.common.file.filefilter;

/*
 * #%L
 * ch-commons-util
 * %%
 * Copyright (C) 2012 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.datetime.Pattern;

/**
 * Filters a file based on an embedded date within the filename.
 * 
 * @author joelauer (twitter: @jjlauer or
 *         <a href="http://twitter.com/jjlauer" target=
 *         window>http://twitter.com/jjlauer</a>)
 */
public class FileNameDateTimeFilter implements FileFilter {
	private static final Logger logger = LoggerFactory.getLogger(FileNameDateTimeFilter.class);

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
			this.pattern = Pattern.DatePattern.YYYY_MM_DD;
		} else {
			this.pattern = pattern;
		}
		if (zone == null) {
			this.zone = ZoneOffset.UTC;
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
