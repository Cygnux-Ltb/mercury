package io.mercury.common.cfg;

public interface ConfigOption {

	String getConfigName();

	default String getConfigName(String module) {
		return module + getConfigName();
	}

	default String getOtherName() {
		return getConfigName();
	}

	default String getOtherName(String module) {
		return module + getOtherName();
	}

}
