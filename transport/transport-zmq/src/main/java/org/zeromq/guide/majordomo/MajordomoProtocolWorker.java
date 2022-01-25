package org.zeromq.guide.majordomo;

import org.zeromq.ZMsg;

/**
 * Majordomo Protocol worker example. Uses the mdwrk API to hide all MDP aspects
 *
 */
public class MajordomoProtocolWorker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean verbose = (args.length > 0 && "-v".equals(args[0]));
		MajordomoProtocolWorkerAPI workerSession = new MajordomoProtocolWorkerAPI("tcp://localhost:5555", "echo",
				verbose);

		ZMsg reply = null;
		while (!Thread.currentThread().isInterrupted()) {
			ZMsg request = workerSession.receive(reply);
			if (request == null)
				break; // Interrupted
			reply = request; // Echo is complex :-)
		}
		workerSession.destroy();
	}
}
