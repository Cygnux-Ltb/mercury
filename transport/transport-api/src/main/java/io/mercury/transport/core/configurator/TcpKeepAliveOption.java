package io.mercury.transport.core.configurator;

public final class TcpKeepAliveOption {

	private int TcpKeepAlive;
	private int TcpKeepAliveCount;
	private int TcpKeepAliveIdle;
	private int TcpKeepAliveInterval;

	public int getTcpKeepAlive() {
		return TcpKeepAlive;
	}

	public int getTcpKeepAliveCount() {
		return TcpKeepAliveCount;
	}

	public int getTcpKeepAliveIdle() {
		return TcpKeepAliveIdle;
	}

	public int getTcpKeepAliveInterval() {
		return TcpKeepAliveInterval;
	}

	public TcpKeepAliveOption setTcpKeepAlive(int tcpKeepAlive) {
		TcpKeepAlive = tcpKeepAlive;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveCount(int tcpKeepAliveCount) {
		TcpKeepAliveCount = tcpKeepAliveCount;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveIdle(int tcpKeepAliveIdle) {
		TcpKeepAliveIdle = tcpKeepAliveIdle;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveInterval(int tcpKeepAliveInterval) {
		TcpKeepAliveInterval = tcpKeepAliveInterval;
		return this;
	}

}
