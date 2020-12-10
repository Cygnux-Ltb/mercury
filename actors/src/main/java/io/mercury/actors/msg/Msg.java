package io.mercury.actors.msg;

public interface Msg {

	Envelope envelope();

	Content content();

	public static interface Envelope {

		int type();

	}

	public static interface Content {

	}

}
