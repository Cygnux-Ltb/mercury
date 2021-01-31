package io.mercury.common.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.log.LogConfigurator.LogLevel;

public final class CommonLoggerFactory {

	public static final String DefaultFolder = "default";

	public static final String DefaultFileName = "runtime-"
			+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

	public static final LogLevel DefaultLevel = LogLevel.ERROR;

	public static final synchronized Logger getLogger(Class<?> clazz) {
		// 配置日志存储目录, 基于${user.home}
		String folder = LogConfigurator.getFolder();
		if (folder == null || folder.isEmpty())
			LogConfigurator.setFolder(DefaultFolder);
		// 配置日志文件名
		String filename = LogConfigurator.getFilename();
		if (filename == null || filename.isEmpty())
			LogConfigurator.setFilename(DefaultFileName);
		// 配置日志級別
		String level = LogConfigurator.getLogLevel();
		if (level == null || level.isEmpty())
			LogConfigurator.setLogLevel(DefaultLevel);
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {
		System.out.println(DefaultFileName);
	}

}
