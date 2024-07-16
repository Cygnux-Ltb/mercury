package io.mercury.transport.rmq.cfg;

import io.mercury.common.config.ConfigOption;
import lombok.Getter;

public enum RmqCfgOption implements ConfigOption {

    Host("rmq.host", "rabbitmq.host"),

    Port("rmq.port", "rabbitmq.port"),

    Username("rmq.username", "rabbitmq.username"),

    Password("rmq.password", "rabbitmq.password"),

    VirtualHost("rmq.virtualHost", "rabbitmq.virtualHost");

    @Getter
    private final String configName;

    @Getter
    private final String otherName;

    RmqCfgOption(String configName, String otherName) {
        this.configName = configName;
        this.otherName = otherName;
    }

}