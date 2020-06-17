package io.mercury.transport.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import io.mercury.transport.socket.configurator.SocketConfigurator;

public class LocalSocketManager {

	private static Map<String, SocketTransceiver> serverSocketMap = new ConcurrentHashMap<>(8);

	/**
	 * 
	 * @param name
	 * @param port
	 * @param callback
	 * @return
	 */
	public static synchronized SocketTransceiver getSocketTransceiver(String host, int port,
			Consumer<byte[]> callback) {
		String socketName = socketName(host, port);
		if (serverSocketMap.containsKey(socketName))
			return serverSocketMap.get(socketName);
		else {
			if (port <= 7000 || port >= 8000)
				throw new RuntimeException("port error.");
			SocketTransceiver transceiver = new SocketTransceiver(SocketConfigurator.builder().port(port).build(),
					callback);
			serverSocketMap.put(socketName, transceiver);
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
		if (serverSocketMap.containsKey(name))
			return serverSocketMap.get(name);
		else
			return null;
	}

	private static String socketName(String name, int port) {
		return name + ":" + port;
	}

}
