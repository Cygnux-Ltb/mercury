package io.mercury.common.config;

public interface ConfigOption {

	String getConfigName();

	default String getConfigName(String module) {
		return module + getConfigName();
	}

	default String getOtherConfigName() {
		return getConfigName();
	}

	default String getOtherConfigName(String module) {
		return module + getOtherConfigName();
	}

}
