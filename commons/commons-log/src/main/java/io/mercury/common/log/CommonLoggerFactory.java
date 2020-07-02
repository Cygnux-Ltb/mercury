package io.mercury.common.log;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommonLoggerFactory {

//	private static final String DefaultFolder = "default";

	private static final String DefaultFileName = "runtime";

	public static Logger getLogger(Class<?> clazz) {
//		if (!LoggerSetter.logFolderSetted()) {
//			String logFolder = System.getProperty(LoggerConst.LOG4J2_FOLDER);
//			if (StringUtil.isNullOrEmpty(logFolder))
//				LoggerSetter.logFolder(DefaultFolder);
//		}
		if (!LoggerSetter.logFilenameSetted()) {
			String logFilename = System.getProperty(LoggerConst.LOG4J2_FILENAME);
			if (logFilename == null || logFilename.isEmpty())
				LoggerSetter.logFileName(DefaultFileName);
		}
		if (!LoggerSetter.logLevelSetted()) {
			String logLevel = System.getProperty(LoggerConst.LOG4J2_LEVEL);
			if (logLevel == null || logLevel.isEmpty())
				LoggerSetter.logLevel(LogLevel.INFO);
		}
		return LoggerFactory.getLogger(clazz);
	}

	public static void main(String[] args) {

		System.out.println(System.getProperty("user.home"));
		LoggerSetter.logFileName("new");

		LoggerSetter.logLevel(LogLevel.ERROR);
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
