package io.mercury.common.config;

import static io.mercury.common.functional.Functions.getOrDefault;
import static io.mercury.common.functional.Functions.getOrThrows;
import static io.mercury.common.lang.Asserter.nonNull;
import static io.mercury.common.util.StringSupport.nonEmpty;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

public final class ConfigWrapper<O extends ConfigOption> {

	private final Config config;

	private final String module;

	public ConfigWrapper(@Nonnull Config config) {
		this("", config);
	}

	public ConfigWrapper(@Nullable String module, @Nonnull Config config) {
		nonNull(config, "config");
		this.config = config;
		this.module = nonEmpty(module) ? module.endsWith(".") ? module : module + "." : "";
	}

	/**
	 * 是否存在此项配置, 并且配置不为空
	 * 
	 * @param option
	 * @return
	 */
	public boolean hasOption(@Nonnull O option) {
		return config.hasPath(option.getConfigName(module));
	}

	/**
	 * 是否存在此项配置, 或此项配置为空
	 * 
	 * @param option
	 * @return
	 */
	public boolean hasOptionOrNull(@Nonnull O option) {
		return config.hasPathOrNull(option.getConfigName(module));
	}

	/**
	 * 获取[boolean]配置值, 如果未配置, 默认值[false]
	 * 
	 * @param option
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
		return getOrDefault(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getBoolean(option.getConfigName(module)), defaultVal);
	}

	/**
	 * 获取[int]配置值, 如果未配置, 默认值[0]
	 * 
	 * @param option
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
		return getOrDefault(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getInt(option.getConfigName(module)), defaultVal);
	}

	/**
	 * 获取[long]配置值, 如果未配置, 默认值[0L]
	 * 
	 * @param option
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
		return getOrDefault(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getLong(option.getConfigName(module)), defaultVal);
	}

	/**
	 * 获取[double]配置值, 如果未配置, 默认值[0.0D]
	 * 
	 * @param option
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
		return getOrDefault(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getDouble(option.getConfigName(module)), defaultVal);
	}

	/**
	 * 获取[String]配置值, 如果未配置, 默认值[""]
	 * 
	 * @param option
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
		return getOrDefault(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getString(option.getConfigName(module)), defaultVal);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public boolean getBooleanOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return getOrThrows(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getBoolean(option.getConfigName(module)),
				new ConfigException.Missing(option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public int getIntOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return getOrThrows(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getInt(option.getConfigName(module)),
				new ConfigException.Missing(option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public int getIntOrThrows(@Nonnull O option, IntPredicate verify)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getIntOrThrows(option, verify,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @param ex
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public int getIntOrThrows(@Nonnull O option, IntPredicate verify, Exception ex)
			throws ConfigException.Missing, ConfigException.BadValue {
		int value = getIntOrThrows(option);
		if (verify.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(module), "value == " + value, ex);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public long getLongOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return getOrThrows(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getLong(option.getConfigName(module)),
				new ConfigException.Missing(option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public long getLongOrThrows(@Nonnull O option, LongPredicate verify)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getLongOrThrows(option, verify,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @param ex
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public long getLongOrThrows(@Nonnull O option, LongPredicate verify, Exception ex)
			throws ConfigException.Missing, ConfigException.BadValue {
		long value = getLongOrThrows(option);
		if (verify.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(module), "value == " + value, ex);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public double getDoubleOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return getOrThrows(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getDouble(option.getConfigName(module)),
				new ConfigException.Missing(option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public double getDoubleOrThrows(@Nonnull O option, DoublePredicate verify)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getDoubleOrThrows(option, verify,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @param ex
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public double getDoubleOrThrows(@Nonnull O option, DoublePredicate verify, Exception ex)
			throws ConfigException.Missing, ConfigException.BadValue {
		double value = getDoubleOrThrows(option);
		if (verify.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(module), "value == " + value, ex);
	}

	/**
	 * 
	 * @param option
	 * @return
	 * @throws ConfigException.Missing
	 */
	public String getStringOrThrows(@Nonnull O option) throws ConfigException.Missing {
		return getOrThrows(() -> config.hasPath(option.getConfigName(module)),
				() -> config.getString(option.getConfigName(module)),
				new ConfigException.Missing(option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public String getStringOrThrows(@Nonnull O option, Predicate<String> verify)
			throws ConfigException.Missing, ConfigException.BadValue {
		return getStringOrThrows(option, verify,
				new IllegalArgumentException("Illegal argument -> " + option.getConfigName(module)));
	}

	/**
	 * 
	 * @param option
	 * @param verify
	 * @param ex
	 * @return
	 * @throws ConfigException.Missing
	 * @throws ConfigException.BadValue
	 */
	public String getStringOrThrows(@Nonnull O option, Predicate<String> verify, Exception ex)
			throws ConfigException.Missing, ConfigException.BadValue {
		String value = getStringOrThrows(option);
		if (verify.test(value))
			return value;
		else
			throw new ConfigException.BadValue(option.getConfigName(module), "value == " + value, ex);
	}

}
