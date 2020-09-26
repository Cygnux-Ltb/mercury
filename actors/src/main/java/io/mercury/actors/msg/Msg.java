package io.mercury.actors.msg;

public interface Msg {

	Envelope envelope();

	Content content();

	public static interface Envelope {

	}

	public static interface Content {

	}

}
