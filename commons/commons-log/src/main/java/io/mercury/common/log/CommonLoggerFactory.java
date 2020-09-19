package io.mercury.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.log.LogConfigurator.LogLevel;

public final class CommonLoggerFactory {

	public static final String DefaultFolder = "default";

	public static final String DefaultFileName = "runtime";

	public static final LogLevel DefaultLevel = LogLevel.ERROR;

	public static final Logger getLogger(Class<?> clazz) {
		// 配置日志存储目录, 基于${user.home}
		String folder = LogConfigurator.folder();
		if (folder == null || folder.isEmpty())
			LogConfigurator.folder(DefaultFolder);
		// 配置日志文件名
		String filename = LogConfigurator.filename();
		if (filename == null || filename.isEmpty())
			LogConfigurator.filename(DefaultFileName);
		// 配置日志級別
		String level = LogConfigurator.level();
		if (level == null || level.isEmpty())
			LogConfigurator.level(DefaultLevel);
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {

	}

}
