package io.mercury.common.cfg;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.System.out;

/**
 * @author yellow013
 */
public final class ConfigUtil {

    /**
     * @param config Config
     */
    public static void showConfig(@Nonnull Config config) {
        showConfig(config, null);
    }

    /**
     * @param config Config
     * @param log    Logger
     */
    public static void showConfig(@Nonnull Config config, @Nullable Logger log) {
        config.entrySet().forEach(entry -> {
            ConfigValue value = entry.getValue();
            if (log != null)
                log.info("Key -> [{}],  ValueType -> [{}],  Value -> [{}]",
                        entry.getKey(), value.valueType(), value.unwrapped());
            else
                out.println("Key -> [" + entry.getKey() + "],  ValueType -> [" + value.valueType()
                        + "],  Value -> [" + value.unwrapped() + "]");
        });
    }

}
