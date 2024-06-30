package io.mercury.transport;

import io.mercury.common.config.Configurator;

public interface TransportConfigurator extends Configurator {

	String getConnectionInfo();

}
