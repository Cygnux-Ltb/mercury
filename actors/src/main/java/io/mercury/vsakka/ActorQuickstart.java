package io.mercury.vsakka;

import java.io.IOException;

import io.mercury.actor.Actor;
import io.mercury.actor.IActorRef;
import io.mercury.actor.IActorSystem;

public class ActorQuickstart {

    public static void main(String[] args) {
        final IActorSystem system = Actor.newSystem("hello-actor");
        try {
            // #create-actors
            final IActorRef<Printer> printerActor = system.actorOf(Printer::new, "printerActor");
            final IActorRef<Greeter> howdyGreeter = system.actorOf(() -> new Greeter("Howdy", printerActor), "howdyGreeter");
            final IActorRef<Greeter> helloGreeter = system.actorOf(() -> new Greeter("Hello", printerActor), "helloGreeter");
            final IActorRef<Greeter> goodDayGreeter = system.actorOf(() -> new Greeter("Good day", printerActor), "goodDayGreeter");
            // #create-actors

            // #main-send-messages
            howdyGreeter.tell(gr -> gr.setWhoToGreet("Actr"));
            howdyGreeter.tell(Greeter::greet);

            howdyGreeter.tell(gr -> gr.setWhoToGreet("Zakgof"));
            howdyGreeter.tell(Greeter::greet);

            helloGreeter.tell(gr -> gr.setWhoToGreet("Java"));
            helloGreeter.tell(Greeter::greet);

            goodDayGreeter.tell(gr -> gr.setWhoToGreet("Lambda"));
            goodDayGreeter.tell(Greeter::greet);
            // #main-send-messages

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            system.shutdown();
        }
    }

}