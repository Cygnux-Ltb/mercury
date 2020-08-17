package io.mercury.actors.reference;

import io.mercury.actors.base.CommonActor;

public abstract class GenericActorE4<E1, E2, E3, E4> extends CommonActor {

	private Class<E1> type1;
	private Class<E2> type2;
	private Class<E3> type3;
	private Class<E4> type4;

	protected GenericActorE4() {
		this.type1 = eventType1();
		this.type2 = eventType2();
		this.type3 = eventType3();
		this.type4 = eventType4();
	}

	@Override
	public final Receive createReceive() {
		return commonReceiveBuilder()

				.match(type1, this::onEvent1)

				.match(type2, this::onEvent2)

				.match(type3, this::onEvent3)

				.match(type4, this::onEvent4)

				.build();
	}

	protected abstract Class<E1> eventType1();

	protected abstract Class<E2> eventType2();

	protected abstract Class<E3> eventType3();

	protected abstract Class<E4> eventType4();

	protected abstract void onEvent1(E1 t1);

	protected abstract void onEvent2(E2 t2);

	protected abstract void onEvent3(E3 t2);

	protected abstract void onEvent4(E4 t4);

}
