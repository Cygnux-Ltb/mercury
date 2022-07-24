package io.mercury.example;

import java.util.Random;

import io.mercury.actor.Actor;
import io.mercury.actor.IActorRef;
import io.mercury.actor.IActorSystem;

public class AskTellLaterExample {

    public static void main(String[] args) throws InterruptedException {
        final IActorSystem system = Actor.newSystem("example");
        final IActorRef<Printer> printerActor = system.actorOf(Printer::new);
        final IActorRef<Randomizer> randomizerActor = system.actorOf(Randomizer::new);
        final IActorRef<Looper> looperActor = system.actorOf(() -> new Looper(printerActor, randomizerActor));

        looperActor.tell(Looper::run);

        system.shutdownCompletable().join();

    }

    private static class Printer {
        public void print(String s) {
            System.err.println("[Printer] " + s);
        }
    }

    private static class Randomizer {

        private final Random random = new Random();

        public int random() {
            System.err.println("[Randomizer] >>> ");
            try {
                return random.nextInt(10000);
            } finally {
                System.err.println("[Randomizer] <<< ");
            }
        }
    }

    public static class Looper {

        private final IActorRef<Printer> printerActor;
        private final IActorRef<Randomizer> randomizerActor;

        private int iteration = 0;

        public Looper(IActorRef<Printer> printerActor, IActorRef<Randomizer> randomizerActor) {
            this.printerActor = printerActor;
            this.randomizerActor = randomizerActor;
        }

        private void run() {
            iteration++;
            printerActor.tell(printer -> printer.print("Looper: iteration " + iteration));
            randomizerActor.ask(Randomizer::random, this::showRandom);
            if (iteration < 10) {
                Actor.<Looper> current().later(Looper::run, 1000);
            } else {
                Actor.system().shutdown();
            }
        }

        private void showRandom(int rand) {
            printerActor.tell(printer -> printer.print("Looper: Randomizer returned: " + rand));
        }

    }

}
