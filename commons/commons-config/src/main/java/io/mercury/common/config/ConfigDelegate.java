package io.mercury.common.config;

import javax.annotation.Nonnull;

import com.typesafe.config.Config;

import io.mercury.common.functional.Functions;
import io.mercury.common.lang.Assertor;

public final class ConfigDelegate<O extends ConfigOption> {

	private final Config config;

	public ConfigDelegate(@Nonnull Config config) {
		Assertor.nonNull(config, "config");
		this.config = config;
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	public boolean getBoolean(@Nonnull O option) {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getBoolean(option.getConfigName()),
				"Missing [" + option.getConfigName() + "] config option");
	}

	/**
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public boolean getBoolean(@Nonnull O option, boolean defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getBoolean(option.getConfigName()), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	public int getInt(@Nonnull O option) {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getInt(option.getConfigName()), "Missing [" + option.getConfigName() + "] config option");
	}

	/**
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public int getInt(@Nonnull O option, int defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getInt(option.getConfigName()), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	public long getLong(@Nonnull O option) {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getLong(option.getConfigName()), "Missing [" + option.getConfigName() + "] config option");
	}

	/**
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public long getLong(@Nonnull O option, long defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getLong(option.getConfigName()), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	public double getDouble(@Nonnull O option) {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getDouble(option.getConfigName()),
				"Missing [" + option.getConfigName() + "] config option");
	}

	/**
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(@Nonnull O option, double defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getDouble(option.getConfigName()), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 */
	public String getString(@Nonnull O option) {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getString(option.getConfigName()),
				"Missing [" + option.getConfigName() + "] config option");
	}

	/**
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public String getString(@Nonnull O option, @Nonnull String defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getString(option.getConfigName()), defaultVal);
	}

}
