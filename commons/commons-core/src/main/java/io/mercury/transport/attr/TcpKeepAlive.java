package io.mercury.transport.attr;

import io.mercury.common.annotation.OnlyOverrideEquals;

@OnlyOverrideEquals
public final class TcpKeepAlive {

    private final KeepAliveOption keepAliveOption;

    private int keepAliveCount;

    private int keepAliveIdle;

    private int keepAliveInterval;

    private TcpKeepAlive(KeepAliveOption keepAliveOption) {
        this.keepAliveOption = keepAliveOption;
    }

    public KeepAliveOption getKeepAliveOption() {
        return keepAliveOption;
    }

    public int getKeepAliveCount() {
        return keepAliveCount;
    }

    public int getKeepAliveIdle() {
        return keepAliveIdle;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public TcpKeepAlive setKeepAliveCount(int keepAliveCount) {
        this.keepAliveCount = keepAliveCount;
        return this;
    }

    public TcpKeepAlive setKeepAliveIdle(int keepAliveIdle) {
        this.keepAliveIdle = keepAliveIdle;
        return this;
    }

    public TcpKeepAlive setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TcpKeepAlive o)) {
            return false;
        } else {
            if (!this.keepAliveOption.equals(o.getKeepAliveOption()))
                return false;
            if (this.keepAliveCount != o.getKeepAliveCount())
                return false;
            if (this.keepAliveIdle != o.getKeepAliveIdle())
                return false;
            return this.keepAliveInterval == o.getKeepAliveInterval();
        }
    }

    /**
     * @return TcpKeepAlive
     */
    public static TcpKeepAlive enable() {
        return new TcpKeepAlive(KeepAliveOption.Enable);
    }

    /**
     * @return TcpKeepAlive
     */
    public static TcpKeepAlive disable() {
        return new TcpKeepAlive(KeepAliveOption.Disable);
    }

    /**
     * @return TcpKeepAlive
     */
    public static TcpKeepAlive withDefault() {
        return new TcpKeepAlive(KeepAliveOption.Default);
    }

    /**
     * @author yellow013
     */
    public enum KeepAliveOption {

        Enable(1),

        Disable(0),

        Default(-1),

        ;

        private final int code;

        KeepAliveOption(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

}
