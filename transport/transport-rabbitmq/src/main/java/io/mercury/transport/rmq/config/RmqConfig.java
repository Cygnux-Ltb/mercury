package io.mercury.transport.rmq.cfg;

import lombok.Getter;

/**
 * @author yellow013
 */
public abstract class RmqCfg {

    /**
     * 连接配置信息
     */
    @Getter
    private final RmqConnection connection;

    protected RmqCfg(RmqConnection connection) {
        this.connection = connection;
    }

}
