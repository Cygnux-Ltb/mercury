package io.mercury.actors.reference;

import io.mercury.actors.base.CommonActor;

public abstract class GenericActorE1<T> extends CommonActor {

	private Class<T> type;

	protected GenericActorE1() {
		this.type = eventType();
	}

	@Override
	public final Receive createReceive() {
		return commonReceiveBuilder()
				
				.match(type, this::onEvent)
				
				.build();
	}

	protected abstract Class<T> eventType();

	protected abstract void onEvent(T t);

}
