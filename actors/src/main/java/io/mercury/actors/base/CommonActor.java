package io.mercury.actors.base;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public abstract class CommonActor extends AbstractActor {

	protected final ActorRef self = self();

	protected final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	protected CommonActor() {
		logger.info("Created Actor -> {}", self);
	}

	protected void commonHandleUnknown(Object obj) {
		logger.error("Received unmatched message -> class==[{}] obj==[{}]", obj.getClass().getName(), obj);
	}

	protected void stop() {
		logger.info("Destroy Actor -> {}", self);
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
