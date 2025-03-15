package io.mercury.fibry;

import eu.lucaventuri.fibry.Actor;
import eu.lucaventuri.fibry.ActorSystem;

public class FibryTest {

    public static void main(String[] args) {
        Actor<Object, Void, Void> objectVoidVoidActor = ActorSystem.named("").newActor(System.out::println);
    }

}
