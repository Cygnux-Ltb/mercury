package io.mercury.configuration.nacos;

import java.util.Properties;

import org.junit.Test;

import io.mercury.common.log.Log4j2Configurator;
import io.mercury.common.log.Log4j2Configurator.LogLevel;
import io.mercury.common.util.PropertiesUtil;

public class NacosReaderTest {

	static {
		Log4j2Configurator.setLogLevel(LogLevel.INFO);
	}

	@Test
	public void test() {
		Properties properties = NacosReader.getProperties("", "", "");
		PropertiesUtil.showProperties(properties);
	}

}
