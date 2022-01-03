package io.mercury.actors;

public abstract class CommonActorT1<T> extends CommonActor {

	protected final Class<T> type;

	protected CommonActorT1() {
		this.type = eventType();
	}

	@Override
	public final Receive createReceive() {
		return baseReceiveBuilder()
				// match type
				.match(type, this::onEvent)
				// build
				.build();
	}

	protected abstract Class<T> eventType();

	protected abstract void onEvent(T t);

}
