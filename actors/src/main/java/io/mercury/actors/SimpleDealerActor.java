package io.mercury.actors;

import org.eclipse.collections.api.list.MutableList;

import akka.actor.ActorRef;
import io.mercury.actors.ref.GenericActorE2;
import io.mercury.common.collections.MutableLists;

public abstract class SimpleDealerActor<T> extends GenericActorE2<ActorRef, T> {

	private MutableList<ActorRef> registered;
	private int turn = -1;

	protected SimpleDealerActor() {
		this.registered = MutableLists.newFastList(8);
	}

	@Override
	protected Class<ActorRef> eventType0() {
		return ActorRef.class;
	}

	@Override
	protected void onEvent0(ActorRef t0) {
		registered.add(t0);
	}

	@Override
	protected void onEvent1(T t1) {
		if (registered.isEmpty())
			handleRegisteredIsEmpty(t1);
		else
			registered.get(nextActorIndex()).forward(t1, getContext());
	}

	private void handleRegisteredIsEmpty(T t1) {
		// TODO
	}

	private int nextActorIndex() {
		if (++turn == registered.size())
			turn = 0;
		return turn;
	}

}
