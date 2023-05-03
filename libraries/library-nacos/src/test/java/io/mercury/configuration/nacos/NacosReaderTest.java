package io.mercury.configuration.nacos;

import java.util.Properties;

import org.junit.Test;

import io.mercury.common.util.PropertiesUtil;
import io.mercury.common.log4j2.Log4j2Configurator;
import io.mercury.common.log4j2.Log4j2Configurator.LogLevel;

public class NacosReaderTest {

	static {
		Log4j2Configurator.setLogLevel(LogLevel.INFO);
	}

	@Test
	public void test() {
		Properties properties = NacosReader.getProperties("", "", "");
		PropertiesUtil.show(properties);
	}

}
