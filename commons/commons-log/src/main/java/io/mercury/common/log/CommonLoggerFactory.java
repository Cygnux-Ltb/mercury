package io.mercury.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.log.LogConfigurator.LogLevel;

public final class CommonLoggerFactory {

	public static final String DefaultLogFolder = "default";

	public static final String DefaultLogFileName = "runtime";

	public static final LogLevel DefaultLogLevel = LogLevel.ERROR;

	public static Logger getLogger(Class<?> clazz) {
		// 配置日志存储目录, 基于${user.home}
		String logFolder = LogConfigurator.getLogFolder();
		if (logFolder == null || logFolder.isEmpty()) {
			LogConfigurator.logFolder(DefaultLogFolder);
		}
		// 配置日志文件名
		String logFilename = LogConfigurator.getLogFileName();
		if (logFilename == null || logFilename.isEmpty()) {
			LogConfigurator.logFileName(DefaultLogFileName);
		}
		// 配置日志級別
		String logLevel = LogConfigurator.getLogLevel();
		if (logLevel == null || logLevel.isEmpty()) {
			LogConfigurator.logLevel(DefaultLogLevel);
		}
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {

	}

}
