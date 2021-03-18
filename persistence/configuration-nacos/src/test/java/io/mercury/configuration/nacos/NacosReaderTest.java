package io.mercury.configuration.nacos;

import java.util.Properties;

import org.junit.Test;

import io.mercury.common.log.CommonLogConfigurator;
import io.mercury.common.log.CommonLogConfigurator.LogLevel;
import io.mercury.common.util.PropertiesUtil;

public class NacosReaderTest {

	static {
		CommonLogConfigurator.setLogLevel(LogLevel.INFO);
	}

	@Test
	public void test() {
		Properties properties = NacosReader.getProperties("http://127.0.0.1:8848", "test", "test");
		PropertiesUtil.showProperties(properties);
	}

}
