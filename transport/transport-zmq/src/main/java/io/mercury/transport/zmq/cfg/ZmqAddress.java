package io.mercury.transport.zmq.cfg;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class ZmqAddress {

	@Getter
	private final String addr;

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
