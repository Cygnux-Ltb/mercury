package io.mercury.actors.reference;

import io.mercury.actors.base.CommonActor;

public abstract class GenericActorT5<T1, T2, T3, T4, T5> extends CommonActor {

	private Class<T1> type1;
	private Class<T2> type2;
	private Class<T3> type3;
	private Class<T4> type4;
	private Class<T5> type5;

	protected GenericActorT5() {
		this.type1 = eventType1();
		this.type2 = eventType2();
		this.type3 = eventType3();
		this.type4 = eventType4();
		this.type5 = eventType5();
	}

	@Override
	public final Receive createReceive() {
		return commonReceiveBuilder()

				.match(type1, this::onEvent1)

				.match(type2, this::onEvent2)

				.match(type3, this::onEvent3)

				.match(type4, this::onEvent4)

				.match(type5, this::onEvent5)

				.build();
	}

	protected abstract Class<T1> eventType1();

	protected abstract Class<T2> eventType2();

	protected abstract Class<T3> eventType3();

	protected abstract Class<T4> eventType4();

	protected abstract Class<T5> eventType5();

	protected abstract void onEvent1(T1 t1);

	protected abstract void onEvent2(T2 t2);

	protected abstract void onEvent3(T3 t2);

	protected abstract void onEvent4(T4 t4);

	protected abstract void onEvent5(T5 t5);

}
