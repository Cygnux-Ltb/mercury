package io.mercury.library.ignite.demo.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.io.Serial;
import java.util.Collections;

/**
 * @author ：lichuankang
 * @date ：2021/10/20 17:48
 * @description ：
 */
public class IgniteExampleMain {

	public static void main(String[] args) {
		// Preparing IgniteConfiguration using Java APIs
		IgniteConfiguration cfg = new IgniteConfiguration();

		// The node will be started as a client node.
		cfg.setClientMode(true);

		// Classes of custom Java logic will be transferred over the wire from this app.
		cfg.setPeerClassLoadingEnabled(true);

		// Setting up an IP Finder to ensure the client can locate the servers.
		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
		cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

		// Starting the node
		Ignite ignite = Ignition.start(cfg);

		// Create an IgniteCache and put some values in it.
		IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
		cache.put(1, "Hello");
		cache.put(2, "World!");

		System.out.println(">> Created the cache and add the values.");

		// Executing custom Java compute task on server nodes.
		ignite.compute(ignite.cluster().forServers());
		// ignite.compute(ignite.cluster().forServers()).broadcast(new RemoteTask());

		System.out.println(">> Compute task is executed, check for output on the server nodes.");
		IgniteCache<Integer, String> cacheValue = ignite.cache("myCache");

		System.out.println(">> " + cacheValue.get(1) + " " + cacheValue.get(2));

		// Disconnect from the cluster.
		ignite.close();
	}

	/**
	 * A compute tasks that prints out a node ID and some details about its OS and
	 * JRE. Plus, the code shows how to access data stored in a cache from the
	 * compute task.
	 */
	@SuppressWarnings("unused")
	private static class RemoteTask implements IgniteRunnable {

		@Serial
		private static final long serialVersionUID = 4404881047854464814L;

		@IgniteInstanceResource
		Ignite ignite;

		@Override
		public void run() {
			System.out.println(">> Executing the compute task");

			System.out.println("   Node ID: " + ignite.cluster().localNode().id() + "\n" + "   OS: "
					+ System.getProperty("os.name") + "   JRE: " + System.getProperty("java.runtime.name"));

			IgniteCache<Integer, String> cache = ignite.cache("myCache");

			System.out.println(">> " + cache.get(1) + " " + cache.get(2));
		}
	}
}
