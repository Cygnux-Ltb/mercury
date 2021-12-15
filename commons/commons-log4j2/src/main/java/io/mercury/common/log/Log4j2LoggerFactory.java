package io.mercury.common.log;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.log.Log4j2Configurator.LogLevel;

/**
 * 
 * @author yellow013
 *
 */
public final class Log4j2LoggerFactory {

	public static final String DefaultFolder = "default";

	public static final String DefaultFileName = "runtime-" + now().format(ofPattern("yyyyMMdd-HHmmss"));

	public static final LogLevel DefaultLevel = LogLevel.ERROR;

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static final synchronized Logger getLogger(Class<?> clazz) {
		// 配置日志存储目录, 基于${user.home}
		String folder = Log4j2Configurator.getFolder();
		if (folder == null || folder.isEmpty())
			Log4j2Configurator.setLogFolder(DefaultFolder);
		// 配置日志文件名
		String filename = Log4j2Configurator.getFilename();
		if (filename == null || filename.isEmpty())
			Log4j2Configurator.setLogFilename(DefaultFileName);
		// 配置日志級別
		String level = Log4j2Configurator.getLogLevel();
		if (level == null || level.isEmpty())
			Log4j2Configurator.setLogLevel(DefaultLevel);
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {
		System.out.println(DefaultFileName);
	}

}
