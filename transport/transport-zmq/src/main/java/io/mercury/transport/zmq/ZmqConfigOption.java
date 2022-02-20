package io.mercury.transport.zmq;

import io.mercury.common.config.ConfigOption;

public enum ZmqConfigOption implements ConfigOption {

	Protocol("zmq.protocol", "zeromq.protocol"),

	Addr("zmq.addr", "zeromq.addr"),

	Port("zmq.port", "zeromq.port"),

	IoThreads("zmq.ioThreads", "zeromq.ioThreads");

	private final String configName;

	private final String otherConfigName;

	ZmqConfigOption(String configName, String otherConfigName) {
		this.configName = configName;
		this.otherConfigName = otherConfigName;
	}

	@Override
	public String getConfigName() {
		return configName;
	}

	public String getOtherConfigName() {
		return otherConfigName;
	}
}