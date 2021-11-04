package io.mercury.configuration.nacos;

import java.util.Properties;

import org.junit.Test;

import io.mercury.common.log.LogConfigurator;
import io.mercury.common.log.LogConfigurator.LogLevel;
import io.mercury.common.util.PropertiesUtil;

public class NacosReaderTest {

	static {
		LogConfigurator.setLogLevel(LogLevel.INFO);
	}

	@Test
	public void test() {
		Properties properties = NacosReader.getProperties("http://101.132.32.183:8848", "own", "simnow");
		PropertiesUtil.showProperties(properties);
	}

}
