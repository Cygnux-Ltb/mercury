package io.mercury.actors.ref;

import io.mercury.actors.base.CommonActor;

public abstract class GenericActorE2<T0, T1> extends CommonActor {

	private Class<T0> type0;
	private Class<T1> type1;

	protected GenericActorE2() {
		this.type0 = eventType0();
		this.type1 = eventType1();
	}

	@Override
	public final Receive createReceive() {
		return commonReceiveBuilder()
				// match type0
				.match(type0, this::onEvent0)
				// match type1
				.match(type1, this::onEvent1)
				// build
				.build();
	}

	protected abstract Class<T0> eventType0();

	protected abstract Class<T1> eventType1();

	protected abstract void onEvent0(T0 t0);

	protected abstract void onEvent1(T1 t1);

}
