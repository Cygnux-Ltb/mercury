package io.mercury.transport.rmq.cfg;

/**
 * @author yellow013
 */
public abstract class RmqCfg {

    /**
     * 连接配置信息
     */
    private final RmqConnection connection;

    protected RmqCfg(RmqConnection connection) {
        this.connection = connection;
    }

    public RmqConnection getConnection() {
        return connection;
    }

}
