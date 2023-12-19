package io.mercury.transport.rmq.cfg;

import io.mercury.common.cfg.ConfigOption;

public enum RmqCfgOption implements ConfigOption {

    Host("rmq.host", "rabbitmq.host"),

    Port("rmq.port", "rabbitmq.port"),

    Username("rmq.username", "rabbitmq.username"),

    Password("rmq.password", "rabbitmq.password"),

    VirtualHost("rmq.virtualHost", "rabbitmq.virtualHost");

    private final String configName;

    private final String otherConfigName;

    RmqCfgOption(String configName, String otherConfigName) {
        this.configName = configName;
        this.otherConfigName = otherConfigName;
    }

    @Override
    public String getConfigName() {
        return configName;
    }

    @Override
    public String getOtherName() {
        return otherConfigName;
    }

}