package io.mercury.transport.core.configurator;

import io.mercury.common.config.Configurator;

public interface TransportConfigurator extends Configurator {

	String host();

	int port();

	String connectionInfo();

}
