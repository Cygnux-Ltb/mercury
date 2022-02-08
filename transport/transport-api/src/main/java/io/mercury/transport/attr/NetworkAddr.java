package io.mercury.transport.attr;

import static io.mercury.transport.attr.NetworkProtocol.TCP;

public final class TcpAddr {

	private final String addr;
	private final int port;

	private final String info;

	private TcpAddr(String addr, int port) {
		this.addr = addr;
		this.port = port;
		this.info = TCP.getPrefix() + addr + ":" + port;
	}

	/**
	 * 
	 * @param port
	 * @return
	 */
	public static final TcpAddr localhost(int port) {
		return new TcpAddr("127.0.0.1", port);
	}

	/**
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public static final TcpAddr with(String addr, int port) {
		return new TcpAddr(addr, port);
	}

	public String getAddr() {
		return addr;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return info;
	}

}
