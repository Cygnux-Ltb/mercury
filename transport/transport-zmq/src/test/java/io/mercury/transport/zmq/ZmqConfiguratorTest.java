package io.mercury.transport.zmq;

import java.net.URL;

import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.mercury.transport.zmq.ZmqConfigurator.ZmqProtocol;

public class ZmqConfiguratorTest {

	@Test
	public void test() {

		System.out.println(ZmqProtocol.INPROC.fixAddr("test"));
		System.out.println(ZmqProtocol.TCP.fixAddr("*:9889"));
		System.out.println(ZmqProtocol.TCP.fixAddr("192.168.1.1:7887"));

		URL url = this.getClass().getResource("zmq-config.conf");
		System.out.println(url.getFile());

		Config config = ConfigFactory.parseURL(url);

		ZmqConfigurator configurator = ZmqConfigurator.withConfig(config);

		System.out.println(configurator);

	}

}
