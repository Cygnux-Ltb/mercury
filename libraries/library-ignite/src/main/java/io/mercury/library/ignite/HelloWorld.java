package io.mercury.library.ignite;

import java.util.Collections;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

/**
 *
 */
public class HelloWorld {

	public static void main(String[] args) throws IgniteException {
		// Preparing IgniteConfiguration using Java APIs
		IgniteConfiguration cfg = new IgniteConfiguration()
				// The node will be started as a client node.
				.setClientMode(true)
				// Classes of custom Java logic will be transferred over the wire from this APP.
				.setPeerClassLoadingEnabled(true)
				// Setting up an IP Finder to ensure the client can locate the servers.
				.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(new TcpDiscoveryMulticastIpFinder()
						.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"))));

		// Starting the node
		final Ignite ignite = Ignition.start(cfg);

		// Create an IgniteCache and put some values in it.
		final IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
		cache.put(1, "Hello");
		cache.put(2, "World!");

		System.out.println(">> Created the cache and add the values.");

		// Executing custom Java compute task on server nodes.
		ignite.compute(ignite.cluster().forServers()).broadcast(new RemoteTask());

		System.out.println(">> Compute task is executed, check for output on the server nodes.");

		// Disconnect from the cluster.
		ignite.close();
	}

	/**
	 * A compute tasks that prints out a node ID and some details about its OS and
	 * JRE. Plus, the code shows how to access data stored in a cache from the
	 * compute task.
	 */
	private static class RemoteTask implements IgniteRunnable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2816115717453185083L;

		@IgniteInstanceResource
		private Ignite ignite;

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
