package io.mercury.common.log;

public final class LogConfigurator {

	private static final String Log4j2Folder = "log4j2.folder";

	private static final String Log4j2Filename = "log4j2.filename";

	private static final String Log4j2Level = "log4j2.level";

	public static synchronized void logFolder(String logFolder) {
		System.setProperty(Log4j2Folder, logFolder);
	}

	public static synchronized void logFileName(String logFileName) {
		System.setProperty(Log4j2Filename, logFileName);
	}

	public static synchronized void logLevel(LogLevel logLevel) {
		System.setProperty(Log4j2Level, logLevel.name());
	}

	public static synchronized String getLogFolder() {
		return System.getProperty(Log4j2Folder);
	}

	public static synchronized String getLogFileName() {
		return System.getProperty(Log4j2Filename);
	}

	public static synchronized String getLogLevel() {
		return System.getProperty(Log4j2Level);
	}

	public static enum LogLevel {

		DEBUG,

		INFO,

		WARN,

		ERROR,

		FATAL,

		;

	}

}
