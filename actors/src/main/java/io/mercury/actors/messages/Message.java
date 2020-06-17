package io.mercury.actors.messages;

public interface Message {

	Envelope envelope();

	Content content();

	public interface Envelope {

	}

	public interface Content {

	}

}
