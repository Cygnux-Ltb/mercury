package com.lightbend.akkasample.sample3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.lightbend.akkasample.StdIn;

/**
 * Supervision
 * <p>
 * see more
 * <a href="http://doc.akka.io/docs/akka/2.4/general/supervision.html">http://doc.akka.io/docs/akka/2.4/general/supervision.html</a>
 * and
 * <a href="http://doc.akka.io/docs/akka/2.4.9/java/lambda-fault-tolerance.html">http://doc.akka.io/docs/akka/2.4.9/java/lambda-fault-tolerance.html</a>
 */
public class App {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();

        final ActorRef supervisor = system.actorOf(Supervisor.props(), "supervisor");

        for (int i = 0; i < 10; i++) {
            supervisor.tell(new NonTrustWorthyChild.Command(), ActorRef.noSender());
        }

        System.out.println("ENTER to terminate");
        StdIn.readLine();
        system.terminate();
    }

}
