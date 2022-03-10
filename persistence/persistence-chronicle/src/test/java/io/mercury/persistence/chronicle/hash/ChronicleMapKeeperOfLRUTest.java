package io.mercury.persistence.chronicle.hash;

import io.mercury.common.datetime.EpochTime;
import io.mercury.common.log.Log4j2Configurator;
import io.mercury.common.log.Log4j2Configurator.LogLevel;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.thread.SleepSupport;
import net.openhft.chronicle.map.ChronicleMap;

import java.io.IOException;
import java.time.Duration;

public class ChronicleMapKeeperOfLRUTest {

    static {
        Log4j2Configurator.setLogLevel(LogLevel.INFO);
    }

    public static void main(String[] args) {

        ChronicleMapKeeperCfg.Builder<Long, String> builder = ChronicleMapKeeperCfg
                .newBuilder(Long.class, String.class, SysProperties.USER_HOME, "test")
                .averageValue(Long.toString(Long.MIN_VALUE)).entries(56636);

        try (ChronicleMapKeeperOfLRU<Long, String> mapKeeper = new ChronicleMapKeeperOfLRU<>(builder.build(),
                Duration.ofMinutes(3))) {
            long l = 0;
            long fileCycle = 60 * 1000;
            do {
                long millis = EpochTime.getEpochMillis();
                long filename = millis / fileCycle;
                ChronicleMap<Long, String> acquire = mapKeeper.acquire(Long.toString(filename));
                acquire.put(++l, Long.toString(l));
                SleepSupport.sleep(1000);
            } while (l != 300);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
