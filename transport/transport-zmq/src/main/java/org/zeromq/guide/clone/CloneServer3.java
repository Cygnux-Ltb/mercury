package org.zeromq.guide.clone;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.guide.util.KvSimple;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clone server Model Three
 *
 * @author Danish Shrestha &lt;dshrestha06@gmail.com&gt;
 */
public class CloneServer3 {

    private static final Map<String, KvSimple> kvMap = new LinkedHashMap<>();

    public void run() {
        try (ZContext ctx = new ZContext()) {
            Socket snapshot = ctx.createSocket(SocketType.ROUTER);
            snapshot.bind("tcp://*:5556");

            Socket publisher = ctx.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5557");

            Socket collector = ctx.createSocket(SocketType.PULL);
            collector.bind("tcp://*:5558");

            Poller poller = ctx.createPoller(2);
            poller.register(collector, Poller.POLLIN);
            poller.register(snapshot, Poller.POLLIN);

            long sequence = 0;
            while (!Thread.currentThread().isInterrupted()) {
                if (poller.poll(1000) < 0)
                    break; // Context has been shut down

                // apply state updates from main thread
                if (poller.pollin(0)) {
                    KvSimple kvMsg = KvSimple.recv(collector);
                    if (kvMsg == null) // Interrupted
                        break;
                    kvMsg.setSequence(++sequence);
                    kvMsg.send(publisher);
                    CloneServer3.kvMap.put(kvMsg.getKey(), kvMsg);
                    System.out.printf("I: publishing update %5d\n", sequence);
                }

                // execute state snapshot request
                if (poller.pollin(1)) {
                    byte[] identity = snapshot.recv(0);
                    if (identity == null)
                        break; // Interrupted
                    String request = snapshot.recvStr();

                    if (!request.equals("ICANHAZ?")) {
                        System.out.println("E: bad request, aborting");
                        break;
                    }

                    for (Entry<String, KvSimple> entry : kvMap.entrySet()) {
                        KvSimple msg = entry.getValue();
                        System.out.println("Sending message " + entry.getValue().getSequence());
                        this.sendMessage(msg, identity, snapshot);
                    }

                    // now send end message with getSequence number
                    System.out.println("Sending state snapshot = " + sequence);
                    snapshot.send(identity, ZMQ.SNDMORE);
                    KvSimple message = new KvSimple("KTHXBAI", sequence, ZMQ.SUBSCRIPTION_ALL);
                    message.send(snapshot);
                }
            }

            System.out.printf(" Interrupted\n%d messages handled\n", sequence);
        }
    }

    private void sendMessage(KvSimple msg, byte[] identity, Socket snapshot) {
        snapshot.send(identity, ZMQ.SNDMORE);
        msg.send(snapshot);
    }

    public static void main(String[] args) {
        new CloneServer3().run();
    }
}
