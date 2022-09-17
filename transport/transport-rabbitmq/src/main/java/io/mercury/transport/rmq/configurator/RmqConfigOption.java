package io.mercury.transport.rmq.configurator;

import io.mercury.common.config.ConfigOption;

public enum RmqConfigOption implements ConfigOption {

    Host("rmq.host", "rabbitmq.host"),

    Port("rmq.port", "rabbitmq.port"),

    Username("rmq.username", "rabbitmq.username"),

    Password("rmq.password", "rabbitmq.password"),

    VirtualHost("rmq.virtualHost", "rabbitmq.virtualHost");

    private final String configName;

    private final String otherConfigName;

    RmqConfigOption(String configName, String otherConfigName) {
        this.configName = configName;
        this.otherConfigName = otherConfigName;
    }

    @Override
    public String getConfigName() {
        return configName;
    }

    @Override
    public String getOtherConfigName() {
        return otherConfigName;
    }

}