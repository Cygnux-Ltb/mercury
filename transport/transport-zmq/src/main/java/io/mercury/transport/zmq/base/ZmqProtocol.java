package io.mercury.transport.zmq.enums;

/**
 * 当前支持的协议类型
 *
 * @author yellow013
 */
public enum ZmqProtocol {

    TCP("tcp"),

    IPC("ipc"),

    INPROC("inproc");

    private final String name;
    private final String prefix;

    ZmqProtocol(String name) {
        this.name = name;
        this.prefix = name + "://";
    }

    public String fixAddr(String addr) {
        if (!addr.startsWith(prefix))
            return prefix + addr;
        return addr;
    }

    @Override
    public String toString() {
        return name();
    }

    public static ZmqProtocol of(String name) {
        for (ZmqProtocol protocol : ZmqProtocol.values()) {
            if (protocol.name.equalsIgnoreCase(name))
                return protocol;
            if (protocol.prefix.equalsIgnoreCase(name))
                return protocol;
        }
        throw new IllegalArgumentException("Unsupported protocol type -> " + name);
    }

}