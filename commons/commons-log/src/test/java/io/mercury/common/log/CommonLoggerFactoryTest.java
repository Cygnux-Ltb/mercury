package io.mercury.common.log;

import java.time.LocalDateTime;

import org.junit.Test;
import org.slf4j.Logger;

import io.mercury.common.log.LogConfigurator.LogLevel;

public class CommonLoggerFactoryTest {

	private static final Logger log = CommonLoggerFactory.getLogger(CommonLoggerFactory.class);

	@Test
	public void test() {

		System.out.println(System.getProperty("user.home"));
		LogConfigurator.filename("new");
		LogConfigurator.logLevel(LogLevel.ERROR);

		log.error("this is error");
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
