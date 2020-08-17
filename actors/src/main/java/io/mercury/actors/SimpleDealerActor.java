package io.mercury.actors;

import org.eclipse.collections.api.list.MutableList;

import akka.actor.ActorRef;
import io.mercury.actors.reference.GenericActorE2;
import io.mercury.common.collections.MutableLists;

public abstract class SimpleDealerActor<T> extends GenericActorE2<ActorRef, T> {

	private MutableList<ActorRef> registered;
	private int turn = -1;

	protected SimpleDealerActor() {
		this.registered = MutableLists.newFastList(8);
	}

	@Override
	protected Class<ActorRef> eventType1() {
		return ActorRef.class;
	}

	@Override
	protected void onEvent1(ActorRef t1) {
		registered.add(t1);
	}

	@Override
	protected void onEvent2(T t2) {
		if (registered.isEmpty())
			handleRegisteredIsEmpty(t2);
		else
			registered.get(nextActorIndex()).forward(t2, getContext());
	}

	private void handleRegisteredIsEmpty(T t2) {
		// TODO
	}

	private int nextActorIndex() {
		if (++turn == registered.size())
			turn = 0;
		return turn;
	}

}
