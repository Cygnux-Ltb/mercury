package io.mercury.common.config;

public interface ConfigOption {

	String getConfigName();

	default String getConfigName(String module) {
		return module + getConfigName();
	}

}
