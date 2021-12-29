package io.mercury.common.config;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import io.mercury.common.functional.Functions;
import io.mercury.common.lang.Assertor;

public final class ConfigDelegate<O extends ConfigOption> {

	private final Config config;

	public ConfigDelegate(@Nonnull Config config) {
		Assertor.nonNull(config, "config");
		this.config = config;
	}

	/**
	 * 是否存在此项配置, 并且配置不为空
	 * 
	 * @param option
	 * @return
	 */
	public boolean hasOption(@Nonnull O option) {
		return config.hasPath(option.getConfigName());
	}

	/**
	 * 是否存在此项配置, 或此项配置为空
	 * 
	 * @param option
	 * @return
	 */
	public boolean hasOptionOrNull(@Nonnull O option) {
		return config.hasPathOrNull(option.getConfigName());
	}

	/**
	 * 获取[boolean]配置值, 如果未配置, 默认值[false]
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public boolean getBoolean(@Nonnull O option) {
		return getBoolean(option, false);
	}

	/**
	 * 获取[boolean]配置值, 如果未配置, 使用指定默认值
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
	 * 获取[int]配置值, 如果未配置, 默认值[0]
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public int getInt(@Nonnull O option) {
		return getInt(option, 0);
	}

	/**
	 * 获取[int]配置值, 如果未配置, 使用指定默认值
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
	 * 获取[long]配置值, 如果未配置, 默认值[0L]
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public long getLong(@Nonnull O option) {
		return getLong(option, 0L);
	}

	/**
	 * 获取[long]配置值, 如果未配置, 使用指定默认值
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
	 * 获取[double]配置值, 如果未配置, 默认值[0.0D]
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public double getDouble(@Nonnull O option) {
		return getDouble(option, 0.0D);
	}

	/**
	 * 获取[double]配置值, 如果未配置, 使用指定默认值
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
	 * 获取[String]配置值, 如果未配置, 默认值[""]
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public String getString(@Nonnull O option) {
		return getString(option, "");
	}

	/**
	 * 获取[String]配置值, 如果未配置, 使用指定默认值
	 * 
	 * @param option
	 * @param defaultVal
	 * @return
	 */
	public String getString(@Nonnull O option, @Nonnull String defaultVal) {
		return Functions.getOrDefault(() -> config.hasPath(option.getConfigName()),
				() -> config.getString(option.getConfigName()), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public boolean getBooleanOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getBoolean(option.getConfigName()), new ConfigException.Missing(option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public int getIntOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getInt(option.getConfigName()), new ConfigException.Missing(option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public int getIntOrThrows(@Nonnull O option, IntPredicate predicate)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getIntOrThrows(option, predicate,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @param exception
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public int getIntOrThrows(@Nonnull O option, IntPredicate predicate, Exception exception)
			throws ConfigException.Missing, ConfigException.BadValue {
		var value = getIntOrThrows(option);
		if (predicate.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(), "value == " + value, exception);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public long getLongOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getLong(option.getConfigName()), new ConfigException.Missing(option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public long getLongOrThrows(@Nonnull O option, LongPredicate predicate)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getLongOrThrows(option, predicate,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @param exception
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public long getLongOrThrows(@Nonnull O option, LongPredicate predicate, Exception exception)
			throws ConfigException.Missing, ConfigException.BadValue {
		var value = getLongOrThrows(option);
		if (predicate.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(), "value == " + value, exception);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public double getDoubleOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getDouble(option.getConfigName()), new ConfigException.Missing(option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public double getDoubleOrThrows(@Nonnull O option, DoublePredicate predicate)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getDoubleOrThrows(option, predicate,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @param exception
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public double getDoubleOrThrows(@Nonnull O option, DoublePredicate predicate, Exception exception)
			throws ConfigException.Missing, ConfigException.BadValue {
		var value = getDoubleOrThrows(option);
		if (predicate.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(), "value == " + value, exception);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public String getStringOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return Functions.getOrThrows(() -> config.hasPath(option.getConfigName()),
				() -> config.getString(option.getConfigName()), new ConfigException.Missing(option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public String getStringOrThrows(@Nonnull O option, Predicate<String> predicate)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getStringOrThrows(option, predicate,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName()));
	}

	/**
	 * 
	 * @param option
	 * @param predicate
	 * @param exception
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public String getStringOrThrows(@Nonnull O option, Predicate<String> predicate, Exception exception)
			throws ConfigException.Missing, ConfigException.BadValue {
		var value = getStringOrThrows(option);
		if (predicate.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(), "value == " + value, exception);
	}

}
