package io.mercury.persistence.chronicle.hash;

import java.io.IOException;
import java.time.Duration;

import io.mercury.common.datetime.EpochUtil;
import io.mercury.common.log.LogConfigurator;
import io.mercury.common.log.LogConfigurator.LogLevel;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.thread.SleepSupport;
import io.mercury.persistence.chronicle.hash.ChronicleMapConfigurator.Builder;
import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapKeeperOfLRUTest {

	static {
		LogConfigurator.setLogLevel(LogLevel.INFO);
	}

	public static void main(String[] args) {

		Builder<Long, String> builder = ChronicleMapConfigurator
				.newBuilder(Long.class, String.class, SysProperties.USER_HOME, "test")
				.averageValue(Long.toString(Long.MIN_VALUE)).entries(56636);

		try (ChronicleMapKeeperOfLRU<Long, String> mapKeeper = new ChronicleMapKeeperOfLRU<>(builder.build(),
				Duration.ofMinutes(3))) {
			long l = 0;
			long fileCycle = 60 * 1000;
			for (;;) {
				long millis = EpochUtil.getEpochMillis();
				long filename = millis / fileCycle;
				ChronicleMap<Long, String> acquire = mapKeeper.acquire(Long.toString(filename));
				acquire.put(++l, Long.toString(l));
				SleepSupport.sleep(1000);
				if (l == 300)
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
