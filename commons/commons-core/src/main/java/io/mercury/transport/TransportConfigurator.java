package io.mercury.transport;

import io.mercury.common.cfg.Configurator;

public interface TransportConfigurator extends Configurator {

	String getConnectionInfo();

}
