package io.mercury.vsakka;

import io.mercury.actors.IActorRef;

public class Greeter {

    private final String message;
    private final IActorRef<Printer> printerActor;
    private String greeting;

    public Greeter(String message, IActorRef<Printer> printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    public void setWhoToGreet(String whoToGreet) {
        this.greeting = message + ", " + whoToGreet;
    }

    public void greet() {
        String greetingMsg = greeting;
        printerActor.tell(printer -> printer.print(greetingMsg));
    }

}
