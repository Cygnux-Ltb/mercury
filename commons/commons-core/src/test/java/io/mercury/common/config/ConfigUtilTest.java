package io.mercury.common.config;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigUtilTest {

	@Test
	public void test() {
		
		URL resource = ConfigUtilTest.class.getClassLoader().getResource("test.properties");
		
		System.out.println(resource.getFile());
		
		Config config = ConfigFactory.parseFile(new File(resource.getFile()));
		
		ConfigUtil.showConfig(config);
		
		int int1 = config.getInt("sys.strategyId");
		
		System.out.println(int1);

	}

}
