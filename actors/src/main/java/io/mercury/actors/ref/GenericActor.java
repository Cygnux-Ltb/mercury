package io.mercury.actors.ref;

import io.mercury.actors.base.CommonActor;

public abstract class GenericActor<T> extends CommonActor {

	private Class<T> type;

	protected GenericActor() {
		this.type = eventType();
	}

	@Override
	public final Receive createReceive() {
		return commonReceiveBuilder()
				// match type
				.match(type, this::onEvent)
				// build
				.build();
	}

	protected abstract Class<T> eventType();

	protected abstract void onEvent(T t);

}
