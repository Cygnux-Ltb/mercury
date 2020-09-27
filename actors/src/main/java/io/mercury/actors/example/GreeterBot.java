package io.mercury.actors.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.mercury.actors.example.Greeter.Greeted;

/**
 * 
 * @author Akka official
 *
 */
public class GreeterBot extends AbstractBehavior<Greeter.Greeted> {

	public static Behavior<Greeter.Greeted> create(int max) {
		return Behaviors.setup(context -> new GreeterBot(context, max));
	}

	private final int max;
	private int greetingCounter;

	private GreeterBot(ActorContext<Greeted> context, int max) {
		super(context);
		this.max = max;
	}

	@Override
	public Receive<Greeted> createReceive() {
		return newReceiveBuilder().onMessage(Greeter.Greeted.class, this::onGreeted).build();
	}

	private Behavior<Greeted> onGreeted(Greeter.Greeted message) {
		greetingCounter++;
		getContext().getLog().info("Greeting {} for {}", greetingCounter, message.whom);
		if (greetingCounter == max) {
			return Behaviors.stopped();
		} else {
			message.from.tell(new Greeter.Greet(message.whom, getContext().getSelf()));
			return this;
		}
	}
}