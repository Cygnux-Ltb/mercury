package io.mercury.common.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

/**
 * 
 * @author yellow013
 *
 */
public final class ConfigUtil {

	/**
	 * 
	 * @param config
	 */
	public static void showConfig(@Nonnull Config config) {
		showConfig(config, null);
	}

	/**
	 * 
	 * @param config
	 * @param log
	 */
	public static void showConfig(@Nonnull Config config, @Nullable Logger log) {
		config.entrySet().stream().forEach(entry -> {
			ConfigValue value = entry.getValue();
			if (log != null)
				log.info("Key -> [{}],  ValueType -> [{}],  Value -> [{}]", entry.getKey(), value.valueType(),
						value.unwrapped());
			else
				System.out.println("Key -> [" + entry.getKey() + "],  ValueType -> [" + value.valueType()
						+ "],  Value -> [" + value.unwrapped() + "]");
		});
	}

}
