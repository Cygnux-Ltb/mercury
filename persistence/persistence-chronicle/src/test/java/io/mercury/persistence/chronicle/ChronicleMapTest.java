package io.mercury.persistence.chronicle;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import io.mercury.common.collections.Capacity;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.thread.ThreadTool;
import io.mercury.persistence.chronicle.hash.ChronicleMapConfigurator;
import io.mercury.persistence.chronicle.hash.ChronicleMapKeeperOfDate;
import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapTest {

	@Ignore
	@Test
	public void test0() {

		ChronicleMapConfigurator<String, byte[]> options = ChronicleMapConfigurator
				.builder(String.class, byte[].class, SysProperties.USER_HOME, "test")
				.entriesOfPow2(Capacity.L16_SIZE_65536).averageKey(new String(new byte[32])).averageValue(new byte[128])
				.build();

		try (ChronicleMapKeeperOfDate<String, byte[]> mapKeeper = new ChronicleMapKeeperOfDate<>(options)) {
			ChronicleMap<String, byte[]> acquire = mapKeeper.acquire("2019.10.11");
			while (true) {
				System.out.println(acquire.size());
				ThreadTool.sleep(2000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
