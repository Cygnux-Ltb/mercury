package io.mercury.transport.rmq.config;

import io.mercury.common.config.ConfigOption;
import lombok.Getter;

public enum RmqConfigOption implements ConfigOption {

    HOST("rmq.host", "rabbitmq.host"),

    PORT("rmq.port", "rabbitmq.port"),

    USERNAME("rmq.username", "rabbitmq.username"),

    PASSWORD("rmq.password", "rabbitmq.password"),

    VIRTUAL_HOST("rmq.virtualHost", "rabbitmq.virtualHost");

    @Getter
    private final String configName;

    @Getter
    private final String otherName;

    RmqConfigOption(String configName, String otherName) {
        this.configName = configName;
        this.otherName = otherName;
    }

}