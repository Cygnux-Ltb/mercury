package io.mercury.actors.example.msg;

public final class SayHello {

	private final String name;

	public SayHello(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
