package io.mercury.actors.example.msg;

import akka.actor.typed.ActorRef;

public final class Greet {

	private final String whom;
	private final ActorRef<Greeted> replyTo;

	public Greet(String whom, ActorRef<Greeted> replyTo) {
		this.whom = whom;
		this.replyTo = replyTo;
	}

	public String getWhom() {
		return whom;
	}

	public ActorRef<Greeted> getReplyTo() {
		return replyTo;
	}

}