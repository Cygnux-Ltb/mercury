package org.zeromq.guide;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

/**
 * Multi Thread Hello World server
 */
public class MultiThreadServer {

	private static class Worker extends Thread {
		private final ZContext context;

		private Worker(ZContext context) {
			this.context = context;
		}

		@Override
		public void run() {
			ZMQ.Socket socket = context.createSocket(SocketType.REP);
			socket.connect("inproc://workers");

			while (true) {

				// Wait for next request from client (C string)
				String request = socket.recvStr(0);
				System.out.println(Thread.currentThread().getName() + " Received request: [" + request + "]");

				// Do some 'work'
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignored) {
				}

				// Send reply back to client (C string)
				socket.send("world", 0);
			}
		}
	}

	public static void main(String[] args) {
		try (ZContext context = new ZContext()) {
			Socket clients = context.createSocket(SocketType.ROUTER);
			clients.bind("tcp://*:5555");

			Socket workers = context.createSocket(SocketType.DEALER);
			workers.bind("inproc://workers");

			for (int thread_nbr = 0; thread_nbr < 5; thread_nbr++) {
				Thread worker = new Worker(context);
				worker.start();
			}

			// Connect work threads to client threads via a queue
			ZMQ.proxy(clients, workers, null);
		}
	}
}
