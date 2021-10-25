package io.mercury.transport.zmq.cfg;

public final class ZmqAddress {

	private final String addr;

	private ZmqAddress(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return addr;
	}

	@Override
	public String toString() {
		return addr;
	}

	/**
	 * 
	 * @param port
	 * @return
	 */
	public final static ZmqAddress tcp(int port) {
		return tcp("*", port);
	}

	/**
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public final static ZmqAddress tcp(String addr, int port) {
		return new ZmqAddress("tcp://" + addr + ":" + port);
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public final static ZmqAddress ipc(String addr) {
		return new ZmqAddress("ipc://" + addr);
	}

}
