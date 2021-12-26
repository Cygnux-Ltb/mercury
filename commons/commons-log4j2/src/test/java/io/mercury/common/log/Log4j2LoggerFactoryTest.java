package io.mercury.common.log;

import java.time.LocalDateTime;

import org.junit.Test;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2Configurator.LogLevel;

public class Log4j2LoggerFactoryTest {

	static {
		System.out.println(System.getProperty("user.home"));
		Log4j2Configurator.setLogFilename("new");
		Log4j2Configurator.setLogLevel(LogLevel.ERROR);
	}

	private static final Logger log = Log4j2LoggerFactory.getLogger(Log4j2LoggerFactory.class);

	@Test
	public void test() {

		Exception e = new NullPointerException("test null point");

		log.error("this is error", e);
		log.warn("this is warn");
		log.info("this is info");
		log.debug("this is debug");

		System.out.println(LocalDateTime.now());

	}

	public void loop() {
		for (;;) {
			log.error("error log info");
			log.warn("warn log info");
			log.info("info log info");
			log.debug("debug log info");
			try {
				Thread.sleep(100);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
