package io.mercury.common.log;

public final class LogConfigurator {

	private static final String Log4j2Folder = "log4j2.folder";

	private static final String Log4j2Filename = "log4j2.filename";

	private static final String Log4j2Level = "log4j2.level";

	public static synchronized void folder(String folder) {
		System.setProperty(Log4j2Folder, folder);
	}

	public static synchronized void filename(String filename) {
		System.setProperty(Log4j2Filename, filename);
	}

	public static synchronized void level(LogLevel level) {
		System.setProperty(Log4j2Level, level.name());
	}

	public static synchronized String folder() {
		return System.getProperty(Log4j2Folder);
	}

	public static synchronized String filename() {
		return System.getProperty(Log4j2Filename);
	}

	public static synchronized String level() {
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
