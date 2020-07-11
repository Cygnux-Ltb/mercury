package io.mercury.common.log;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mercury.common.log.LogConfigurator.LogLevel;

public final class CommonLoggerFactory {

	public static final String DefaultLogFolder = "default";

	public static final String DefaultLogFileName = "runtime";

	public static final LogLevel DefaultLogLevel = LogLevel.ERROR;

	public static Logger getLogger(Class<?> clazz) {
		String logFolder = LogConfigurator.getLogFolder();
		if (logFolder == null || logFolder.isEmpty())
			LogConfigurator.logFolder(DefaultLogFolder);
		String logFilename = LogConfigurator.getLogFileName();
		if (logFilename == null || logFilename.isEmpty())
			LogConfigurator.logFileName(DefaultLogFileName);
		String logLevel = LogConfigurator.getLogLevel();
		if (logLevel == null || logLevel.isEmpty())
			LogConfigurator.logLevel(DefaultLogLevel);
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {

		System.out.println(System.getProperty("user.home"));
		LogConfigurator.logFileName("new");
		LogConfigurator.logLevel(LogLevel.ERROR);
		Logger log = getLogger(CommonLoggerFactory.class);

		log.error("this is error");
		log.warn("this is warn");
		log.info("this is info");
		log.debug("this is debug");

		System.out.println(LocalDateTime.now());

		for (;;) {
			log.error("this is error");
			log.warn("this is warn");
			log.info("this is info");
			log.debug("this is debug");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
