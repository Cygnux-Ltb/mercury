package io.mercury.common.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;

/**
 * 
 * @author yellow013
 */
public final class Log4j2Configurator {

	/**
	 * 
	 * @param folder
	 */
	public static synchronized void setLogFolder(String folder) {
		System.setProperty("log4j2.folder", folder);
	}

	/**
	 * 
	 * @param filename
	 */
	public static synchronized void setLogFilename(String filename) {
		System.setProperty("log4j2.filename", filename);
	}

	/**
	 * 
	 * @param level
	 */
	public static synchronized void setLogLevel(LogLevel level) {
		System.setProperty("log4j2.level", level.name());
	}

	/**
	 * 
	 * @param sizeOfMb
	 */
	public static synchronized void setFileSizeOfMb(int sizeOfMb) {
		System.setProperty("log4j2.sizeOfMb", Integer.toString(sizeOfMb));
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized String getFolder() {
		return System.getProperty("log4j2.folder");
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized String getFilename() {
		return System.getProperty("log4j2.filename");
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized String getLogLevel() {
		return System.getProperty("log4j2.level");
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized String getFileSizeOfMb() {
		return System.getProperty("log4j2.sizeOfMb");
	}

	/**
	 * 
	 * @author yellow013<br>
	 * 
	 *         <b>Use: org.apache.logging.log4j.Level </b><br>
	 *         <br>
	 *         Levels used for identifying the severity of an event. Levels are
	 *         organized from most specific to least:
	 *         <ul>
	 *         <li>{@link #FATAL} (most specific, little data)</li>
	 *         <li>{@link #ERROR}</li>
	 *         <li>{@link #WARN}</li>
	 *         <li>{@link #INFO}</li>
	 *         <li>{@link #DEBUG}</li>
	 *         </ul>
	 *
	 *         Typically, configuring a level in a filter or on a logger will cause
	 *         logging events of that level and those that are more specific to pass
	 *         through the filter. A special level, {@link #ALL}, is guaranteed to
	 *         capture all levels when used in logging configurations.
	 *
	 */
	public static enum LogLevel implements Comparable<LogLevel> {

		DEBUG(1, org.apache.logging.log4j.Level.DEBUG),

		INFO(2, org.apache.logging.log4j.Level.INFO),

		WARN(3, org.apache.logging.log4j.Level.WARN),

		ERROR(4, org.apache.logging.log4j.Level.ERROR),

		FATAL(5, org.apache.logging.log4j.Level.FATAL),

		;

		private final int priority;
		private final org.apache.logging.log4j.Level level;

		private LogLevel(int priority, org.apache.logging.log4j.Level level) {
			this.priority = priority;
			this.level = level;
		}

		public org.apache.logging.log4j.Level getLevel() {
			return level;
		}

		private Filter getFilter(Result mismatch) {
			return ThresholdFilter.createFilter(level, Result.ACCEPT, mismatch);
		}

		public static final Filter getCompositeFilterWith(LogLevel level) {
			List<Filter> filters = new ArrayList<>();
			for (LogLevel logLevel : values()) {
				if (logLevel.priority < level.priority)
					continue;
				filters.add(logLevel.getFilter(logLevel.priority == level.priority ? Result.DENY : Result.NEUTRAL));
			}
			return CompositeFilter.createFilters(filters.toArray(new Filter[filters.size()]));
		}

	}

	public static void main(String[] args) {

		Filter compositeFilter = LogLevel.getCompositeFilterWith(LogLevel.INFO);

		System.out.println(compositeFilter);

	}

}
