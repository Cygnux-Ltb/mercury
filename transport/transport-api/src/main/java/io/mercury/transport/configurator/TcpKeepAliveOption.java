package io.mercury.transport.configurator;

public final class TcpKeepAliveOption {

	private int tcpKeepAlive;
	private int tcpKeepAliveCount;
	private int tcpKeepAliveIdle;
	private int tcpKeepAliveInterval;

	public int getTcpKeepAlive() {
		return tcpKeepAlive;
	}

	public int getTcpKeepAliveCount() {
		return tcpKeepAliveCount;
	}

	public int getTcpKeepAliveIdle() {
		return tcpKeepAliveIdle;
	}

	public int getTcpKeepAliveInterval() {
		return tcpKeepAliveInterval;
	}

	public TcpKeepAliveOption setTcpKeepAlive(int tcpKeepAlive) {
		this.tcpKeepAlive = tcpKeepAlive;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveCount(int tcpKeepAliveCount) {
		this.tcpKeepAliveCount = tcpKeepAliveCount;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveIdle(int tcpKeepAliveIdle) {
		this.tcpKeepAliveIdle = tcpKeepAliveIdle;
		return this;
	}

	public TcpKeepAliveOption setTcpKeepAliveInterval(int tcpKeepAliveInterval) {
		this.tcpKeepAliveInterval = tcpKeepAliveInterval;
		return this;
	}

}
