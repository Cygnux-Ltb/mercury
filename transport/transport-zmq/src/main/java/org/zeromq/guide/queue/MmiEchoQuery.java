package org.zeromq.guide.queue;

import org.zeromq.ZMsg;
import org.zeromq.guide.majordomo.MajordomoProtocolClientAPI;

/**
 * MMI echo query example
 */
public class MmiEchoQuery {

    public static void main(String[] args) {
        boolean verbose = (args.length > 0 && "-v".equals(args[0]));
        MajordomoProtocolClientAPI clientSession = new MajordomoProtocolClientAPI("tcp://localhost:5555", verbose);

        ZMsg request = new ZMsg();

        // This is the service we want to look up
        request.addString("echo");

        // This is the service we send our request to
        ZMsg reply = clientSession.send("mmi.service", request);

        if (reply != null) {
            String replyCode = reply.getFirst().toString();
            System.out.printf("Lookup echo service: %s\n", replyCode);
        } else {
            System.out.println("E: no response from broker, make sure it's running");
        }

        clientSession.destroy();
    }

}
