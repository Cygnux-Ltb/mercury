package io.mercury.actors.base;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public abstract class CommonActor extends AbstractActor {

	protected final ActorRef self = self();

	protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	protected CommonActor() {
		log.info("Created Actor -> {}", self);
	}

	protected final ReceiveBuilder baseReceiveBuilder() {
		return receiveBuilder().matchAny(this::handleUnknown);
	}

	private final void handleUnknown(Object obj) {
		log.error("Received unmatch message -> class==[{}] obj -> {}", obj.getClass().getName(), obj);
		handleUnknown0(obj);
	}

	protected abstract void handleUnknown0(Object t);

	protected void stop() {
		log.info("Stop actor -> {}", self);
		getContext().stop(self);
	}

}
