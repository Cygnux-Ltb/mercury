package io.mercury.transport.zmq;

import io.mercury.common.cfg.ConfigOption;

public enum ZmqConfigOption implements ConfigOption {

	Protocol("zmq.protocol", "zeromq.protocol"),

	Addr("zmq.addr", "zeromq.addr"),

	Port("zmq.port", "zeromq.port"),

	IoThreads("zmq.ioThreads", "zeromq.ioThreads");

	private final String configName;

	private final String otherName;

	ZmqConfigOption(String configName, String otherName) {
		this.configName = configName;
		this.otherName = otherName;
	}

	@Override
	public String getConfigName() {
		return configName;
	}

	@Override
	public String getOtherName() {
		return otherName;
	}

}