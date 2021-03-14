package io.mercury.common.log;

public final class CommonLogConfigurator {

	private static final String Log4j2Folder = "log4j2.folder";

	private static final String Log4j2Filename = "log4j2.filename";

	private static final String Log4j2Level = "log4j2.level";

	public static synchronized void setFolder(String folder) {
		System.setProperty(Log4j2Folder, folder);
	}

	public static synchronized void setFilename(String filename) {
		System.setProperty(Log4j2Filename, filename);
	}

	public static synchronized void setLogLevel(LogLevel level) {
		System.setProperty(Log4j2Level, level.name());
	}

	public static synchronized String getFolder() {
		return System.getProperty(Log4j2Folder);
	}

	public static synchronized String getFilename() {
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
