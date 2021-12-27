package io.mercury.common.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigUtilTest {

	@Test
	public void test() {
		File f = new File("");
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(f.getAbsolutePath());
		System.out.println(System.getProperty("user.dir"));
		URL resource = this.getClass().getResource("/");
		System.out.println(resource);
		File file = new File(resource.getFile());
		Config config = ConfigFactory.parseFile(file);

		ConfigUtil.showConfig(config);

	}

}
