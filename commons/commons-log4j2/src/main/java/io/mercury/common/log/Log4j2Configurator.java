package io.mercury.common.log;

/**
 * 
 * @author yellow013
 *
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
	 * @author yellow013
	 *
	 */
	public static enum LogLevel {

		DEBUG, INFO, WARN, ERROR, FATAL;

	}

}
