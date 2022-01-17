package io.mercury.common.log;

import static io.mercury.common.log.Log4j2Configurator.getFileSizeOfMb;
import static io.mercury.common.log.Log4j2Configurator.getFilename;
import static io.mercury.common.log.Log4j2Configurator.getFolder;
import static io.mercury.common.log.Log4j2Configurator.getLogLevel;
import static io.mercury.common.log.Log4j2Configurator.setFileSizeOfMb;
import static io.mercury.common.log.Log4j2Configurator.setLogFilename;
import static io.mercury.common.log.Log4j2Configurator.setLogFolder;
import static io.mercury.common.log.Log4j2Configurator.setLogLevel;
import static io.mercury.common.log.Log4j2Configurator.LogLevel.ERROR;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 
 * @author yellow013
 *
 */
public final class Log4j2LoggerFactory {

	/**
	 * default log file folder
	 */
	public static final String DefaultFolder = "default";

	/**
	 * default log filename
	 */
	public static final String DefaultFileName = "runtime-" + now().format(ofPattern("yyMMdd-HHmmss"));

	/**
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static final synchronized org.slf4j.Logger getLogger(Object obj) {
		if (obj == null)
			return getLogger("Unnamed-Logger");
		if (obj instanceof String)
			return getLogger((String) obj);
		return getLogger(obj.getClass());
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static final synchronized org.slf4j.Logger getLogger(Class<?> clazz) {
		checkAndSettings();
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static final synchronized org.slf4j.Logger getLogger(String name) {
		checkAndSettings();
		return org.slf4j.LoggerFactory.getLogger(name);
	}

	/**
	 * Internal Logger Settings
	 */
	private static final void checkAndSettings() {
		// 配置日志存储目录, 基于${user.home}
		var folder = getFolder();
		if (folder == null || folder.isEmpty())
			setLogFolder(DefaultFolder);
		// 配置日志文件名
		var filename = getFilename();
		if (filename == null || filename.isEmpty())
			setLogFilename(DefaultFileName);
		// 配置日志級別
		var level = getLogLevel();
		if (level == null || level.isEmpty())
			setLogLevel(ERROR);
		// 配置日志文件大小
		var sizeOfMb = getFileSizeOfMb();
		if (sizeOfMb == null || sizeOfMb.isEmpty())
			setFileSizeOfMb(127);
	}

}
