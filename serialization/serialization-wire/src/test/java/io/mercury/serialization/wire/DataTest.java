package io.mercury.serialization.wire;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.mercury.common.log4j2.Log4j2Configurator;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.YamlWire;

public class DataTest {
	
	static {
		Log4j2Configurator.useInfoLogLevel();
	}

	@Test
	public void test0() {

		Data data = new Data("TEST", 1024L, TimeUnit.HOURS, 12.5D);
		System.out.println(data);

		WireOut wireOut = new YamlWire(Bytes.elasticHeapByteBuffer());
		data.writeMarshallable(wireOut);
		System.out.println(wireOut);

	}

}
