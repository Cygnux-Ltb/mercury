package io.mercury.transport.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import io.mercury.common.lang.Assertor;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public class LocalSocketManager {

	private static final Map<String, SocketTransceiver> ServerSocketMap = new ConcurrentHashMap<>(8);

	/**
	 * 
	 * @param host
	 * @param port
	 * @param callback
	 * @return
	 */
	public static synchronized SocketTransceiver getSocketTransceiver(String host, int port,
			Consumer<byte[]> callback) {
		String socketName = socketName(host, port);
		if (ServerSocketMap.containsKey(socketName))
			return ServerSocketMap.get(socketName);
		else {
			Assertor.atWithinRange(port, 7000, 8000, "port");
			SocketTransceiver transceiver = new SocketTransceiver(SocketConfigurator.builder().port(port).build(),
					callback);
			ServerSocketMap.put(socketName, transceiver);
			return transceiver;
		}
	}

	/**
	 * Return SocketTransceiver obj or null.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	public static SocketTransceiver acquireSocketTransceiver(String name, int port) {
		if (ServerSocketMap.containsKey(name))
			return ServerSocketMap.get(name);
		else
			return null;
	}

	private static String socketName(String name, int port) {
		return name + ":" + port;
	}

}
