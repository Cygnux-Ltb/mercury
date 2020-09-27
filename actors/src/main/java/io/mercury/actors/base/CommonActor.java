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

	protected final ReceiveBuilder commonReceiveBuilder() {
		return receiveBuilder().matchAny(this::handleUnknown);
	}

	private final void handleUnknown(Object obj) {
		log.error("Received unmatched message -> class==[{}] obj -> {}", obj.getClass().getName(), obj);
		handleUnknown0(obj);
	}

	protected abstract void handleUnknown0(Object t);

	protected void stop() {
		log.info("Stop actor -> {}", self);
		getContext().stop(self);
	}

	/**
	 * Returns this AbstractActor's ActorContext.<br>
	 * The ActorContext is not thread safe so do not expose it outside of the
	 * AbstractActor.<br>
	 * Same as {@link AbstractActor#context()}
	 */
	@Override
	public final ActorContext getContext() {
		return super.getContext();
	}

	/**
	 * Return the sender of the <b>current</b> message.<br>
	 * Same as {@link AbstractActor#sender()}
	 */
	@Override
	public final ActorRef getSender() {
		return super.getSender();
	}

	/**
	 * Returns the ActorRef for this actor.<br>
	 * Same as {@link AbstractActor#self()}.
	 */
	@Override
	public final ActorRef getSelf() {
		return super.getSelf();
	}

}
