package io.mercury.common.log;

public class LoggerSetter {

//	private static volatile boolean logFolderSetted;
	private static volatile boolean logFilenameSetted;
	private static volatile boolean logLevelSetted;

//	public static boolean logFolderSetted() {
//		return logFolderSetted;
//	}

	public static boolean logFilenameSetted() {
		return logFilenameSetted;
	}

	public static boolean logLevelSetted() {
		return logLevelSetted;
	}

//	public static void logFolder(String logFolder) {
//		System.setProperty(LoggerConst.LOG4J2_FOLDER, logFolder);
//		logFolderSetted = true;
//	}

	public static void logFileName(String logFileName) {
		System.setProperty(LoggerConst.LOG4J2_FILENAME, logFileName);
		logFilenameSetted = true;
	}

	public static void logLevel(LogLevel logLevel) {
		System.setProperty(LoggerConst.LOG4J2_LEVEL, logLevel.name());
		logLevelSetted = true;
	}

}
